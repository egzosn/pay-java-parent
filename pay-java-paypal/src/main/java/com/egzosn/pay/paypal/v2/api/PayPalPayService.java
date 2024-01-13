package com.egzosn.pay.paypal.v2.api;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpHeader;
import com.egzosn.pay.common.http.HttpStringEntity;
import com.egzosn.pay.common.http.ResponseEntity;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.IOUtils;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.v2.bean.Constants;
import com.egzosn.pay.paypal.v2.bean.PayPalRefundResult;
import com.egzosn.pay.paypal.v2.bean.PayPalTransactionType;
import com.egzosn.pay.paypal.v2.bean.order.ApplicationContext;
import com.egzosn.pay.paypal.v2.bean.order.Money;
import com.egzosn.pay.paypal.v2.bean.order.OrderRequest;
import com.egzosn.pay.paypal.v2.bean.order.PurchaseUnitRequest;
import com.egzosn.pay.paypal.v2.bean.order.ShippingDetail;
import com.egzosn.pay.paypal.v2.utils.PayPalUtil;


/**
 * 贝宝支付配置存储
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2021-1-16 ‏‎22:15:09
 */
public class PayPalPayService extends BasePayService<PayPalConfigStorage> implements PayPalPayServiceInf {


    /**
     * 沙箱环境
     */
    private static final String SANDBOX_REQ_URL = "https://api.sandbox.paypal.com/";
    /**
     * 正式测试环境
     */
    private static final String REQ_URL = "https://api.paypal.com/";

    private static final String NOTIFY_VALIDATE_URL = "https://ipnpb.paypal.com/cgi-bin/webscr?cmd=_notify-validate&";
    private static final String SANDBOX_NOTIFY_VALIDATE_URL = "https://ipnpb.sandbox.paypal.com/cgi-bin/webscr?cmd=_notify-validate&";

    /**
     * 获取对应的请求地址
     *
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return (payConfigStorage.isTest() ? SANDBOX_REQ_URL : REQ_URL) + transactionType.getMethod();
    }


    /**
     * 获取通知校验对应的请求地址
     *
     * @param params 回调参数
     * @return 请求地址
     */
    public String getNotifyReqUrl(Map<String, Object> params) {
        return (payConfigStorage.isTest() ? SANDBOX_NOTIFY_VALIDATE_URL : NOTIFY_VALIDATE_URL) + UriVariables.getMapToParameters(params);
    }


    public PayPalPayService(PayPalConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }


    /**
     * 获取请求token
     *
     * @return 授权令牌
     */
    public String getAccessToken() {
        return getAccessToken(false);
    }

    /**
     * 获取授权令牌
     *
     * @param forceRefresh 是否重新获取， true重新获取
     * @return 新的授权令牌
     * @throws PayErrorException 支付异常
     */
    public String getAccessToken(boolean forceRefresh) throws PayErrorException {
        Lock lock = payConfigStorage.getAccessTokenLock();
        try {
            lock.lock();
            if (forceRefresh) {
                payConfigStorage.expireAccessToken();
            }
            if (payConfigStorage.isAccessTokenExpired()) {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Basic " + authorizationString(getPayConfigStorage().getAppId(), getPayConfigStorage().getKeyPrivate()));
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/x-www-form-urlencoded");
                try {
                    HttpStringEntity entity = new HttpStringEntity("grant_type=client_credentials", header);
                    JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.AUTHORIZE), entity, JSONObject.class);
                    payConfigStorage.updateAccessToken(String.format("%s %s", resp.getString("token_type"), resp.getString("access_token")), resp.getIntValue("expires_in"));

                }
                catch (UnsupportedEncodingException e) {
                    throw new PayErrorException(new PayException("failure", e.getMessage()));
                }
                return payConfigStorage.getAccessToken();
            }
        }
        finally {
            lock.unlock();
        }
        return payConfigStorage.getAccessToken();
    }


    /**
     * IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * 参数解析与校验  https://developer.paypal.com/docs/api-basics/notifications/ipn/IPNIntro/#id08CKFJ00JYK
     * 1.Check that the payment_status is Completed.
     * 2.If the payment_status is Completed, check the txn_id against the previous PayPal transaction that you processed to ensure the IPN message is not a duplicate.
     * 3.Check that the receiver_email is an email address registered in your PayPal account.
     * 4.Check that the price (carried in mc_gross) and the currency (carried in mc_currency) are correct for the item (carried in item_name or item_number).
     *
     * @param params 回调回来的参数集
     * @return 是否成功 true成功
     */
    @Deprecated
    @Override
    public boolean verify(Map<String, Object> params) {

        throw new PayErrorException(new PayException("failure", "payPal V2版本不支持此校验方式"));

    }


    /**
     * 保留IPN的校验方式
     *
     * @param noticeParams 参数
     * @return 结果
     */
    public boolean verifyIpn(NoticeParams noticeParams) {
        final Map<String, Object> params = noticeParams.getBody();
        Object paymentStatus = params.get("payment_status");
        if (!"Completed".equals(paymentStatus)) {
            LOG.warn("状态未完成:" + paymentStatus);
            return false;
        }
        String resp = getHttpRequestTemplate().getForObject(getNotifyReqUrl(params), authHeader(), String.class);
        return "VERIFIED".equals(resp);

    }

    @Override
    public boolean verify(NoticeParams noticeParams) {

        final Map<String, List<String>> headers = noticeParams.getHeaders();
        if (null == headers || headers.isEmpty()) {
            throw new PayErrorException(new PayException("failure", "校验失败，请求头不能为空"));
        }


        String clientCertificateLocation = noticeParams.getHeader(Constants.PAYPAL_HEADER_CERT_URL);
        ResponseEntity<InputStream> clientCertificateResponseEntity = requestTemplate.getForObjectEntity(clientCertificateLocation, InputStream.class);
        if (clientCertificateResponseEntity.getStatusCode() > 400) {
            LOG.error("获取证书信息失败，无法进行webHook校验:{}", clientCertificateLocation);
            return false;
        }
        InputStream inputStream = clientCertificateResponseEntity.getBody();
        Collection<X509Certificate> clientCerts = PayPalUtil.getCertificateFromStream(inputStream);
        String webHookId = payConfigStorage.getWebHookId();
        String actualSignatureEncoded = noticeParams.getHeader(Constants.PAYPAL_HEADER_TRANSMISSION_SIG);
        String authAlgo = noticeParams.getHeader(Constants.PAYPAL_HEADER_AUTH_ALGO);
        String transmissionId = noticeParams.getHeader(Constants.PAYPAL_HEADER_TRANSMISSION_ID);
        String transmissionTime = noticeParams.getHeader(Constants.PAYPAL_HEADER_TRANSMISSION_TIME);
        String requestBody = noticeParams.getBodyStr();
        String expectedSignature = String.format("%s|%s|%s|%s", transmissionId, transmissionTime, webHookId, PayPalUtil.crc32(requestBody));
        boolean isDataValid = PayPalUtil.validateData(clientCerts, authAlgo, actualSignatureEncoded, expectedSignature);
        LOG.debug("数据校验结果: {}", isDataValid);
        return isDataValid;

    }

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param request 通知请求
     * @return 获得回调的请求参数
     */
    @Override
    public NoticeParams getNoticeParams(NoticeRequest request) {
        NoticeParams noticeParams = new NoticeParams();
        try (InputStream is = request.getInputStream()) {
            String body = IOUtils.toString(is);
            noticeParams.setBodyStr(body);
            noticeParams.setBody(JSON.parseObject(body));
        }
        catch (IOException e) {
            throw new PayErrorException(new PayException("failure", "获取回调参数异常"), e);
        }
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, Collections.list(request.getHeaders(name)));
        }
        noticeParams.setHeaders(headers);
        return noticeParams;
    }

    /**
     * 获取授权请求头
     *
     * @return 授权请求头
     */
    private HttpHeader authHeader() {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Authorization", getAccessToken()));
        headers.add(new BasicHeader("PayPal-Request-Id", UUID.randomUUID().toString()));

        return new HttpHeader(headers);
    }


    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @return 对应页面重定向信息
     */
    @Override
    public String toPay(PayOrder order) {
        order.setTransactionType(PayPalTransactionType.CHECKOUT);
        return super.toPay(order);
    }

    private ApplicationContext initUrl(ApplicationContext applicationContext, PayOrder order) {
        String cancelUrl = (String) order.getAttr("cancelUrl");
        if (StringUtils.isEmpty(cancelUrl)) {
            cancelUrl = payConfigStorage.getCancelUrl();
        }

        String returnUrl = (String) order.getAttr("returnUrl");
        if (StringUtils.isEmpty(returnUrl)) {
            returnUrl = payConfigStorage.getReturnUrl();
        }
        applicationContext
                .cancelUrl(cancelUrl)
                .returnUrl(returnUrl);


        return applicationContext;
    }

    private ApplicationContext createApplicationContext(PayOrder order) {
        ApplicationContext applicationContext = new ApplicationContext();
        initUrl(applicationContext, order);
        String brandName = (String) order.getAttr("brandName");
        if (StringUtils.isEmpty(brandName)) {
            applicationContext.setBrandName(brandName);
        }

        String landingPage = (String) order.getAttr("landingPage");
        if (StringUtils.isEmpty(landingPage)) {
            applicationContext.setLandingPage(landingPage);
        }

        String shippingPreference = (String) order.getAttr("shippingPreference");
        if (StringUtils.isEmpty(shippingPreference)) {
            applicationContext.setShippingPreference(shippingPreference);
        }

        String userAction = (String) order.getAttr("userAction");
        if (StringUtils.isEmpty(userAction)) {
            applicationContext.setUserAction(userAction);
        }


        return applicationContext;
    }

    /**
     * 返回创建的订单信息
     * 订单信息与接口地址 https://developer.paypal.com/docs/api/orders/v2
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(PayPalTransactionType.CHECKOUT);
        }
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCheckoutPaymentIntent("CAPTURE");

        orderRequest.setApplicationContext(createApplicationContext(order));

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
        CurType curType = order.getCurType();
        if (null == curType) {
            curType = DefaultCurType.USD;
        }
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .description(order.getSubject())
                .invoiceId((String) order.getAttr("invoiceId"))
                .customId(order.getOutTradeNo())
                .money(new Money()
                        .currencyCode(curType.getType())
                        .value(Util.conversionAmount(order.getPrice()).toString()));

        Object shippingDetail = order.getAttr("shippingDetail");
        if (shippingDetail instanceof ShippingDetail) {
            purchaseUnitRequest.setShippingDetail((ShippingDetail) shippingDetail);
        }
        else {
            ShippingDetail shippingDetail1 = JSON.parseObject(JSON.toJSONString(shippingDetail), ShippingDetail.class);
            purchaseUnitRequest.setShippingDetail(shippingDetail1);
        }

        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.setPurchaseUnits(purchaseUnitRequests);

        HttpStringEntity entity = new HttpStringEntity(JSON.toJSONString(orderRequest), ContentType.APPLICATION_JSON);
        HttpHeader header = authHeader();
        header.addHeader(new BasicHeader("prefer", "return=representation"));

        entity.setHeaders(header);
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(order.getTransactionType()), entity, JSONObject.class);
        if ("created".equalsIgnoreCase(resp.getString("status")) && StringUtils.isNotEmpty(resp.getString("id"))) {
            order.setTradeNo(resp.getString("id"));
        }
        return preOrderHandler(resp, order);
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {

        return PayOutMessage.TEXT().content(code).build();
    }

    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {

        return PayOutMessage.TEXT().content("200").build();
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        if (orderInfo instanceof JSONObject) {
            JSONObject resp = (JSONObject) orderInfo;
            JSONArray links = resp.getJSONArray("links");
            for (int i = 0; i < links.size(); i++) {
                JSONObject link = links.getJSONObject(i);
                if ("approve".equals(link.getString("rel"))) {
                    return String.format("<script type=\"text/javascript\">location.href=\"%s\"</script>", link.getString("href"));
                }
            }
        }
        return "<script type=\"text/javascript\">location.href=\"/\"</script>";
    }

    @Override
    public String getQrPay(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> microPay(PayOrder order) {
        return null;
    }

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.ORDERS_GET), authHeader(), JSONObject.class, tradeNo);
    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        return getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.ORDERS_GET), authHeader(), JSONObject.class, assistOrder.getTradeNo());
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        throw new UnsupportedOperationException("不支持该操作");
    }

    /**
     * 注意：最好在付款成功之后回调时进行调用
     * 确认订单并返回确认后订单信息
     * <b>注意：此方法一个订单只能调用一次, 建议在支付回调时进行调用</b>
     * 这里主要用来获取captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 详情： https://developer.paypal.com/docs/api/orders/v2/#orders_capture
     *
     * @param tradeNo paypal下单成功之后返回的订单号
     * @return 确认后订单信息
     * 获取captureId
     */
    @Override
    public Map<String, Object> ordersCapture(String tradeNo) {
        final HttpHeader header = authHeader();
        header.addHeader(new BasicHeader("Content-Type", "application/json"));
        JSONObject ordersCaptureInfo = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.ORDERS_CAPTURE), header, JSONObject.class, tradeNo);
//        String captureId = ordersCaptureInfo.getJSONArray("purchaseUnits").getJSONObject(0).getJSONObject("payments").getJSONArray("captures").getJSONObject(0).getString("id");
        return ordersCaptureInfo;
    }

    /**
     * 确认订单之后获取订单信息
     * 详情： https://developer.paypal.com/docs/api/payments/v2/#captures_get
     *
     * @param captureId 确认付款订单之后生成的id
     * @return 确认付款订单详情
     * <pre>
     *     {
     *   "id": "2GG279541U471931P",
     *   "status": "COMPLETED",
     *   "status_details": {},
     *   "amount": {
     *     "total": "10.99",
     *     "currency": "USD"
     *   },
     *   "final_capture": true,
     *   "seller_protection": {
     *     "status": "ELIGIBLE",
     *     "dispute_categories": [
     *       "ITEM_NOT_RECEIVED",
     *       "UNAUTHORIZED_TRANSACTION"
     *     ]
     *   },
     *   "seller_receivable_breakdown": {
     *     "gross_amount": {
     *       "total": "10.99",
     *       "currency": "USD"
     *     },
     *     "paypal_fee": {
     *       "value": "0.33",
     *       "currency": "USD"
     *     },
     *     "net_amount": {
     *       "value": "10.66",
     *       "currency": "USD"
     *     },
     *     "receivable_amount": {
     *       "currency_code": "CNY",
     *       "value": "59.26"
     *     },
     *     "paypal_fee_in_receivable_currency": {
     *       "currency_code": "CNY",
     *       "value": "1.13"
     *     },
     *     "exchange_rate": {
     *       "source_currency": "USD",
     *       "target_currency": "CNY",
     *       "value": "5.9483297432325"
     *     }
     *   },
     *   "invoice_id": "INVOICE-123",
     *   "create_time": "2017-09-11T23:24:01Z",
     *   "update_time": "2017-09-11T23:24:01Z",
     *   "links": [
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/captures/2GG279541U471931P",
     *       "rel": "self",
     *       "method": "GET"
     *     },
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/captures/2GG279541U471931P/refund",
     *       "rel": "refund",
     *       "method": "POST"
     *     },
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/authorizations/0VF52814937998046",
     *       "rel": "up",
     *       "method": "GET"
     *     }
     *   ]
     * }
     *
     * </pre>
     */
    @Override
    public Map<String, Object> getCapture(String captureId) {
        JSONObject ordersCaptureInfo = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.GET_CAPTURE), authHeader(), JSONObject.class, captureId);
        return ordersCaptureInfo;
    }

    /**
     * 申请退款接口
     * 通过captureId发起退款  详情： https://developer.paypal.com/docs/api/payments/v2/#captures_refund
     * captureId 详情{@link #ordersCapture(String)}
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {
        JSONObject request = new JSONObject();
        Money amount = new Money();
        if (null == refundOrder.getCurType()) {
            refundOrder.setCurType(DefaultCurType.USD);
        }
        amount.setCurrencyCode(refundOrder.getCurType().getType());
        amount.value(Util.conversionAmount(refundOrder.getRefundAmount()).toString());
        request.put("amount", amount);
        request.put("note_to_payer", refundOrder.getDescription());
        request.put("invoiceId", refundOrder.getOutTradeNo());

        HttpStringEntity httpEntity = new HttpStringEntity(request.toJSONString(), ContentType.APPLICATION_JSON);
        httpEntity.setHeaders(authHeader());
        //TODO: 这里TradeNo为{@link #ordersCapture} 确认订单之后的captureId
        String captureId = refundOrder.getTradeNo();
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.REFUND), httpEntity, JSONObject.class, captureId);
        PayPalRefundResult payPalRefundResult = new PayPalRefundResult(resp, refundOrder.getTradeNo());
        refundOrder.setRefundNo(payPalRefundResult.getRefundNo());
        return payPalRefundResult;
    }

    /**
     * 查询退款
     * 通过退款id获取退款信息 详情：https://developer.paypal.com/docs/api/payments/v2/#refunds
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        JSONObject resp = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.REFUND_GET), authHeader(), JSONObject.class, refundOrder.getRefundNo());
        return resp;
    }

    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {
        return Collections.emptyMap();
    }


}
