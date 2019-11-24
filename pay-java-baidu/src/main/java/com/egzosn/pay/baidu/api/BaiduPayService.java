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


public class BaiduPayService extends BasePayService<com.egzosn.pay.baidu.api.BaiduPayConfigStorage> {
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
    public static final String SIGN = "sign";
    public static final String METHOD = "method";
    public static final String TYPE = "type";
    
    public static final Integer RESPONSE_SUCCESS = 2;
    public static final String RESPONSE_STATUS = "status";
    
    
    public BaiduPayService(com.egzosn.pay.baidu.api.BaiduPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }
    
    public BaiduPayService(com.egzosn.pay.baidu.api.BaiduPayConfigStorage payConfigStorage,
                           HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }
    
    /**
     * 验证响应
     *
     * @param params 回调回来的参数集
     * @return
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        if (!RESPONSE_SUCCESS.equals(params.get(RESPONSE_STATUS))) {
            return false;
        }
        return signVerify(params, String.valueOf(params.get(RSA_SIGN))) && verifySource(String.valueOf(params.get(TP_ORDER_ID)));
    }
    
    /**
     * 验证签名
     *
     * @param params 参数集
     * @param sign   签名原文
     * @return
     */
    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        String rsaSign = String.valueOf(params.get(RSA_SIGN));
        String targetRsaSign = getRsaSign(params, RSA_SIGN);
        LOG.debug("百度返回的签名: " + rsaSign + " 本地产生的签名: " + targetRsaSign);
        return StringUtils.equals(rsaSign, targetRsaSign);
    }
    
    @Override
    public boolean verifySource(String id) {
        return true;
    }
    
    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        if (!(order instanceof BaiduPayOrder)) {
            throw new UnsupportedOperationException("请使用 " + BaiduPayOrder.class.getName());
        }
        NoNullMap<String, Object> params = getUseOrderInfoParams(order);
        String rsaSign = getRsaSign(params, RSA_SIGN);
        params.putIfNoNull(RSA_SIGN, rsaSign);
        return params;
    }
    
    /**
     * 获取"查询支付状态"所需参数
     *
     * @return
     */
    public Map<String, Object> getUseQueryPay() {
        String appKey = payConfigStorage.getAppKey();
        Map<String, Object> result = new HashMap<>();
        result.put(APP_KEY, appKey);
        result.put(APP_ID, payConfigStorage.getAppid());
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
    
    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return
     */
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
    
    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return
     */
    @Override
    @Deprecated
    public String buildRequest(Map<String, Object> orderInfo,
                               MethodType method) {
        throw new UnsupportedOperationException("百度不支持PC支付");
    }
    
    /**
     * 百度不支持扫码付
     *
     * @param order 发起支付的订单信息
     * @return
     */
    @Override
    @Deprecated
    public String getQrPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持扫码付");
    }
    
    /**
     * 百度不支持刷卡付
     *
     * @param order 发起支付的订单信息
     * @return
     */
    @Override
    @Deprecated
    public Map<String, Object> microPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持刷卡付");
    }
    
    /**
     * 查询订单
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return secondaryInterface(tradeNo, outTradeNo, BaiduTransactionType.PAY_QUERY);
    }
    
    /**
     * 百度不支持该操作
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    @Override
    @Deprecated
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        throw new UnsupportedOperationException("不支持该操作");
    }
    
    /**
     * 退款, 请使用 {@link com.egzosn.pay.baidu.api.BaiduPayService#refundUseBaidu}
     *
     * @param orderId
     * @param userId
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @return
     */
    @Override
    @Deprecated
    public Map<String, Object> refund(String orderId,
                                      String userId,
                                      BigDecimal refundAmount,
                                      BigDecimal totalAmount) {
        throw new UnsupportedOperationException("请使用 " + getClass().getName() + "#refundUseBaidu");
    }
    
    /**
     * 退款
     *
     * @param orderId
     * @param userId
     * @param refundType
     * @param tpOrderId
     * @param refundReason
     * @return
     */
    public Map<String, Object> refundUseBaidu(Long orderId,
                                              Long userId,
                                              Integer refundType,
                                              String tpOrderId,
                                              String refundReason) {
        return refundUseBaidu(new BaiduRefundOrder(orderId, userId, refundType, refundReason, tpOrderId));
    }
    
    /**
     * 退款, 请使用 {@link com.egzosn.pay.baidu.api.BaiduPayService#refundUseBaidu}
     *
     * @param refundOrder 退款订单信息
     * @return
     */
    @Override
    @Deprecated
    public Map<String, Object> refund(RefundOrder refundOrder) {
        throw new UnsupportedOperationException("请使用 " + getClass().getName() + "#refundUseBaidu");
    }
    
    /**
     * 退款, 请使用 {@link com.egzosn.pay.baidu.api.BaiduPayService#refundUseBaidu}
     *
     * @param refundOrder
     * @return
     */
    public Map<String, Object> refundUseBaidu(BaiduRefundOrder refundOrder) {
        Map<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.APPLY_REFUND;
        parameters.put(METHOD, transactionType.getMethod());
        parameters.put(ORDER_ID, refundOrder.getTradeNo());
        parameters.put(USER_ID, refundOrder.getUserId());
        parameters.put("refundType", refundOrder.getRefundType());
        parameters.put("refundReason", String.valueOf(refundOrder.getRefundReason()));
        parameters.put(TP_ORDER_ID, refundOrder.getTpOrderId());
        parameters.put("applyRefundMoney", refundOrder.getApplyRefundMoney());
        parameters.put("bizRefundBatchId", refundOrder.getBizRefundBatchId());
        parameters.put(APP_KEY, payConfigStorage.getAppKey());
        parameters.put(RSA_SIGN, getRsaSign(parameters, RSA_SIGN));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * 退费查询
     *
     * @param orderId 百度平台订单ID
     * @param userId  百度用户ID
     * @return
     */
    @Override
    public Map<String, Object> refundquery(String orderId,
                                           String userId) {
        Map<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.REFUND_QUERY;
        parameters.put(METHOD, transactionType.getMethod());
        parameters.put(TYPE, 3);
        parameters.put(ORDER_ID, orderId);
        parameters.put(USER_ID, userId);
        parameters.put(APP_KEY, payConfigStorage.getAppKey());
        parameters.put(RSA_SIGN, getRsaSign(parameters, RSA_SIGN));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * 退费查询
     *
     * @param refundOrder 退款订单单号信息
     * @return
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return refundquery(refundOrder.getTradeNo(), refundOrder.getOutTradeNo());
    }
    
    /**
     * 下载资金账单
     *
     * @param billDate     账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param access_token
     * @return
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String access_token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * 下载订单对账单
     *
     * @param billDate
     * @param access_token
     * @return
     */
    public Map<String, Object> downloadOrderBill(Date billDate, String access_token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_ORDER_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * 通用查询接口
     *
     * @param orderId
     * @param siteId
     * @param transactionType 交易类型
     * @return
     */
    @Override
    public Map<String, Object> secondaryInterface(Object orderId,
                                                  String siteId,
                                                  TransactionType transactionType) {
        if (!BaiduTransactionType.PAY_QUERY.equals(transactionType)) {
            throw new UnsupportedOperationException("不支持该操作");
        }
        
        Map<String, Object> parameters = getUseQueryPay();
        parameters.put(ORDER_ID, orderId);
        parameters.put(SITE_ID, siteId);
        parameters.put(SIGN, getRsaSign(parameters, SIGN));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }
    
    /**
     * 获取支付请求地址
     *
     * @param transactionType 交易类型
     * @return
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return ((BaiduTransactionType) transactionType).getUrl();
    }
    
    /**
     * 签名
     *
     * @param params
     * @param ignoreKeys
     * @return
     */
    private String getRsaSign(Map<String, Object> params, String... ignoreKeys) {
        String waitSignVal = SignUtils.parameterText(params, "&", false, ignoreKeys);
        return SignUtils.RSA.createSign(waitSignVal, payConfigStorage.getKeyPrivate(), "UTF-8");
    }
}
