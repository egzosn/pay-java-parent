package com.egzosn.pay.baidu.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.baidu.bean.BaiduPayOrder;
import com.egzosn.pay.baidu.bean.BaiduRefundOrder;
import com.egzosn.pay.baidu.bean.BaiduTransactionType;
import com.egzosn.pay.baidu.bean.type.AuditStatus;
import com.egzosn.pay.baidu.util.Asserts;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.Util;
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
    public static final String SIGN = "sign";
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

    /**
     * 验证响应
     *
     * @param params 回调回来的参数集
     * @return 结果
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
     * @return 结果
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
     * @return 结果
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        Map<String, Object> params = getUseOrderInfoParams(order);
        String rsaSign = getRsaSign(params, RSA_SIGN);
        params.put(RSA_SIGN, rsaSign);
        return params;
    }

    /**
     * 获取"查询支付状态"所需参数
     *
     * @return 结果
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
     * @param order 订单信息
     * @return 结果
     */
    private Map<String, Object> getUseOrderInfoParams(PayOrder order) {
        BaiduPayOrder payOrder = (BaiduPayOrder) order;
        Map<String, Object> result = new HashMap<>();
        String appKey = payConfigStorage.getAppKey();
        String dealId = payConfigStorage.getDealId();
        result.put(APP_KEY, appKey);
        result.put(TP_ORDER_ID, payOrder.getTradeNo());
        result.put(DEAL_ID, dealId);
        result.put(DEAL_TITLE, payOrder.getSubject());
        result.put(SIGN_FIELDS_RANGE, payOrder.getSignFieldsRange());
        result.put(BIZ_INFO, JSON.toJSONString(payOrder.getBizInfo()));
        result.put(TOTAL_AMOUNT, String.valueOf(Util.conversionAmount(order.getPrice())));

        return result;
    }

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 结果
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
     * @param errno          错误代码
     * @param message        消息
     * @param auditStatus    状态
     * @param refundPayMoney 退款金额
     * @return 结果
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
     * @param errno   错误代码
     * @param message 消息
     * @return 结果
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
     * @param errno        错误代码
     * @param message      消息
     * @param isConsumed   是否消费
     * @param isErrorOrder 错误订单
     * @return 结果
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
     * @param code       状态码
     * @param message    消息
     * @param isConsumed 是否消费
     * @return 结果
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
     * @return 结果
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
     * @return 结果
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo,
                               MethodType method) {
        throw new UnsupportedOperationException("百度不支持PC支付");
    }


    /**
     * 百度不支持扫码付
     *
     * @param order 发起支付的订单信息
     * @return 结果
     */
    @Override
    public String getQrPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持扫码付");
    }

    /**
     * 百度不支持刷卡付
     *
     * @param order 发起支付的订单信息
     * @return 结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        throw new UnsupportedOperationException("百度不支持刷卡付");
    }

    /**
     * 查询订单
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 结果
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
     * @return 结果
     */
    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        throw new UnsupportedOperationException("不支持该操作");
    }


    /**
     * 退款
     *
     * @param refundOrder 退款订单信息
     * @return 退款结果
     */
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        Map<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.APPLY_REFUND;
        parameters.put(METHOD, transactionType.getMethod());
        parameters.put(ORDER_ID, refundOrder.getOutTradeNo());
        parameters.put(USER_ID, refundOrder.getUserId());
        setParameters(parameters, "refundType", refundOrder);
        parameters.put("refundReason", refundOrder.getDescription());
        parameters.put(TP_ORDER_ID, refundOrder.getTradeNo());
        parameters.put("applyRefundMoney", refundOrder.getRefundAmount());
        parameters.put("bizRefundBatchId", refundOrder.getRefundNo());
        parameters.put(APP_KEY, payConfigStorage.getAppKey());
        parameters.put(RSA_SIGN, getRsaSign(parameters, RSA_SIGN));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);

    }


    /**
     * 退费查询
     *
     * @param refundOrder 退款订单单号信息
     * @return 退款查询结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {

        Map<String, Object> parameters = getUseQueryPay();
        BaiduTransactionType transactionType = BaiduTransactionType.REFUND_QUERY;
        parameters.put(METHOD, transactionType.getMethod());
        parameters.put(TYPE, 3);
        parameters.put(ORDER_ID, refundOrder.getTradeNo());
        parameters.put(USER_ID, refundOrder.getUserId());
        parameters.put(APP_KEY, payConfigStorage.getAppKey());
        parameters.put(RSA_SIGN, getRsaSign(parameters, RSA_SIGN));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(transactionType), UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }

    /**
     * 下载资金账单
     *
     * @param billDate    账单时间：日账单格式为yyyy-MM-dd
     * @param accessToken 用户token
     * @return 对账单
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String accessToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", accessToken);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }

    /**
     * 下载订单对账单
     *
     * @param billDate    账单时间：日账单格式为yyyy-MM-dd
     * @param accessToken 用户token
     * @return 账单结果
     */
    public Map<String, Object> downloadOrderBill(Date billDate, String accessToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", accessToken);
        parameters.put("billTime", DateUtils.formatDay(billDate));
        return requestTemplate.getForObject(String.format("%s?%s", getReqUrl(BaiduTransactionType.DOWNLOAD_ORDER_BILL),
                UriVariables.getMapToParameters(parameters)), JSONObject.class);
    }

    /**
     * 通用查询接口
     *
     * @param orderId         订单id
     * @param siteId          用户id
     * @param transactionType 交易类型
     * @return 结果
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
     * @return 请求URL
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return ((BaiduTransactionType) transactionType).getUrl();
    }

    /**
     * 签名
     *
     * @param params     参数
     * @param ignoreKeys 忽略字段
     * @return 签名结果
     */
    private String getRsaSign(Map<String, Object> params, String... ignoreKeys) {
        String waitSignVal = SignUtils.parameterText(params, "&", false, ignoreKeys);
        return SignUtils.valueOf(payConfigStorage.getSignType()).createSign(waitSignVal, payConfigStorage.getKeyPrivate(), payConfigStorage.getInputCharset());
    }
}
