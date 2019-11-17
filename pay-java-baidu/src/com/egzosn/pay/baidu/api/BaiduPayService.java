package com.egzosn.pay.baidu.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.baidu.bean.BaiduPayOrder;
import com.egzosn.pay.baidu.bean.BaiduRefundOrder;
import com.egzosn.pay.baidu.bean.BaiduTransactionType;
import com.egzosn.pay.baidu.bean.type.AuditStatus;
import com.egzosn.pay.baidu.util.Asserts;
import com.egzosn.pay.baidu.util.NoNullMap;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BaiduPayService extends BasePayService<BaiduPayConfigStorage> {
    public static final String APP_KEY = "appKey";
    public static final String APP_ID = "appId";
    public static final String DEAL_ID = "dealId";
    public static final String TP_ORDER_ID = "tpOrderId";
    public static final String DEAL_TITLE = "dealTitle";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String SIGN_FIELDS_RANGE = "signFieldsRange";
    public static final String BIZ_INFO = "bizInfo";
    public static final String RSA_SIGN = "rsaSign";
    public static final String ORDER_ID = "orderId";
    public static final String USER_ID = "userId";
    public static final String SITE_ID = "siteId";
    public static final String METHOD = "method";
    public static final String TYPE = "type";
    
    public static final Integer RESPONSE_SUCCESS = 2;
    public static final String RESPONSE_STATUS = "status";
    
    
    public BaiduPayService(BaiduPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }
    
    public BaiduPayService(BaiduPayConfigStorage payConfigStorage,
                           HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }
    
    @Override
    public boolean verify(Map<String, Object> params) {
        if (!RESPONSE_SUCCESS.equals(params.get(RESPONSE_STATUS))) {
            return false;
        }
        return signVerify(params, String.valueOf(params.get(RSA_SIGN))) && verifySource(String.valueOf(params.get(TP_ORDER_ID)));
    }
    
    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        String keyPrivate = payConfigStorage.getKeyPrivate();
        String rsaSign = String.valueOf(params.get(RSA_SIGN));
        String targetRsaSign = getRsaSign(params, keyPrivate, RSA_SIGN);
        LOG.debug("百度返回的签名: " + rsaSign + " 本地产生的签名: " + targetRsaSign);
        return StringUtils.equals(rsaSign, targetRsaSign);
    }
    
    @Override
    public boolean verifySource(String id) {
        return true;
    }
    
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        if (!(order instanceof BaiduPayOrder)) {
            throw new UnsupportedOperationException("请使用 " + BaiduPayOrder.class.getName());
        }
        NoNullMap<String, Object> params = getUseOrderInfoParams(order);
        String rsaSign = getRsaSignUserOrderInfo(params, payConfigStorage.getKeyPrivate());
        params.putIfNoNull(RSA_SIGN, rsaSign);
        return params;
    }
    
    /**
     * 获取"查询支付状态"所需参数
     *
     * @return
     */
    public NoNullMap<String, Object> getUseQueryPay() {
        String appKey = payConfigStorage.getAppKey();
        NoNullMap<String, Object> result = new NoNullMap<>();
        result.putIfNoNull(APP_KEY, appKey)
                .putIfNoNull(APP_ID, payConfigStorage.getAppid());
        return result;
    }
    
    /**
     * 获取"创建订单"所需参数
     *
     * @param order
     * @return
     */
    private NoNullMap<String, Object> getUseOrderInfoParams(PayOrder order) {
        BaiduPayOrder payOrder = (BaiduPayOrder) order;
        NoNullMap<String, Object> result = new NoNullMap<>();
        String appKey = payConfigStorage.getAppKey();
        String dealId = payConfigStorage.getDealId();
        result.putIfNoNull(APP_KEY, appKey)
                .putIfNoNull(TP_ORDER_ID, payOrder.getTradeNo())
                .putIfNoNull(DEAL_ID, dealId)
                .putIfNoNull(DEAL_TITLE, payOrder.getSubject())
                .putIfNoNull(SIGN_FIELDS_RANGE, payOrder.getSignFieldsRange())
                .putIfNoNull(BIZ_INFO, JSON.toJSONString(payOrder.getBizInfo()))
                .putIfNoNull(TOTAL_AMOUNT, String.valueOf(order.getPrice()));
        return result;
    }
    
    @Override
    @Deprecated
    public PayOutMessage getPayOutMessage(String code, String message) {
        throw new UnsupportedOperationException("请使用 " + getClass().getName() + "#getPayOutMessageUseBaidu");
    }
    
    /**
     * 请求业务方退款审核/响应处理
     * http://smartprogram.baidu.com/docs/develop/function/tune_up_examine/
     *
     * @param errno
     * @param message
     * @param auditStatus
     * @param refundPayMoney
     * @return
     */
    public PayOutMessage getApplyRefundOutMessageUseBaidu(Integer errno,
                                                          String message,
                                                          AuditStatus auditStatus,
                                                          BigDecimal refundPayMoney) {
        JSONObject data = new JSONObject();
        data.put("auditStatus", auditStatus.getCode());
        JSONObject calculateRes = new JSONObject();
        calculateRes.put("refundPayMoney", refundPayMoney);
        data.put("calculateRes", calculateRes);
        return PayOutMessage.JSON()
                .content("errno", errno)
                .content("message", message)
                .content("data", data)
                .build();
        
    }
    
    /**
     * 通知退款状态/响应处理
     * http://smartprogram.baidu.com/docs/develop/function/tune_up_drawback/
     *
     * @param errno
     * @param message
     * @return
     */
    public PayOutMessage getRefundOutMessageUseBaidu(Integer errno,
                                                     String message) {
        return PayOutMessage.JSON()
                .content("errno", errno)
                .content("message", message)
                .content("data", "{}")
                .build();
        
    }
    
    /**
     * 支付通知/响应处理
     *
     * @param errno
     * @param message
     * @param isConsumed
     * @param isErrorOrder
     * @return
     */
    public PayOutMessage getPayOutMessageUseBaidu(Integer errno,
                                                  String message,
                                                  Integer isConsumed,
                                                  Integer isErrorOrder) {
        Asserts.isNoNull(errno, "errno 是必填的");
        Asserts.isNoNull(message, "message 是必填的");
        Asserts.isNoNull(isConsumed, "isConsumed 是必填的");
        JSONObject data = new JSONObject();
        data.put("isConsumed", isConsumed);
        if (isErrorOrder != null) {
            data.put("isErrorOrder", isErrorOrder);
        }
        return PayOutMessage.JSON()
                .content("errno", errno)
                .content("message", message)
                .content("data", data)
                .build();
    }
    
    /**
     * 支付通知/响应处理
     * http://smartprogram.baidu.com/docs/develop/function/tune_up_notice/
     *
     * @param code
     * @param message
     * @param isConsumed
     * @return
     */
    public PayOutMessage getPayOutMessageUseBaidu(Integer code,
                                                  String message,
                                                  Integer isConsumed) {
        return getPayOutMessageUseBaidu(code, message, isConsumed, null);
    }
    
    /**
     * 支付通知/响应处理
     * http://smartprogram.baidu.com/docs/develop/function/tune_up_notice/
     *
     * @param payMessage 支付回调消息
     * @return
     */
    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return getPayOutMessageUseBaidu(0, "success", 2);
    }
    
    @Override
    public String buildRequest(Map<String, Object> orderInfo,
                               MethodType method) {
        throw new UnsupportedOperationException("百度不支持PC支付");
    }
    
    @Override
    public String getQrPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持扫码付");
    }
    
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持刷卡付");
    }
    
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return secondaryInterface(tradeNo, outTradeNo, BaiduTransactionType.PAY_QUERY);
    }
    
    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        throw new UnsupportedOperationException("不支持该操作");
    }
    
    @Override
    @Deprecated
    public Map<String, Object> refund(String orderId,
                                      String userId,
                                      BigDecimal refundAmount,
                                      BigDecimal totalAmount) {
        throw new UnsupportedOperationException("请使用 " + getClass().getName() + "#refundUseBaidu");
    }
    
    public Map<String, Object> refundUseBaidu(Long orderId,
                                              Long userId,
                                              Integer refundType,
                                              String tpOrderId,
                                              String refundReason) {
        return refundUseBaidu(new BaiduRefundOrder(orderId, userId, refundType, refundReason, tpOrderId));
    }
    
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        throw new UnsupportedOperationException("请使用 " + getClass().getName() + "#refundUseBaidu");
    }
    
    public Map<String, Object> refundUseBaidu(BaiduRefundOrder refundOrder) {
        NoNullMap<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.APPLY_REFUND;
        parameters.putIfNoNull(METHOD, transactionType.getMethod())
                .putIfNoNull(ORDER_ID, refundOrder.getTradeNo())
                .putIfNoNull(USER_ID, refundOrder.getUserId())
                .putIfNoNull("refundType", refundOrder.getRefundType())
                .putIfNoNull("refundReason", String.valueOf(refundOrder.getRefundReason()))
                .putIfNoNull(TP_ORDER_ID, refundOrder.getTpOrderId())
                .putIfNoNull("applyRefundMoney", refundOrder.getApplyRefundMoney())
                .putIfNoNull("bizRefundBatchId", refundOrder.getBizRefundBatchId())
                .putIfNoNull(APP_KEY, payConfigStorage.getAppKey())
                .putIfNoNull(RSA_SIGN, getRsaSign(parameters, payConfigStorage.getKeyPrivate()));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * @param orderId 百度平台订单ID
     * @param userId  百度用户ID
     * @return
     */
    @Override
    public Map<String, Object> refundquery(String orderId,
                                           String userId) {
        NoNullMap<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.REFUND_QUERY;
        parameters.putIfNoNull(METHOD, transactionType.getMethod())
                .putIfNoNull(TYPE, 3)
                .putIfNoNull(ORDER_ID, orderId)
                .putIfNoNull(USER_ID, userId)
                .putIfNoNull(APP_KEY, payConfigStorage.getAppKey())
                .putIfNoNull(RSA_SIGN, getRsaSign(parameters, payConfigStorage.getKeyPrivate()));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return refundquery(refundOrder.getTradeNo(), refundOrder.getOutTradeNo());
    }
    
    @Override
    public Map<String, Object> downloadbill(Date billDate, String access_token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    public Map<String, Object> downloadOrderBill(Date billDate, String access_token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_ORDER_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    @Override
    public Map<String, Object> secondaryInterface(Object orderId,
                                                  String siteId,
                                                  TransactionType transactionType) {
        if (!BaiduTransactionType.PAY_QUERY.equals(transactionType)) {
            throw new UnsupportedOperationException("不支持该操作");
        }
        
        NoNullMap<String, Object> parameters = getUseQueryPay();
        parameters.putIfNoNull(ORDER_ID, orderId)
                .putIfNoNull(SITE_ID, siteId)
                .putIfNoNull("sign", getRsaSignUsePayQuery(parameters, payConfigStorage.getKeyPrivate()));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return ((BaiduTransactionType) transactionType).getUrl();
    }
    
    private String getRsaSignUserOrderInfo(Map<String, Object> params, String privateKey) {
        Map<String, String> signParams = new HashMap<>();
        signParams.put(APP_KEY, String.valueOf(params.get(APP_KEY)));
        signParams.put(DEAL_ID, String.valueOf(params.get(DEAL_ID)));
        signParams.put(TP_ORDER_ID, String.valueOf(params.get(TP_ORDER_ID)));
        signParams.put(TOTAL_AMOUNT, String.valueOf(params.get(TOTAL_AMOUNT)));
        if (signParams.containsValue(null)) {
            throw new IllegalArgumentException("参数 " + signParams.keySet().toString() + " 均为必填");
        }
        
        return SignUtils.RSA.sign(params, privateKey, "UTF-8");
    }
    
    /**
     * "支付状态查询" 使用的签名
     *
     * @param params
     * @param privateKey
     * @return
     */
    private String getRsaSignUsePayQuery(Map<String, Object> params, String privateKey) {
        Map<String, String> signParams = new HashMap<>();
        signParams.put(APP_KEY, String.valueOf(params.get(APP_KEY)));
        signParams.put(APP_ID, String.valueOf(params.get(APP_ID)));
        signParams.put(ORDER_ID, String.valueOf(params.get(ORDER_ID)));
        signParams.put(SITE_ID, String.valueOf(params.get(SITE_ID)));
        if (signParams.containsValue(null)) {
            throw new IllegalArgumentException("参数 " + signParams.keySet().toString() + " 均为必填");
        }
        
        return SignUtils.RSA.sign(params, privateKey, "UTF-8");
    }
    
    private String getRsaSign(Map<String, Object> params, String privateKey) {
        Map<String, String> signParams = new HashMap<>();
        signParams.put(APP_KEY, String.valueOf(params.get(APP_KEY)));
        signParams.put(USER_ID, String.valueOf(params.get(USER_ID)));
        signParams.put(ORDER_ID, String.valueOf(params.get(ORDER_ID)));
        if (signParams.containsValue(null)) {
            throw new IllegalArgumentException("参数 " + signParams.keySet().toString() + " 均为必填");
        }
        
        return SignUtils.RSA.sign(params, privateKey, "UTF-8");
    }
    
    private String getRsaSign(Map<String, Object> params, String... ignoreKeys) {
        return SignUtils.RSA.createSign(SignUtils.parameterText(params, "&", ignoreKeys), payConfigStorage.getKeyPrivate(), "UTF-8");
    }
}
