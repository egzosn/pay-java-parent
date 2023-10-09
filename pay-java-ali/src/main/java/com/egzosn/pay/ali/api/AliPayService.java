package com.egzosn.pay.ali.api;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.egzosn.pay.ali.bean.AliPayConst.ALIPAY_CERT_SN_FIELD;
import static com.egzosn.pay.ali.bean.AliPayConst.APP_AUTH_TOKEN;
import static com.egzosn.pay.ali.bean.AliPayConst.BIZ_CONTENT;
import static com.egzosn.pay.ali.bean.AliPayConst.CODE;
import static com.egzosn.pay.ali.bean.AliPayConst.DBACK_AMOUNT;
import static com.egzosn.pay.ali.bean.AliPayConst.HTTPS_REQ_URL;
import static com.egzosn.pay.ali.bean.AliPayConst.NOTIFY_URL;
import static com.egzosn.pay.ali.bean.AliPayConst.PASSBACK_PARAMS;
import static com.egzosn.pay.ali.bean.AliPayConst.PAYEE_INFO;
import static com.egzosn.pay.ali.bean.AliPayConst.PRODUCT_CODE;
import static com.egzosn.pay.ali.bean.AliPayConst.RETURN_URL;
import static com.egzosn.pay.ali.bean.AliPayConst.SIGN;
import static com.egzosn.pay.ali.bean.AliPayConst.SUCCESS_CODE;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.ali.bean.AliPayBillType;
import com.egzosn.pay.ali.bean.AliPayConst;
import com.egzosn.pay.ali.bean.AliPayMessage;
import com.egzosn.pay.ali.bean.AliRefundResult;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.ali.bean.AliTransferType;
import com.egzosn.pay.ali.bean.CertEnvironment;
import com.egzosn.pay.ali.bean.OrderSettle;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.TransferService;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.Order;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.TransferType;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.sign.SignTextUtils;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 支付宝支付服务
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2017-2-22 20:09
 */
public class AliPayService extends BasePayService<AliPayConfigStorage> implements TransferService, AliPayServiceInf {


    /**
     * api服务地址，默认为国内
     */
    private String apiServerUrl;

    /**
     * 获取对应的请求地址
     *
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        if (StringUtils.isNotEmpty(apiServerUrl)) {
            return apiServerUrl;
        }
        return payConfigStorage.isTest() ? AliPayConst.DEV_REQ_URL : HTTPS_REQ_URL;
    }

    /**
     * 获取对应的请求地址
     *
     * @return 请求地址
     */
    public String getReqUrl() {
        return getReqUrl(null);
    }


    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     */
    @Override
    public AliPayService setPayConfigStorage(AliPayConfigStorage payConfigStorage) {
        payConfigStorage.loadCertEnvironment();
        super.setPayConfigStorage(payConfigStorage);
        return this;
    }

    public AliPayService(AliPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    public AliPayService(AliPayConfigStorage payConfigStorage) {
        this(payConfigStorage, null);
    }


    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Deprecated
    @Override
    public boolean verify(Map<String, Object> params) {
        return verify(new NoticeParams(params));
    }

    /**
     * 回调校验
     *
     * @param noticeParams 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(NoticeParams noticeParams) {
        final Map<String, Object> params = noticeParams.getBody();
        if (params.get(SIGN) == null) {
            LOG.debug("支付宝支付异常：params：{}", params);
            return false;
        }

        return signVerify(params, (String) params.get(SIGN));
    }


    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @return 生成的签名结果
     */
    public boolean signVerify(Map<String, Object> params, String sign) {

        if (params instanceof JSONObject) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (SIGN.equals(entry.getKey()) || ALIPAY_CERT_SN_FIELD.equals(entry.getKey())) {
                    continue;
                }
                TreeMap<String, Object> response = new TreeMap((Map<String, Object>) entry.getValue());
                LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
                linkedHashMap.put(CODE, response.remove(CODE));
                linkedHashMap.put("msg", response.remove("msg"));
                linkedHashMap.putAll(response);
                return SignUtils.valueOf(payConfigStorage.getSignType()).verify(JSON.toJSONString(linkedHashMap), sign, getKeyPublic(params), payConfigStorage.getInputCharset());
            }
        }
        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params, sign, getKeyPublic(params), payConfigStorage.getInputCharset());
    }

    /**
     * 获取公钥信息
     *
     * @param params 响应参数
     * @return 公钥信息
     */
    protected String getKeyPublic(Map<String, Object> params) {
        if (!payConfigStorage.isCertSign()) {
            return payConfigStorage.getKeyPublic();
        }
        return payConfigStorage.getCertEnvironment().getAliPayPublicKey(getAliPayCertSN(params));
    }

    /**
     * 从响应Map中提取支付宝公钥证书序列号
     *
     * @param respMap 响应Map
     * @return 支付宝公钥证书序列号
     */
    public String getAliPayCertSN(java.util.Map<String, Object> respMap) {
        return (String) respMap.get(ALIPAY_CERT_SN_FIELD);
    }


    /**
     * 生成并设置签名
     *
     * @param parameters 请求参数
     * @return 请求参数
     */
    protected Map<String, Object> setSign(Map<String, Object> parameters) {
        parameters.put("sign_type", payConfigStorage.getSignType());
        String sign = createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset());

        parameters.put(SIGN, sign);
        return parameters;
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

        return setSign(getOrder(order));
    }

    private void setNotifyUrl(Map<String, Object> orderInfo, AssistOrder order) {
//        orderInfo.put(NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, order.getNotifyUrl());
        OrderParaStructure.loadParameters(orderInfo, NOTIFY_URL, order);
    }

    private void setReturnUrl(Map<String, Object> orderInfo, PayOrder order) {
        orderInfo.put(RETURN_URL, payConfigStorage.getReturnUrl());
        OrderParaStructure.loadParameters(orderInfo, RETURN_URL, order);
    }

    /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return 返回支付宝预下单信息
     * @see PayOrder 支付订单信息
     */
    protected Map<String, Object> getOrder(PayOrder order) {


        Map<String, Object> orderInfo = getPublicParameters(order.getTransactionType());
        setNotifyUrl(orderInfo, order);
        orderInfo.put("format", "json");
        setAppAuthToken(orderInfo, order.getAttrs());

        Map<String, Object> bizContent = new TreeMap<>();
        bizContent.put("body", order.getBody());
        OrderParaStructure.loadParameters(bizContent, "seller_id", payConfigStorage.getSeller());
        bizContent.put("subject", order.getSubject());
        bizContent.put("out_trade_no", order.getOutTradeNo());
        bizContent.put("total_amount", Util.conversionAmount(order.getPrice()).toString());
        switch ((AliTransactionType) order.getTransactionType()) {
            case PAGE:
                bizContent.put(PASSBACK_PARAMS, order.getAddition());
                bizContent.put(PRODUCT_CODE, "FAST_INSTANT_TRADE_PAY");
                bizContent.put(AliPayConst.REQUEST_FROM_URL, payConfigStorage.getReturnUrl());
                OrderParaStructure.loadParameters(bizContent, AliPayConst.REQUEST_FROM_URL, order);
                setReturnUrl(orderInfo, order);
                break;
            case WAP:
                bizContent.put(PASSBACK_PARAMS, order.getAddition());
                //产品码。
                //商家和支付宝签约的产品码。 枚举值（点击查看签约情况）：
                //QUICK_WAP_WAY：无线快捷支付产品。
                //默认值为QUICK_WAP_PAY。
                bizContent.put(PRODUCT_CODE, "QUICK_WAP_PAY");
                OrderParaStructure.loadParameters(bizContent, PRODUCT_CODE, order);

                bizContent.put(AliPayConst.QUIT_URL, payConfigStorage.getReturnUrl());
                OrderParaStructure.loadParameters(bizContent, AliPayConst.QUIT_URL, order);
                setReturnUrl(orderInfo, order);
                break;
            case APP:
                bizContent.put(PASSBACK_PARAMS, order.getAddition());
                bizContent.put(PRODUCT_CODE, "QUICK_MSECURITY_PAY");
                break;
            case MINAPP:
                bizContent.put("extend_params", order.getAddition());
                bizContent.put("buyer_id", order.getOpenid());
                bizContent.put(PRODUCT_CODE, "FACE_TO_FACE_PAYMENT");
                break;
            case BAR_CODE:
            case WAVE_CODE:
            case SECURITY_CODE:
                bizContent.put("scene", order.getTransactionType().toString().toLowerCase());
                bizContent.put(PRODUCT_CODE, "FACE_TO_FACE_PAYMENT");
                bizContent.put("auth_code", order.getAuthCode());
                break;

        }


        setExpirationTime(bizContent, order);

        bizContent.putAll(order.getAttrs());
        orderInfo.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        return preOrderHandler(orderInfo, order);
    }

    private Map<String, Object> setExpirationTime(Map<String, Object> bizContent, PayOrder order) {
        if (null == order.getExpirationTime()) {
            return bizContent;
        }
        bizContent.put("timeout_express", DateUtils.minutesRemaining(order.getExpirationTime()) + "m");
        switch ((AliTransactionType) order.getTransactionType()) {
            case SWEEPPAY:
                bizContent.put("qr_code_timeout_express", DateUtils.minutesRemaining(order.getExpirationTime()) + "m");
                break;
            case PAGE:
            case WAP:
            case APP:
                bizContent.put("time_expire", DateUtils.formatDate(order.getExpirationTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                break;
            default:
        }
        return bizContent;
    }

    /**
     * 获取公共请求参数
     *
     * @param transactionType 交易类型
     * @return 放回公共请求参数
     */
    protected Map<String, Object> getPublicParameters(TransactionType transactionType) {
        boolean depositBack = transactionType == AliTransactionType.REFUND_DEPOSITBACK_COMPLETED;
        Map<String, Object> orderInfo = new TreeMap<>();
        orderInfo.put("app_id", payConfigStorage.getAppId());
        orderInfo.put("charset", payConfigStorage.getInputCharset());
        String method = "method";
        String version = "1.0";
        if (depositBack) {
            method = "msg_method";
            orderInfo.put("utc_timestamp", System.currentTimeMillis());
            version = "1.1";
        }
        else {
            orderInfo.put("timestamp", DateUtils.format(new Date()));
        }

        orderInfo.put(method, transactionType.getMethod());
        orderInfo.put("version", version);

        loadCertSn(orderInfo);
        return orderInfo;
    }

    /**
     * 加载证书序列
     *
     * @param orderInfo 订单信息
     */
    protected void loadCertSn(Map<String, Object> orderInfo) {
        if (payConfigStorage.isCertSign()) {
            final CertEnvironment certEnvironment = payConfigStorage.getCertEnvironment();
            OrderParaStructure.loadParameters(orderInfo, "app_cert_sn", certEnvironment.getMerchantCertSN());
            OrderParaStructure.loadParameters(orderInfo, "alipay_root_cert_sn", certEnvironment.getRootCertSN());
        }
    }


    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.TEXT().content(code.toLowerCase()).build();
    }

    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return PayOutMessage.TEXT().content("success").build();
    }

    @Override
    public String toPay(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(AliTransactionType.PAGE);
        }
        else if (order.getTransactionType() != AliTransactionType.PAGE && order.getTransactionType() != AliTransactionType.WAP) {
            throw new PayErrorException(new PayException("-1", "错误的交易类型:" + order.getTransactionType()));
        }
        return super.toPay(order);
    }

    /**
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        StringBuilder formHtml = new StringBuilder();
        formHtml.append("<form id=\"_alipaysubmit_\" name=\"alipaysubmit\" action=\"");
        String bizContent = (String) orderInfo.remove(BIZ_CONTENT);
        formHtml.append(getReqUrl()).append("?").append(UriVariables.getMapToParameters(orderInfo))
                .append("\" method=\"").append(method.name().toLowerCase()).append("\">");
        formHtml.append("<input type=\"hidden\" name=\"biz_content\" value=\'").append(bizContent).append("\'/>");
        formHtml.append("</form>");
        formHtml.append("<script>document.forms['_alipaysubmit_'].submit();</script>");

        return formHtml.toString();
    }


    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @return 返回二维码信息,，支付时需要的
     */
    @Override
    public String getQrPay(PayOrder order) {
        order.setTransactionType(AliTransactionType.SWEEPPAY);
        Map<String, Object> orderInfo = orderInfo(order);
        //预订单
        JSONObject result = getHttpRequestTemplate().postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(orderInfo), null, JSONObject.class);
        JSONObject response = result.getJSONObject("alipay_trade_precreate_response");
        if (SUCCESS_CODE.equals(response.getString(CODE))) {
            return response.getString("qr_code");
        }
        throw new PayErrorException(new PayException(response.getString(CODE), response.getString("msg"), result.toJSONString()));

    }

    /**
     * pos主动扫码付款(条码付)
     *
     * @param order 发起支付的订单信息
     * @return 支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(AliTransactionType.BAR_CODE);
        }
        else if (order.getTransactionType() != AliTransactionType.BAR_CODE && order.getTransactionType() != AliTransactionType.WAVE_CODE && order.getTransactionType() != AliTransactionType.SECURITY_CODE) {
            throw new PayErrorException(new PayException("-1", "错误的交易类型:" + order.getTransactionType()));
        }

        Map<String, Object> orderInfo = orderInfo(order);
        //预订单
        JSONObject result = getHttpRequestTemplate().postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(orderInfo), null, JSONObject.class);
        JSONObject response = result.getJSONObject("alipay_trade_pay_response");
        if (!SUCCESS_CODE.equals(response.getString(CODE))) {
            LOG.info("收款失败");
        }
        return result;
    }


    /**
     * 统一收单交易结算接口
     *
     * @param order 交易结算信息
     * @return 结算结果
     */
    public Map<String, Object> settle(OrderSettle order) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.SETTLE);
        setAppAuthToken(parameters, order.getAttrs());
        final Map<String, Object> bizContent = order.toBizContent();
        bizContent.putAll(order.getAttrs());
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return getHttpRequestTemplate().postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), null, JSONObject.class);
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
        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.QUERY);

    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        if (null == assistOrder.getTransactionType()) {
            assistOrder.setTransactionType(AliTransactionType.QUERY);
        }
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(assistOrder.getTransactionType());
        Map<String, Object> bizContent = new TreeMap<>();
        OrderParaStructure.loadParameters(bizContent, "query_options", assistOrder);

        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(getBizContent(assistOrder.getTradeNo(), assistOrder.getOutTradeNo(), bizContent)));
        //设置签名
        setSign(parameters);
        return requestTemplate.getForObject(getReqUrl(assistOrder.getTransactionType()) + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);
    }


    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.CLOSE);
    }

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        return secondaryInterface(assistOrder.getTradeNo(), assistOrder.getOutTradeNo(), AliTransactionType.CLOSE);
    }

    /**
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易。
     * 如果此订单用户支付失败，支付宝系统会将此订单关闭；如果用户支付成功，支付宝系统会将此订单资金退还给用户。
     * 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。
     * 提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public Map<String, Object> cancel(String tradeNo, String outTradeNo) {
        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.CANCEL);
    }

    /**
     * 设置支付宝授权Token
     *
     * @param parameters 参数
     * @param attrs      订单属性
     */
    protected void setAppAuthToken(Map<String, Object> parameters, Map<String, Object> attrs) {
        setAppAuthToken(parameters);
        OrderParaStructure.loadParameters(parameters, APP_AUTH_TOKEN, (String) attrs.remove(APP_AUTH_TOKEN));
    }

    /**
     * 设置支付宝授权Token
     *
     * @param parameters 参数
     */
    protected void setAppAuthToken(Map<String, Object> parameters) {
        OrderParaStructure.loadParameters(parameters, APP_AUTH_TOKEN, payConfigStorage.getAppAuthToken());
    }


    /**
     * 申请退款接口
     * 兼容 收单退款冲退完成通知 {@link #refundDepositBackCompleted(RefundOrder)} 与 {@link com.egzosn.pay.ali.bean.RefundDepositBackCompletedNotify}
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public AliRefundResult refund(RefundOrder refundOrder) {
        if (null != refundOrder.getTransactionType() && refundOrder.getTransactionType() == AliTransactionType.REFUND_DEPOSITBACK_COMPLETED) {
            String status = refundDepositBackCompleted(refundOrder);
            AliRefundResult result = new AliRefundResult();
            result.setCode(status);
            return result;
        }
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.REFUND);
        setAppAuthToken(parameters, refundOrder.getAttrs());

        Map<String, Object> bizContent = getBizContent(refundOrder.getTradeNo(), refundOrder.getOutTradeNo(), null);
        OrderParaStructure.loadParameters(bizContent, AliPayConst.OUT_REQUEST_NO, refundOrder.getRefundNo());
        bizContent.put("refund_amount", Util.conversionAmount(refundOrder.getRefundAmount()));
        OrderParaStructure.loadParameters(bizContent, AliPayConst.REFUND_REASON, refundOrder.getDescription());
        OrderParaStructure.loadParameters(bizContent, AliPayConst.REFUND_REASON, refundOrder);
        OrderParaStructure.loadParameters(bizContent, "refund_royalty_parameters", refundOrder);
        OrderParaStructure.loadParameters(bizContent, AliPayConst.QUERY_OPTIONS, refundOrder);
        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        JSONObject result = requestTemplate.getForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);
        JSONObject refundResponse = result.getJSONObject("alipay_trade_refund_response");
        AliRefundResult refundResult = AliRefundResult.create(refundResponse);
        refundResult.setOutRequestNo(refundOrder.getRefundNo());
        return refundResult;
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.REFUNDQUERY);
        setAppAuthToken(parameters, refundOrder.getAttrs());
        Map<String, Object> bizContent = getBizContent(refundOrder.getTradeNo(), refundOrder.getOutTradeNo(), null);
        OrderParaStructure.loadParameters(bizContent, AliPayConst.OUT_REQUEST_NO, refundOrder.getRefundNo());
        OrderParaStructure.loadParameters(bizContent, AliPayConst.QUERY_OPTIONS, refundOrder);
//        bizContent.putAll(refundOrder.getAttrs());
        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return requestTemplate.getForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);

    }

    /**
     * 目前只支持日账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String billType) {

        return this.downloadBill(billDate, "trade".equals(billType) ? AliPayBillType.TRADE_DAY : AliPayBillType.SIGNCUSTOMER_DAY);
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.DOWNLOADBILL);

        Map<String, Object> bizContent = new TreeMap<>();
        bizContent.put("bill_type", billType.getType());
        //目前只支持日账单
        bizContent.put("bill_date", DateUtils.formatDate(billDate, billType.getDatePattern()));
        //设置请求参数的集合
        final String bizContentStr = JSON.toJSONString(bizContent);
        parameters.put(BIZ_CONTENT, bizContentStr);
        //设置签名
        setSign(parameters);
        Map<String, String> bizContentMap = new HashMap<String, String>(1);
        parameters.put(BIZ_CONTENT, bizContentStr);
        return requestTemplate.postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), bizContentMap, JSONObject.class);

    }

    /**
     * @param tradeNoOrBillDate  支付平台订单号或者账单类型， 具体请
     *                           类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType 商户单号或者 账单类型
     * @param transactionType    交易类型
     * @return 返回支付方对应接口的结果
     */
    public Map<String, Object> secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType) {

        if (transactionType == AliTransactionType.REFUND) {
            throw new PayErrorException(new PayException("failure", "通用接口不支持:" + transactionType));
        }

        if (transactionType == AliTransactionType.DOWNLOADBILL) {
            if (tradeNoOrBillDate instanceof Date) {
                return downloadBill((Date) tradeNoOrBillDate, outTradeNoBillType);
            }
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + tradeNoOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(transactionType);

        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, getContentToJson((String) tradeNoOrBillDate, outTradeNoBillType));
        //设置签名
        setSign(parameters);

        return requestTemplate.getForObject(getReqUrl(transactionType) + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);
    }

    /**
     * 新版转账转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        final TransferType transferType = order.getTransferType();
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(transferType);
        setAppAuthToken(parameters, order.getAttrs());

        Map<String, Object> bizContent = new LinkedHashMap<String, Object>();
        bizContent.put("out_biz_no", order.getOutNo());
        bizContent.put("trans_amount", order.getAmount());
        transferType.setAttr(bizContent, order);
        OrderParaStructure.loadParameters(bizContent, "order_title", order);
        OrderParaStructure.loadParameters(bizContent, "original_order_id", order);
        setPayeeInfo(bizContent, order);
        bizContent.put("remark", order.getRemark());
        OrderParaStructure.loadParameters(bizContent, "business_params", order);

        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return getHttpRequestTemplate().postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), null, JSONObject.class);
    }

    /**
     * 转账查询
     *
     * @param assistOrder 辅助交易订单
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(AssistOrder assistOrder) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransferType.TRANS_QUERY);

        Map<String, Object> bizContent = new TreeMap<String, Object>();
        if (StringUtils.isEmpty(assistOrder.getOutTradeNo())) {
            bizContent.put("order_id", assistOrder.getTradeNo());
        }
        else {
            bizContent.put("out_biz_no", assistOrder.getOutTradeNo());
        }
        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return getHttpRequestTemplate().postForObject(getReqUrl() + "?" + UriVariables.getMapToParameters(parameters), null, JSONObject.class);

    }

    private Map<String, Object> setPayeeInfo(Map<String, Object> bizContent, Order order) {
        final Object attr = order.getAttr(PAYEE_INFO);

        if (attr instanceof String) {
            bizContent.put(PAYEE_INFO, attr);
        }
        if (attr instanceof TreeMap) {
            bizContent.put(PAYEE_INFO, attr);
        }
        if (attr instanceof Map) {
            Map<String, Object> payeeInfo = new TreeMap<String, Object>((Map) attr);
            bizContent.put(PAYEE_INFO, payeeInfo);
        }
        return bizContent;
    }


    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(String outNo, String tradeNo) {

        return transferQuery(new AssistOrder(tradeNo, outNo));
    }


    /**
     * 获取biz_content。请求参数的集合 不包含下载账单
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param bizContent 请求参数的集合
     * @return 请求参数的集合 不包含下载账单
     */
    private Map<String, Object> getBizContent(String tradeNo, String outTradeNo, Map<String, Object> bizContent) {
        if (null == bizContent) {
            bizContent = new TreeMap<>();
        }
        if (!StringUtils.isEmpty(outTradeNo)) {
            bizContent.put("out_trade_no", outTradeNo);
        }
        if (!StringUtils.isEmpty(tradeNo)) {
            bizContent.put("trade_no", tradeNo);
        }
        return bizContent;
    }

    /**
     * 获取biz_content。不包含下载账单
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 获取biz_content。不包含下载账单
     */
    private String getContentToJson(String tradeNo, String outTradeNo) {

        return JSON.toJSONString(getBizContent(tradeNo, outTradeNo, null));
    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return AliPayMessage.create(message);
    }

    /**
     * 收单退款冲退完成通知
     * 退款存在退到银行卡场景下时，收单会根据银行回执消息发送退款完成信息
     *
     * @param refundOrder 退款订单
     * @return fail    消息获取失败	是  success	消息获取成功	否
     */
    @Override
    public String refundDepositBackCompleted(RefundOrder refundOrder) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(refundOrder.getTransactionType());
        OrderParaStructure.loadParameters(parameters, "notify_id", refundOrder);
        OrderParaStructure.loadParameters(parameters, "msg_type", refundOrder);
        OrderParaStructure.loadParameters(parameters, "msg_uid", refundOrder);
        OrderParaStructure.loadParameters(parameters, "msg_app_id", refundOrder);

        Map<String, Object> bizContent = getBizContent(refundOrder.getTradeNo(), refundOrder.getOutTradeNo(), null);
        OrderParaStructure.loadParameters(bizContent, AliPayConst.OUT_REQUEST_NO, refundOrder.getRefundNo());
        OrderParaStructure.loadParameters(bizContent, "dback_status", refundOrder);
        bizContent.put(DBACK_AMOUNT, refundOrder.getRefundAmount());
        OrderParaStructure.loadParameters(bizContent, DBACK_AMOUNT, refundOrder);
        OrderParaStructure.loadParameters(bizContent, "bank_ack_time", refundOrder);
        OrderParaStructure.loadParameters(bizContent, "est_bank_receipt_time", refundOrder);
        //设置请求参数的集合
        parameters.put(BIZ_CONTENT, JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);

        return null;
    }

    /**
     * 设置api服务器地址
     *
     * @param apiServerUrl api服务器地址
     * @return 自身
     */
    @Override
    public AliPayServiceInf setApiServerUrl(String apiServerUrl) {
        this.apiServerUrl = apiServerUrl;
        return this;
    }


}
