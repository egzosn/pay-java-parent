package com.egzosn.pay.paypal.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpHeader;
import com.egzosn.pay.common.http.HttpStringEntity;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.paypal.bean.PayPalTransactionType;
import com.egzosn.pay.paypal.bean.order.*;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 贝宝支付配置存储
 * @author  egan
 *
 * email egzosn@gmail.com
 * date 2018-4-8 ‏‎22:15:09
 */
public class PayPalPayService extends BasePayService<PayPalConfigStorage>{

    /**
     * 沙箱环境
     */
    private static final String SANDBOX_REQ_URL = "https://api.sandbox.paypal.com/v1/";
    /**
     * 正式测试环境
     */
    private static final String REQ_URL = "https://api.paypal.com/v1/";

    /**
     * 获取对应的请求地址
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType transactionType){
        return (payConfigStorage.isTest() ? SANDBOX_REQ_URL : REQ_URL) + transactionType.getMethod();
    }


    public PayPalPayService(PayPalConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }



    /**
     * 获取请求token
     * @return 授权令牌
     */
    public String getAccessToken()  {
        try {
            return getAccessToken(false);
        } catch (PayErrorException e) {
            throw e;
        }
    }

    /**
     *  获取授权令牌
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
                        payConfigStorage.updateAccessToken(String.format("%s %s", resp.getString("token_type" ), resp.getString("access_token" )),  resp.getIntValue("expires_in" ));

                    } catch (UnsupportedEncodingException e) {
                        throw new PayErrorException(new PayException("failure", e.getMessage()));
                    }
                return payConfigStorage.getAccessToken();
            }
        } finally {
            lock.unlock();
        }
        return payConfigStorage.getAccessToken();
    }


    @Override
    public boolean verify(Map<String, Object> params) {

        HttpStringEntity httpEntity = new HttpStringEntity("{\"payer_id\":\""+(String)params.get("PayerID")+"\"}", ContentType.APPLICATION_JSON);
        httpEntity.setHeaders(authHeader());
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.EXECUTE), httpEntity, JSONObject.class, (String) params.get("paymentId"));
        return  "approved".equals(resp.getString("state"));

    }

    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        return true;
    }

    @Override
    public boolean verifySource(String id) {
        return true;
    }

    /**
     * 获取授权请求头
     * @return 授权请求头
     */
    private HttpHeader authHeader(){

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Authorization", getAccessToken()));
        headers.add(new BasicHeader("PayPal-Request-Id", UUID.randomUUID().toString()));

        return new HttpHeader(headers);
    }
    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        Amount amount = new Amount();
        if (null == order.getCurType()){
            order.setCurType(DefaultCurType.USD);
        }
        amount.setCurrency(order.getCurType().getType());
        amount.setTotal(Util.conversionAmount(order.getPrice()).toString());

        Transaction transaction = new Transaction();
        if (!StringUtils.isEmpty(order.getSubject())){
            transaction.setDescription(order.getSubject());
        }else {
            transaction.setDescription(order.getBody());
        }
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent(order.getTransactionType().getType());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        //取消按钮转跳地址
        redirectUrls.setCancelUrl(payConfigStorage.getNotifyUrl());
        //发起付款后的页面转跳地址
        redirectUrls.setReturnUrl(payConfigStorage.getReturnUrl());
        payment.setRedirectUrls(redirectUrls);
        HttpStringEntity entity = new HttpStringEntity(JSON.toJSONString(payment),  ContentType.APPLICATION_JSON);
        entity.setHeaders(authHeader());
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(order.getTransactionType()), entity, JSONObject.class);
        if ("created".equals(resp.getString("state")) && StringUtils.isNotEmpty(resp.getString("id"))){
            order.setOutTradeNo(resp.getString("id"));
        }
        return preOrderHandler(resp, order);
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.TEXT().content(code).build();
    }

    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return PayOutMessage.TEXT().content("success").build();
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        if (orderInfo instanceof  JSONObject){
            Payment payment = ((JSONObject) orderInfo).toJavaObject(Payment.class);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return String.format("<script type=\"text/javascript\">location.href=\"%s\"</script>",links.getHref() );
                }
            }
        }
     return "<script type=\"text/javascript\">location.href=\"/\"</script>" ;
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
        JSONObject resp = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.ORDERS), authHeader(), JSONObject.class, tradeNo);
        return resp;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }
    /**
     * 申请退款接口
     * 废弃
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param refundAmount 退款金额
     * @param totalAmount 总金额
     * @return 返回支付方申请退款后的结果
     * @see #refund(RefundOrder)
     * @deprecated {@link #refund(RefundOrder)}
     */
    @Deprecated
    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        return refund(new RefundOrder( tradeNo,  outTradeNo,  refundAmount,  totalAmount));
    }


    /**
     * 申请退款接口
     *
     * @param refundOrder   退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        JSONObject request =  new JSONObject();

        if (null != refundOrder.getRefundAmount() && BigDecimal.ZERO.compareTo( refundOrder.getRefundAmount()) == -1){
            Amount amount = new Amount();
            if(null == refundOrder.getCurType()){
                refundOrder.setCurType(DefaultCurType.USD);
            }

            amount.setCurrency(refundOrder.getCurType().getType());
            amount.setTotal(Util.conversionAmount(refundOrder.getRefundAmount()).toString());
            request.put("amount", amount);
            request.put("description", refundOrder.getDescription());
        }

        HttpStringEntity httpEntity = new HttpStringEntity(request.toJSONString(), ContentType.APPLICATION_JSON);
        httpEntity.setHeaders(authHeader());
        JSONObject resp = getHttpRequestTemplate().postForObject(getReqUrl(PayPalTransactionType.REFUND),  httpEntity, JSONObject.class, refundOrder.getTradeNo());
        return resp;
    }
    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        JSONObject resp = getHttpRequestTemplate().getForObject(getReqUrl(PayPalTransactionType.REFUND_QUERY), authHeader(), JSONObject.class, tradeNo);
        return resp;
    }

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return refundquery(refundOrder.getTradeNo(), refundOrder.getOutTradeNo());
    }

    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Object> secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType) {
        return Collections.emptyMap();
    }


}
