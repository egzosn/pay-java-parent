package com.egzosn.pay.paypal.v2.api;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.MethodType;
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
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.v2.bean.PayPalRefundResult;
import com.egzosn.pay.paypal.v2.bean.PayPalTransactionType;
import com.egzosn.pay.paypal.v2.bean.order.ApplicationContext;
import com.egzosn.pay.paypal.v2.bean.order.Money;
import com.egzosn.pay.paypal.v2.bean.order.OrderRequest;
import com.egzosn.pay.paypal.v2.bean.order.PurchaseUnitRequest;
import com.egzosn.pay.paypal.v2.bean.order.ShippingDetail;


/**
 * 贝宝支付配置存储
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2021-1-16 ‏‎22:15:09
 */
public class PayPalPayService extends BasePayService<PayPalConfigStorage> {

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
                header.put("Authorization", "Basic " + authorizationString(getPayConfigStorage().getAppid(), getPayConfigStorage().getKeyPrivate()));
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
     * @return
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        Object paymentStatus = params.get("payment_status");
        if (!"Completed".equals(paymentStatus)) {
            LOG.warn("状态未完成:" + paymentStatus);
            return false;
        }
        String resp = getHttpRequestTemplate().getForObject(getNotifyReqUrl(params), authHeader(), String.class);
        return "VERIFIED".equals(resp);

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
        if ("created".equals(resp.getString("state")) && StringUtils.isNotEmpty(resp.getString("id"))) {
            order.setTradeNo(resp.getString("id"));
        }
        return preOrderHandler(resp, order);
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        String out = "The response from IPN was: <b>" + code + "</b>";
        return PayOutMessage.TEXT().content(out).build();
    }

    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        Map<String, Object> message = payMessage.getPayMessage();
        return new PayPalOutMessageBuilder(message).build();
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
        JSONObject resp = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.ORDERS_GET), authHeader(), JSONObject.class, tradeNo);
        return resp;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }


    /**
     * 申请退款接口
     *
     * 1.需要通过支付单号获取captureId 详情： https://developer.paypal.com/docs/api/payments/v2/#captures
     * 2.通过captureId发起退款  详情： https://developer.paypal.com/docs/api/payments/v2/#captures_refund
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {

        JSONObject ordersCaptureInfo = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.ORDERS_CAPTURE), authHeader(), JSONObject.class, refundOrder.getTradeNo());
        if (!"COMPLETED".equals(ordersCaptureInfo.getString("status"))) {
            return new PayPalRefundResult(ordersCaptureInfo, refundOrder.getTradeNo());
        }

        String captureId = ordersCaptureInfo.getJSONArray("purchaseUnits").getJSONObject(0).getJSONObject("payments").getJSONArray("captures").getJSONObject(0).getString("id");
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
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.REFUND), httpEntity, JSONObject.class, captureId);
        PayPalRefundResult payPalRefundResult = new PayPalRefundResult(resp, refundOrder.getTradeNo());
        refundOrder.setRefundNo(payPalRefundResult.getRefundNo());
        return payPalRefundResult;
    }

    /**
     * 查询退款
     * 通过退款id获取退款信息 详情：https://developer.paypal.com/docs/api/payments/v2/#refunds
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        JSONObject resp = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.REFUND_GET), authHeader(), JSONObject.class, refundOrder.getRefundNo());
        return resp;
    }

    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return Collections.emptyMap();
    }


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调的请求参数
     */
    @Override
    public Map<String, Object> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {

        Map<String, Object> params = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0, len = values.length; i < len; i++) {
                valueStr += (i == len - 1) ? values[i] : values[i] + ",";
            }
            if (StringUtils.isNotEmpty(payConfigStorage.getInputCharset()) && !valueStr.matches("\\w+")) {
                try {
                    if (valueStr.equals(new String(valueStr.getBytes("iso8859-1"), "iso8859-1"))) {
                        valueStr = new String(valueStr.getBytes("iso8859-1"), payConfigStorage.getInputCharset());
                    }
                }
                catch (UnsupportedEncodingException e) {
                    LOG.error(e);
                }
            }
            params.put(name, valueStr);
        }
        return params;
    }


}
