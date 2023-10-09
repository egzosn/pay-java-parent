package com.egzosn.pay.wx.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import static com.egzosn.pay.wx.api.WxConst.APPID;
import static com.egzosn.pay.wx.api.WxConst.CIPHER_ALGORITHM;
import static com.egzosn.pay.wx.api.WxConst.FAIL;
import static com.egzosn.pay.wx.api.WxConst.FAILURE;
import static com.egzosn.pay.wx.api.WxConst.HMACSHA256;
import static com.egzosn.pay.wx.api.WxConst.HMAC_SHA256;
import static com.egzosn.pay.wx.api.WxConst.MCH_ID;
import static com.egzosn.pay.wx.api.WxConst.NONCE_STR;
import static com.egzosn.pay.wx.api.WxConst.OUT_TRADE_NO;
import static com.egzosn.pay.wx.api.WxConst.REQ_INFO;
import static com.egzosn.pay.wx.api.WxConst.RESULT_CODE;
import static com.egzosn.pay.wx.api.WxConst.RETURN_CODE;
import static com.egzosn.pay.wx.api.WxConst.RETURN_MSG_CODE;
import static com.egzosn.pay.wx.api.WxConst.SANDBOXNEW;
import static com.egzosn.pay.wx.api.WxConst.SIGN;
import static com.egzosn.pay.wx.api.WxConst.SUCCESS;
import static com.egzosn.pay.wx.api.WxConst.URI;
import static com.egzosn.pay.wx.bean.WxTransferType.GETTRANSFERINFO;
import static com.egzosn.pay.wx.bean.WxTransferType.QUERY_BANK;
import static com.egzosn.pay.wx.bean.WxTransferType.TRANSFERS;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.TransferService;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.SignType;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.TransferType;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.ClientHttpRequest;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.HttpStringEntity;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.XML;
import com.egzosn.pay.common.util.sign.SignTextUtils;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.sign.encrypt.AES;
import com.egzosn.pay.common.util.sign.encrypt.RSA2;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.bean.RedpackOrder;
import com.egzosn.pay.wx.bean.WxPayBillType;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.bean.WxPayMessage;
import com.egzosn.pay.wx.bean.WxRefundResult;
import com.egzosn.pay.wx.bean.WxSendredpackType;
import com.egzosn.pay.wx.bean.WxTransactionType;
import com.egzosn.pay.wx.bean.WxTransferType;

/**
 * 微信支付服务
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 * </pre>
 */
public class WxPayService extends BasePayService<WxPayConfigStorage> implements WxRedPackService, WxBillService, TransferService {


    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     */
    public WxPayService(WxPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     * @param configStorage    微信对应的网络配置，包含代理配置、ssl证书配置
     */
    public WxPayService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     */
    @Override
    public BasePayService setPayConfigStorage(WxPayConfigStorage payConfigStorage) {
        String signType = payConfigStorage.getSignType();
        if (HMAC_SHA256.equals(signType)) {
            payConfigStorage.setSignType(HMACSHA256);
        }
        this.payConfigStorage = payConfigStorage;
        return this;
    }

    /**
     * 根据交易类型获取url
     *
     * @param transactionType 交易类型
     * @return 请求url
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {

        return URI + (payConfigStorage.isTest() ? SANDBOXNEW : "") + transactionType.getMethod();
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
        //如果为退款不需要校验, 直接返回，
        if (params.containsKey(REQ_INFO)) {
            return true;
        }

        if (Objects.isNull(params.get(SIGN)) || !(SUCCESS.equals(params.get(RETURN_CODE)) && SUCCESS.equals(params.get(RESULT_CODE)))) {
            if (LOG.isErrorEnabled()) {
                LOG.error(String.format("微信支付异常：return_code=%s,参数集=%s", params.get(RETURN_CODE), params));
            }
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
    private boolean signVerify(Map<String, Object> params, String sign) {
        return signVerify(params, sign, payConfigStorage.isTest());
    }

    private boolean signVerify(Map<String, Object> params, String sign, boolean isTest) {
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());
        String keyPrivate = payConfigStorage.getKeyPrivate();
        if (isTest) {
            keyPrivate = getKeyPrivate();
        }
        String content = SignTextUtils.parameterText(params, "&", SIGN, "appId") + "&key=" + (signUtils == SignUtils.MD5 ? "" : keyPrivate);
        return signUtils.verify(content, sign, keyPrivate, payConfigStorage.getInputCharset());
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, Object> getPublicParameters() {

        Map<String, Object> parameters = new TreeMap<>();
        parameters.put(APPID, payConfigStorage.getAppId());
        parameters.put(MCH_ID, payConfigStorage.getMchId());
        //判断如果是服务商模式信息则加入
        OrderParaStructure.loadParameters(parameters, "sub_mch_id", payConfigStorage.getSubMchId());
        OrderParaStructure.loadParameters(parameters, "sub_appid", payConfigStorage.getSubAppId());
        parameters.put(NONCE_STR, SignTextUtils.randomStr());
        return parameters;


    }


    /**
     * 微信统一下单接口
     *
     * @param order 支付订单集
     * @return 下单结果
     */
    public JSONObject unifiedOrder(PayOrder order) {

        //统一下单
        Map<String, Object> parameters = getPublicParameters();
        // 购买支付信息
        parameters.put("body", order.getSubject());
        // 购买支付信息
        OrderParaStructure.loadParameters(parameters, "detail", order);
        // 订单号
        parameters.put(OUT_TRADE_NO, order.getOutTradeNo());
        parameters.put("spbill_create_ip", StringUtils.isEmpty(order.getSpbillCreateIp()) ? "192.168.1.150" : order.getSpbillCreateIp());
        // 总金额单位为分
        parameters.put("total_fee", Util.conversionCentAmount(order.getPrice()));
        OrderParaStructure.loadParameters(parameters, "attach", order.getAddition());
        initNotifyUrl(parameters, order);
        parameters.put("trade_type", order.getTransactionType().getType());
        if (null != order.getExpirationTime()) {
            parameters.put("time_start", DateUtils.formatDate(new Date(), DateUtils.YYYYMMDDHHMMSS));
            parameters.put("time_expire", DateUtils.formatDate(order.getExpirationTime(), DateUtils.YYYYMMDDHHMMSS));
        }

        if (null != order.getCurType()) {
            parameters.put("fee_type", order.getCurType().getType());
        }

        ((WxTransactionType) order.getTransactionType()).setAttribute(parameters, order);
        //可覆盖参数
/*        OrderParaStructure.loadParameters(parameters, NOTIFY_URL, order);
        OrderParaStructure.loadParameters(parameters, "goods_tag", order);
        OrderParaStructure.loadParameters(parameters, "limit_pay", order);
        OrderParaStructure.loadParameters(parameters, "receipt", order);
        OrderParaStructure.loadParameters(parameters, "product_id", order);*/
        parameters.putAll(order.getAttrs());
        parameters = preOrderHandler(parameters, order);
        setSign(parameters);

        String requestXML = XML.getMap2Xml(parameters);
        if (LOG.isDebugEnabled()) {
            LOG.debug("requestXML：" + requestXML);
        }

        HttpStringEntity entity = new HttpStringEntity(requestXML, ClientHttpRequest.APPLICATION_XML_UTF_8);

        //调起支付的参数列表
        JSONObject result = requestTemplate.postForObject(getReqUrl(order.getTransactionType()), entity, JSONObject.class);

        if (!SUCCESS.equals(result.get(RETURN_CODE)) || !SUCCESS.equals(result.get(RESULT_CODE))) {
            throw new PayErrorException(new WxPayError(result.getString(RESULT_CODE), result.getString(RETURN_MSG_CODE), result.toJSONString()));
        }
        return result;
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

        ////统一下单
        JSONObject result = unifiedOrder(order);
        // 对微信返回的数据进行校验
        if (verify(new NoticeParams(preOrderHandler(result, order)))) {
            //如果是扫码支付或者刷卡付无需处理，直接返回
            if (((WxTransactionType) order.getTransactionType()).isReturn()) {
                return result;
            }

            Map<String, Object> params = new TreeMap<>();

            if (WxTransactionType.JSAPI == order.getTransactionType()) {
                params.put("signType", payConfigStorage.getSignType());
                params.put("appId", payConfigStorage.getAppId());
                params.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                params.put("nonceStr", result.get(NONCE_STR));
                params.put("package", "prepay_id=" + result.get("prepay_id"));
            }
            else if (WxTransactionType.APP == order.getTransactionType()) {
                params.put("partnerid", payConfigStorage.getPid());
                params.put(APPID, payConfigStorage.getAppId());
                params.put("prepayid", result.get("prepay_id"));
                params.put("timestamp", System.currentTimeMillis() / 1000);
                params.put("noncestr", result.get(NONCE_STR));
                params.put("package", "Sign=WXPay");
            }
            String paySign = createSign(SignTextUtils.parameterText(params), payConfigStorage.getInputCharset());
            params.put(WxTransactionType.JSAPI.equals(order.getTransactionType()) ? "paySign" : SIGN, paySign);
            return params;
        }
        throw new PayErrorException(new WxPayError(result.getString(RETURN_CODE), result.getString(RETURN_MSG_CODE), "Invalid sign value"));

    }

    /**
     * 生成并设置签名
     *
     * @param parameters 请求参数
     * @return 请求参数
     */
    private Map<String, Object> setSign(Map<String, Object> parameters) {

        String signTypeStr = payConfigStorage.getSignType();
        if (HMACSHA256.equals(signTypeStr)) {
            signTypeStr = SignUtils.HMACSHA256.getName();
        }
        parameters.put("sign_type", signTypeStr);
        String sign = createSign(SignTextUtils.parameterText(parameters, "&", SIGN, "appId"), payConfigStorage.getInputCharset());
        parameters.put(SIGN, sign);
        return parameters;
    }


    /**
     * 获取验签秘钥
     *
     * @return 验签秘钥
     */
    private String getKeyPrivate() {
        if (!payConfigStorage.isTest()) {
            return payConfigStorage.getKeyPrivate();
        }
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put(MCH_ID, payConfigStorage.getMchId());
        parameters.put(NONCE_STR, SignTextUtils.randomStr());

        String sign = createSign(SignTextUtils.parameterText(parameters, "&", SIGN, "appId"), payConfigStorage.getInputCharset(), false);
        parameters.put(SIGN, sign);

        HttpStringEntity entity = new HttpStringEntity(XML.getMap2Xml(parameters), ClientHttpRequest.APPLICATION_XML_UTF_8);
        JSONObject result = requestTemplate.postForObject(getReqUrl(WxTransactionType.GETSIGNKEY), entity, JSONObject.class);
        if (SUCCESS.equals(result.get(RETURN_CODE))) {
            return result.getString("sandbox_signkey");
        }
        LOG.error("获取sandbox_signkey失败", new PayErrorException(new PayException(result.getString(RETURN_CODE), result.getString(RETURN_MSG_CODE), result.toJSONString())));
        return null;
    }

    /**
     * 签名
     *
     * @param content           需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @return 签名结果
     */
    @Override
    public String createSign(String content, String characterEncoding) {

        return createSign(content, characterEncoding, payConfigStorage.isTest());
    }

    /**
     * 签名
     *
     * @param content           需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @param test              是否为沙箱环境
     * @return 签名结果
     */
    public String createSign(String content, String characterEncoding, boolean test) {
        SignType signType = SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase());
        String keyPrivate = payConfigStorage.getKeyPrivate();
        if (test) {
            keyPrivate = getKeyPrivate();
        }
        return signType.createSign(content + "&key=" + (signType == SignUtils.MD5 ? "" : keyPrivate), keyPrivate, characterEncoding).toUpperCase();
    }


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param body 通知请求`
     * @return 获得回调的请求参数
     */
    public Map<String, Object> getRefundNoticeParams(Map<String, Object> body) {
        String reqInfo = (String) body.get(REQ_INFO);
        if (StringUtils.isEmpty(reqInfo)) {
            return body;
        }
        try {
            String decrypt = AES.decrypt(reqInfo, payConfigStorage.getSecretKey(), payConfigStorage.getInputCharset());
            JSONObject data = XML.toJSONObject(decrypt);
            body.putAll(data);
            return body;
        }
        catch (GeneralSecurityException | IOException e) {
            throw new PayErrorException(new WxPayError(FAIL, e.getMessage()), e);
        }
    }


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param request 通知请求
     * @return 获得回调的请求参数
     */
    @Override
    public NoticeParams getNoticeParams(NoticeRequest request) {

        TreeMap<String, Object> map = new TreeMap<String, Object>();
        try {
            Map<String, Object> body = XML.inputStream2Map(request.getInputStream(), map);
            body = getRefundNoticeParams(body);
            return new NoticeParams(body);
        }
        catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getMessage()));
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
        return PayOutMessage.XML().code(code.toUpperCase()).content(message).build();
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
        return PayOutMessage.XML().code("SUCCESS").content("成功").build();
    }


    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        if (!SUCCESS.equals(orderInfo.get(RETURN_CODE))) {
            throw new PayErrorException(new WxPayError((String) orderInfo.get(RETURN_CODE), (String) orderInfo.get(RETURN_MSG_CODE)));
        }
        if (WxTransactionType.MWEB.name().equals(orderInfo.get("trade_type"))) {
            return String.format("<script type=\"text/javascript\">location.href=\"%s%s\"</script>", orderInfo.get("mweb_url"), StringUtils.isEmpty(payConfigStorage.getReturnUrl()) ? "" : "&redirect_url=" + URLEncoder.encode(payConfigStorage.getReturnUrl()));
        }
        throw new UnsupportedOperationException();

    }

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @return 返回二维码信息,，支付时需要的
     */
    @Override
    public String getQrPay(PayOrder order) {
        order.setTransactionType(WxTransactionType.NATIVE);
        Map<String, Object> orderInfo = orderInfo(order);
        //获取对应的支付账户操作工具（可根据账户id）
        if (!SUCCESS.equals(orderInfo.get(RESULT_CODE))) {
            throw new PayErrorException(new WxPayError((String) orderInfo.get("err_code"), orderInfo.toString()));
        }
        return (String) orderInfo.get("code_url");
    }

    /**
     * 刷卡付,pos主动扫码付款
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(WxTransactionType.MICROPAY);

        }
        else if (WxTransactionType.MICROPAY != order.getTransactionType() && WxTransactionType.FACEPAY != order.getTransactionType()) {
            throw new PayErrorException(new PayException("-1", "错误的交易类型:" + order.getTransactionType()));
        }
        return orderInfo(order);
    }


    /**
     * 交易查询接口
     *
     * @param transactionId 微信支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String transactionId, String outTradeNo) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.QUERY);
    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        return secondaryInterface(assistOrder.getTradeNo(), assistOrder.getOutTradeNo(), WxTransactionType.QUERY);
    }


    /**
     * 交易关闭接口
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(String transactionId, String outTradeNo) {

        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.CLOSE);
    }

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        return secondaryInterface(assistOrder.getTradeNo(), assistOrder.getOutTradeNo(), WxTransactionType.CLOSE);
    }


    /**
     * 交易交易撤销
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public Map<String, Object> cancel(String transactionId, String outTradeNo) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.REVERSE);
    }


    private Map<String, Object> initNotifyUrl(Map<String, Object> parameters, AssistOrder order) {
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order);
        return parameters;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public WxRefundResult refund(RefundOrder refundOrder) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();

        OrderParaStructure.loadParameters(parameters, "transaction_id", refundOrder.getTradeNo());
        OrderParaStructure.loadParameters(parameters, OUT_TRADE_NO, refundOrder.getOutTradeNo());
        OrderParaStructure.loadParameters(parameters, "out_refund_no", refundOrder.getRefundNo());
        parameters.put("total_fee", Util.conversionCentAmount(refundOrder.getTotalAmount()));
        parameters.put("refund_fee", Util.conversionCentAmount(refundOrder.getRefundAmount()));
        initNotifyUrl(parameters, refundOrder);
        if (null != refundOrder.getCurType()) {
            parameters.put("refund_fee_type", refundOrder.getCurType().getType());
        }
        OrderParaStructure.loadParameters(parameters, "refund_desc", refundOrder.getDescription());
        //附加参数，这里可进行覆盖前面所有参数
        parameters.putAll(refundOrder.getAttrs());
        //设置签名
        setSign(parameters);
        return WxRefundResult.create(requestTemplate.postForObject(getReqUrl(WxTransactionType.REFUND), XML.getMap2Xml(parameters), JSONObject.class));
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
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters, "transaction_id", refundOrder.getTradeNo());
        OrderParaStructure.loadParameters(parameters, OUT_TRADE_NO, refundOrder.getOutTradeNo());
        OrderParaStructure.loadParameters(parameters, "out_refund_no", refundOrder.getRefundNo());
        //设置签名
        setSign(parameters);
        return requestTemplate.postForObject(getReqUrl(WxTransactionType.REFUNDQUERY), XML.getMap2Xml(parameters), JSONObject.class);
    }


    /**
     * 目前只支持日账单
     *
     * @param billDate 下载对账单的日期，格式：20140603
     * @param billType 账单类型
     *                 ALL（默认值），返回当日所有订单信息（不含充值退款订单）
     *                 SUCCESS，返回当日成功支付的订单（不含充值退款订单）
     *                 REFUND，返回当日退款订单（不含充值退款订单）
     *                 RECHARGE_REFUND，返回当日充值退款订单
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String billType) {
        return downloadBill(billDate, WxPayBillType.valueOf(billType));
    }

    /**
     * 目前只支持日账单
     *
     * @param billDate 下载对账单的日期，格式：20140603
     * @param billType 账单类型
     *                 ALL（默认值），返回当日所有订单信息（不含充值退款订单）
     *                 SUCCESS，返回当日成功支付的订单（不含充值退款订单）
     *                 REFUND，返回当日退款订单（不含充值退款订单）
     *                 RECHARGE_REFUND，返回当日充值退款订单
     * @return 返回支付方下载对账单的结果, 如果【账单类型】为gzip的话则返回值中key为data值为gzip的输入流
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        parameters.put("bill_type", billType.getType());
        //目前只支持日账单
        parameters.put(WxConst.BILL_DATE, DateUtils.formatDate(billDate, DateUtils.YYYYMMDD));
        String fileType = billType.getFileType();
        OrderParaStructure.loadParameters(parameters, "tar_type", fileType);
        //设置签名
        setSign(parameters);
        Map<String, Object> ret = new HashMap<String, Object>(3);
        ret.put(RETURN_CODE, SUCCESS);
        ret.put(RETURN_MSG_CODE, "ok");
        if (StringUtils.isEmpty(fileType)) {
            String respStr = requestTemplate.postForObject(getReqUrl(WxTransactionType.DOWNLOADBILL), XML.getMap2Xml(parameters), String.class);
            if (respStr.indexOf("<") == 0) {
                return XML.toJSONObject(respStr);
            }
            ret.put("data", respStr);
            return ret;
        }
        InputStream respStream = requestTemplate.postForObject(getReqUrl(WxTransactionType.DOWNLOADBILL), XML.getMap2Xml(parameters), InputStream.class);
        ret.put("data", respStream);
        return ret;
    }


    /**
     * 目前只支持日账单,增加账单返回格式
     *
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param path     账单返回格式 账单存储的基础路径,按月切割
     * @return 返回支付方下载对账单的结果
     */
    @Deprecated
    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType, String path) {
        Map<String, Object> parameters = getDownloadBillParam(billDate, billType, true);
        //设置签名
        setSign(parameters);
        InputStream inputStream = requestTemplate.postForObject(getReqUrl(WxTransactionType.DOWNLOADBILL), XML.getMap2Xml(parameters), InputStream.class);
        //解压流
        try (InputStream fileIs = uncompress(inputStream);) {
            writeToLocal(path + DateUtils.formatDate(new Date(), DateUtils.YYYYMM) + "/" + DateUtils.formatDate(new Date(), DateUtils.YYYYMMDDHHMMSS) + ".txt", fileIs);
            Map<String, Object> ret = new HashMap<>(3);
            ret.put(RETURN_CODE, SUCCESS);
            ret.put(RETURN_MSG_CODE, "ok");
            ret.put("data", path);
            return ret;
        }
        catch (IOException e) {
            throw new PayErrorException(new WxPayError(FAIL, e.getMessage()), e);
        }

    }

    /**
     * GZIP解压缩
     *
     * @param input 输入流账单
     * @return 解压后输入流
     * @throws IOException IOException
     */
    @Deprecated
    public static InputStream uncompress(InputStream input) throws IOException {
        try (GZIPInputStream ungZip = new GZIPInputStream(input); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = ungZip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    /**
     * 将InputStream写入本地文件
     *
     * @param destination 写入本地目录
     * @param inputStream 输入流
     * @throws IOException IOException
     */
    @Deprecated
    private void writeToLocal(String destination, InputStream inputStream) throws IOException {

        // 判断字节大小
        if (inputStream.available() != 0) {
            LOG.debug("结果大小:{}", inputStream.available());
            File file = new File(destination);
            if (!file.getParentFile().exists()) {
                boolean result = file.getParentFile().mkdirs();
                if (!result) {
                    LOG.warn("创建失败");
                }
            }
            try (OutputStream out = new FileOutputStream(file)) {
                int size = 0;
                int len = 0;
                byte[] buf = new byte[1024];
                while ((size = inputStream.read(buf)) != -1) {
                    len += size;
                    out.write(buf, 0, size);
                }
                LOG.debug("最终写入字节数大小:{}", len);
            }

        }
    }


    /**
     * 下载账单公共参数
     *
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param tarType  账单返回格式 默认返回流false ，gzip 时候true
     * @return
     */
    @Deprecated
    private Map<String, Object> getDownloadBillParam(Date billDate, String billType, boolean tarType) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        parameters.put("bill_type", billType);
        //目前只支持日账单
        parameters.put(WxConst.BILL_DATE, DateUtils.formatDate(billDate, DateUtils.YYYYMMDD));
        if (tarType) {
            parameters.put("tar_type", "GZIP");
        }
        return parameters;
    }

    /**
     * @param transactionIdOrBillDate 支付平台订单号或者账单日期， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType      商户单号或者 账单类型
     * @param transactionType         交易类型
     * @return 返回支付方对应接口的结果
     */
    private Map<String, Object> secondaryInterface(Object transactionIdOrBillDate, String outTradeNoBillType, TransactionType transactionType) {

        if (transactionType == WxTransactionType.REFUND) {
            throw new PayErrorException(new PayException(FAILURE, "通用接口不支持:" + transactionType));
        }

        if (transactionType == WxTransactionType.DOWNLOADBILL) {
            if (transactionIdOrBillDate instanceof Date) {
                return downloadBill((Date) transactionIdOrBillDate, WxPayBillType.forType(outTradeNoBillType));
            }
            throw new PayErrorException(new PayException(FAILURE, "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        if (!(null == transactionIdOrBillDate || transactionIdOrBillDate instanceof String)) {
            throw new PayErrorException(new PayException(FAILURE, "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters, OUT_TRADE_NO, outTradeNoBillType);
        OrderParaStructure.loadParameters(parameters, "transaction_id", (String) transactionIdOrBillDate);
        //设置签名
        setSign(parameters);
        return requestTemplate.postForObject(getReqUrl(transactionType), XML.getMap2Xml(parameters), JSONObject.class);
    }

    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        Map<String, Object> parameters = new TreeMap<>();


        parameters.put("partner_trade_no", order.getOutNo());
        parameters.put("amount", Util.conversionCentAmount(order.getAmount()));
        if (!StringUtils.isEmpty(order.getRemark())) {
            parameters.put("desc", order.getRemark());
        }
        parameters.put(NONCE_STR, SignTextUtils.randomStr());
        if (null != order.getTransferType() && TRANSFERS == order.getTransferType()) {
            transfers(parameters, order);
            parameters.put("mchid", payConfigStorage.getPid());
        }
        else {
            parameters.put(MCH_ID, payConfigStorage.getPid());
            order.setTransferType(WxTransferType.PAY_BANK);
            payBank(parameters, order);
        }
        parameters.put(SIGN, createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));

        return getHttpRequestTemplate().postForObject(getReqUrl(order.getTransferType()), XML.getMap2Xml(parameters), JSONObject.class);
    }


    /**
     * 转账到余额所需要参数
     *
     * @param parameters 参数信息
     * @param order      转账订单
     * @return 包装后参数信息
     * <p>
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2">企业付款到零钱</a>
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_2">商户企业付款到银行卡</a>
     * </p>
     */
    private Map<String, Object> transfers(Map<String, Object> parameters, TransferOrder order) {
        //转账到余额, 申请商户号的appid或商户号绑定的appid
        parameters.put("mch_appid", payConfigStorage.getAppId());
        parameters.put("openid", order.getPayeeAccount());
        parameters.put("spbill_create_ip", StringUtils.isEmpty(order.getIp()) ? "192.168.1.150" : order.getIp());
        //默认不校验真实姓名
        parameters.put("check_name", "NO_CHECK");
        //当存在时候 校验收款用户真实姓名
        if (!StringUtils.isEmpty(order.getPayeeName())) {
            parameters.put("check_name", "FORCE_CHECK");
            parameters.put("re_user_name", order.getPayeeName());
        }
        return parameters;
    }

    /**
     * 转账到银行卡所需要参数
     *
     * @param parameters 参数信息
     * @param order      转账订单
     * @return 包装后参数信息
     */
    private Map<String, Object> payBank(Map<String, Object> parameters, TransferOrder order) {

        parameters.put("enc_bank_no", keyPublic(order.getPayeeAccount()));
        parameters.put("enc_true_name", keyPublic(order.getPayeeName()));
        parameters.put("bank_code", order.getBank().getCode());
        return parameters;
    }


    /**
     * 转账查询
     *
     * @param outNo          商户转账订单号
     * @param wxTransferType 微信转账类型，.....这里没办法了只能这样写(┬＿┬)，请见谅 {@link com.egzosn.pay.wx.bean.WxTransferType}
     *                       <p>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">企业付款到零钱</a>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3">商户企业付款到银行卡</a>
     *                       </p>
     * @return 对应的转账订单
     * @deprecated {@link #transferQuery(AssistOrder)}
     */
    @Deprecated
    @Override
    public Map<String, Object> transferQuery(String outNo, String wxTransferType) {
        if (StringUtils.isEmpty(wxTransferType)) {
            throw new PayErrorException(new WxPayError(FAILURE, "微信转账类型必填，详情com.egzosn.pay.wx.bean.WxTransferType"));
        }
        AssistOrder assistOrder = new AssistOrder(outNo);

        assistOrder.setTransactionType(WxTransferType.valueOf(wxTransferType));
        return transferQuery(assistOrder);
    }

    /**
     * 转账查询
     *
     * @param assistOrder 辅助交易订单
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(AssistOrder assistOrder) {
        Map<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put(MCH_ID, payConfigStorage.getPid());
        parameters.put("partner_trade_no", assistOrder.getOutTradeNo());
        parameters.put(NONCE_STR, SignTextUtils.randomStr());
        if (null == assistOrder.getTransactionType()) {
            throw new PayErrorException(new WxPayError(FAILURE, "微信转账类型必填，详情com.egzosn.pay.wx.bean.WxTransferType"));
        }
        //如果类型为余额方式
        if (TRANSFERS == assistOrder.getTransactionType() || GETTRANSFERINFO == assistOrder.getTransactionType()) {
            parameters.put(APPID, payConfigStorage.getAppId());
            parameters.put(SIGN, createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));
            return getHttpRequestTemplate().postForObject(getReqUrl(GETTRANSFERINFO), XML.getMap2Xml(parameters), JSONObject.class);
        }
        parameters.put(SIGN, createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));
        //默认查询银行卡的记录
        return getHttpRequestTemplate().postForObject(getReqUrl(QUERY_BANK), XML.getMap2Xml(parameters), JSONObject.class);
    }

    private String keyPublic(String content) {
        try {
            return RSA2.encrypt(content, payConfigStorage.getKeyPublic(), CIPHER_ALGORITHM, payConfigStorage.getInputCharset());
        }
        catch (GeneralSecurityException | IOException e) {
            throw new PayErrorException(new WxPayError(FAILURE, e.getLocalizedMessage()));
        }
    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return WxPayMessage.create(message);
    }

    /**
     * 微信发红包
     *
     * @param redpackOrder 红包实体
     * @return 返回发红包实体后的结果
     * @author faymanwang 1057438332@qq.com
     */
    @Override
    public Map<String, Object> sendredpack(RedpackOrder redpackOrder) {
        return sendRedPack(redpackOrder);
    }

    /**
     * 微信发红包
     *
     * @param redpackOrder 红包实体
     * @return 返回发红包实体后的结果
     * @author faymanwang 1057438332@qq.com
     */
    @Override
    public Map<String, Object> sendRedPack(RedpackOrder redpackOrder) {
        Map<String, Object> parameters = new TreeMap<>();
        redPackParam(redpackOrder, parameters);
        final TransferType transferType = redpackOrder.getTransferType();
        if (WxSendredpackType.SENDGROUPREDPACK == transferType) {
            //现金红包，小程序红包默认传1.裂变红包取传入值，且需要大于3
            parameters.put("total_num", Math.max(redpackOrder.getTotalNum(), 3));
            parameters.put("amt_type", "ALL_RAND");
        }
        else if (WxSendredpackType.SENDMINIPROGRAMHB == transferType) {
            parameters.put("notify_way", "MINI_PROGRAM_JSAPI");
        }

        parameters.put(SIGN, createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));
        final JSONObject resp = requestTemplate.postForObject(getReqUrl(redpackOrder.getTransferType()), XML.getMap2Xml(parameters), JSONObject.class);
        if (WxSendredpackType.SENDMINIPROGRAMHB != transferType || FAIL.equals(resp.getString(RESULT_CODE))) {
            return resp;
        }
        Map<String, Object> params = new TreeMap<>();
        params.put("appId", payConfigStorage.getAppId());
        params.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonceStr", parameters.get(NONCE_STR));
        params.put("package", UriVariables.urlEncoder(resp.getString("package")));
        String paySign = createSign(SignTextUtils.parameterText(params), payConfigStorage.getInputCharset());
        params.put("signType", payConfigStorage.getSignType());
        params.put("paySign", paySign);
        return params;
    }


    /**
     * 查询红包记录
     * 用于商户对已发放的红包进行查询红包的具体信息，可支持普通红包和裂变包
     * 查询红包记录API只支持查询30天内的红包订单，30天之前的红包订单请登录商户平台查询。
     *
     * @param mchBillno 商户发放红包的商户订单号
     * @return 返回查询结果
     * @author faymanwang 1057438332@qq.com
     */
    @Override
    public Map<String, Object> gethbinfo(String mchBillno) {
        return getHbInfo(mchBillno);
    }

    /**
     * 查询红包记录
     * 用于商户对已发放的红包进行查询红包的具体信息，可支持普通红包和裂变包
     * 查询红包记录API只支持查询30天内的红包订单，30天之前的红包订单请登录商户平台查询。
     *
     * @param mchBillNo 商户发放红包的商户订单号
     * @return 返回查询结果
     * @author faymanwang 1057438332@qq.com
     */
    @Override
    public Map<String, Object> getHbInfo(String mchBillNo) {
        Map<String, Object> parameters = this.getPublicParameters();
        parameters.put("mch_billno", mchBillNo);
        parameters.put("bill_type", "MCHT");
        parameters.put(SIGN, createSign(SignTextUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));
        return requestTemplate.postForObject(getReqUrl(WxSendredpackType.GETHBINFO), XML.getMap2Xml(parameters), JSONObject.class);
    }

    /**
     * 微信红包构造参数方法
     *
     * @param redpackOrder 红包实体
     * @param parameters   接收参数
     */
    private void redPackParam(RedpackOrder redpackOrder, Map<String, Object> parameters) {
        parameters.put(NONCE_STR, SignTextUtils.randomStr());
        parameters.put(MCH_ID, payConfigStorage.getPid());
        if (StringUtils.isEmpty(redpackOrder.getWxAppId())) {
            throw new PayErrorException(new WxPayError(FAIL, "RedpackOrder#getWxAppId()公众账号appid 必填"));
        }
        parameters.put("wxappid", redpackOrder.getWxAppId());
        parameters.put("send_name", redpackOrder.getSendName());
        parameters.put("re_openid", redpackOrder.getReOpenid());
        parameters.put("mch_billno", redpackOrder.getMchBillNo());
        parameters.put("total_amount", Util.conversionCentAmount(redpackOrder.getTotalAmount()));
        parameters.put("total_num", 1);
        parameters.put("wishing", redpackOrder.getWishing());
        parameters.put("client_ip", StringUtils.isNotEmpty(redpackOrder.getIp()) ? redpackOrder.getIp() : "192.168.0.1");
        parameters.put("act_name", redpackOrder.getActName());
        parameters.put("remark", redpackOrder.getRemark());
        if (StringUtils.isNotEmpty(redpackOrder.getSceneId())) {
            parameters.put("scene_id", redpackOrder.getSceneId());
        }
    }
}
