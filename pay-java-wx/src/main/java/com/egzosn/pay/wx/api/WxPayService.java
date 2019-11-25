package com.egzosn.pay.wx.api;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.MatrixToImageWriter;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.sign.encrypt.RSA2;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.bean.WxPayMessage;
import com.egzosn.pay.wx.bean.WxTransactionType;
import com.egzosn.pay.common.util.XML;
import com.egzosn.pay.wx.bean.WxTransferType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.egzosn.pay.wx.bean.WxTransferType.*;

/**
 * 微信支付服务
 *
 * @author egan
 *         <pre>
 *                 email egzosn@gmail.com
 *                 date 2016-5-18 14:09:01
 *                 </pre>
 */
public class WxPayService extends BasePayService<WxPayConfigStorage> {


    /**
     * 微信请求地址
     */
    public static final String URI = "https://api.mch.weixin.qq.com/";
    /**
     * 沙箱
     */
    public static final String SANDBOXNEW = "sandboxnew/";

    public static final String SUCCESS = "SUCCESS";
    public static final String RETURN_CODE = "return_code";
    public static final String SIGN = "sign";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
    public static final String FAILURE = "failure";
    public static final String APPID = "appid";
    private static final String HMAC_SHA256 = "HMAC-SHA256";
    private static final String HMACSHA256 = "HMACSHA256";
    private static final String RETURN_MSG_CODE = "return_msg";
    private static final String RESULT_CODE = "result_code";
    private static final String MCH_ID = "mch_id";
    private static final String NONCE_STR = "nonce_str";


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
    @Override
    public boolean verify(Map<String, Object> params) {

        if (!(SUCCESS.equals(params.get(RETURN_CODE)) && SUCCESS.equals(params.get(RESULT_CODE)))) {
            LOG.debug(String.format("微信支付异常：return_code=%s,参数集=%s", params.get(RETURN_CODE), params));
            return false;
        }

        if (null == params.get(SIGN)) {
            LOG.debug("微信支付异常：签名为空！out_trade_no=" + params.get("out_trade_no"));
            return false;
        }

        try {
            return signVerify(params, (String) params.get(SIGN)) && verifySource((String) params.get("out_trade_no"));
        } catch (PayErrorException e) {
            LOG.error(e);
        }
        return false;
    }


    /**
     * 微信是否也需要再次校验来源，进行订单查询
     *
     * @param id 商户单号
     * @return true通过
     */
    @Override
    public boolean verifySource(String id) {
        return true;
    }


    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @return 生成的签名结果
     */
    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        return signVerify(params, sign, payConfigStorage.isTest());
    }

    private boolean signVerify(Map<String, Object> params, String sign, boolean isTest) {
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());
        String keyPrivate = payConfigStorage.getKeyPrivate();
        if (isTest) {
            keyPrivate = getKeyPrivate();
        }
        String content = SignUtils.parameterText(params, "&", SIGN, "appId") + "&key=" + (signUtils == SignUtils.MD5 ? "" : keyPrivate);
        return signUtils.verify(content, sign, keyPrivate, payConfigStorage.getInputCharset());
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, Object> getPublicParameters() {

        Map<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put(APPID, payConfigStorage.getAppid());
        parameters.put(MCH_ID, payConfigStorage.getMchId());
        //判断如果是服务商模式信息则加入
        if (!StringUtils.isEmpty(payConfigStorage.getSubMchId())) {
            parameters.put("sub_mch_id", payConfigStorage.getSubMchId());
        }
        if (!StringUtils.isEmpty(payConfigStorage.getSubAppid())) {
            parameters.put("sub_appid", payConfigStorage.getSubAppid());
        }
        parameters.put(NONCE_STR, SignUtils.randomStr());
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
//        parameters.put("detail", order.getBody());
        // 订单号
        parameters.put("out_trade_no", order.getOutTradeNo());
        parameters.put("spbill_create_ip", StringUtils.isEmpty(order.getSpbillCreateIp()) ? "192.168.1.150" : order.getSpbillCreateIp());
        // 总金额单位为分
        parameters.put("total_fee", Util.conversionCentAmount(order.getPrice()));
        if (StringUtils.isNotEmpty(order.getAddition())) {
            parameters.put("attach", order.getAddition());
        }
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("trade_type", order.getTransactionType().getType());
        if (null != order.getExpirationTime()) {
            parameters.put("time_start", DateUtils.formatDate(new Date(), DateUtils.YYYYMMDDHHMMSS));
            parameters.put("time_expire", DateUtils.formatDate(order.getExpirationTime(), DateUtils.YYYYMMDDHHMMSS));
        }
        ((WxTransactionType) order.getTransactionType()).setAttribute(parameters, order);
        parameters =  preOrderHandler(parameters, order);
        setSign(parameters);

        String requestXML = XML.getMap2Xml(parameters);
        if (LOG.isDebugEnabled()) {
            LOG.debug("requestXML：" + requestXML);
        }

        //调起支付的参数列表
        JSONObject result = requestTemplate.postForObject(getReqUrl(order.getTransactionType()), requestXML, JSONObject.class);

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
        if (verify(preOrderHandler(result, order))) {
            //如果是扫码支付或者刷卡付无需处理，直接返回
            if (((WxTransactionType) order.getTransactionType()).isReturn()) {
                return result;
            }

            Map<String, Object> params = new TreeMap<String, Object>();

            if (WxTransactionType.JSAPI == order.getTransactionType()) {
                params.put("signType", payConfigStorage.getSignType());
                params.put("appId", payConfigStorage.getAppid());
                params.put("timeStamp", System.currentTimeMillis() / 1000);
                params.put("nonceStr", result.get(NONCE_STR));
                params.put("package", "prepay_id=" + result.get("prepay_id"));
            } else if (WxTransactionType.APP == order.getTransactionType()) {
                params.put("partnerid", payConfigStorage.getPid());
                params.put(APPID, payConfigStorage.getAppid());
                params.put("prepayid", result.get("prepay_id"));
                params.put("timestamp", System.currentTimeMillis() / 1000);
                params.put("noncestr", result.get(NONCE_STR));
                params.put("package", "Sign=WXPay");
            }
            params =  preOrderHandler(params, order);
            String paySign = createSign(SignUtils.parameterText(params), payConfigStorage.getInputCharset());
            params.put(SIGN, paySign);
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
        String signType = payConfigStorage.getSignType();
        if (HMACSHA256.equals(signType)) {
            signType = HMAC_SHA256;
        }
        parameters.put("sign_type", signType);
        String sign = createSign(SignUtils.parameterText(parameters, "&", SIGN, "appId"), payConfigStorage.getInputCharset());
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
        parameters.put(NONCE_STR, SignUtils.randomStr());

        String sign = createSign(SignUtils.parameterText(parameters, "&", SIGN, "appId"), payConfigStorage.getInputCharset(), false);
        parameters.put(SIGN, sign);

        JSONObject result = requestTemplate.postForObject(getReqUrl(WxTransactionType.GETSIGNKEY), XML.getMap2Xml(parameters), JSONObject.class);
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
     * @param test 是否为沙箱环境
     * @return 签名结果
     */
    public String createSign(String content, String characterEncoding, boolean test) {
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase());
        String keyPrivate = payConfigStorage.getKeyPrivate();
        if (test){
            keyPrivate = getKeyPrivate();
        }
        return signUtils.createSign(content + "&key=" + (signUtils == SignUtils.MD5 ? "" : keyPrivate), keyPrivate, characterEncoding).toUpperCase();
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
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        try {
            return XML.inputStream2Map(is, map);
        } catch (IOException e) {
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
        return PayOutMessage.XML().code("Success").content("成功").build();
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
    public String getQrPay(PayOrder order){
        Map<String, Object> orderInfo = orderInfo(order);
        //获取对应的支付账户操作工具（可根据账户id）
        if (!SUCCESS.equals(orderInfo.get(RESULT_CODE))) {
            throw new PayErrorException(new WxPayError((String)orderInfo.get("err_code"), orderInfo.toString()));
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

    /**
     * 退款
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户单号
     * @param refundAmount  退款金额
     * @param totalAmount   总金额
     * @return 返回支付方申请退款后的结果
     * @see #refund(RefundOrder, Callback)
     */
    @Deprecated
    @Override
    public Map<String, Object> refund(String transactionId, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {

        return refund(new RefundOrder(transactionId, outTradeNo, refundAmount, totalAmount));
    }


    private Map<String, Object> setParameters(Map<String, Object> parameters, String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            parameters.put(key, value);
        }
        return parameters;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();

        setParameters(parameters, "transaction_id", refundOrder.getTradeNo());
        setParameters(parameters, "out_trade_no", refundOrder.getOutTradeNo());
        setParameters(parameters, "out_refund_no", refundOrder.getRefundNo());
        parameters.put("total_fee", Util.conversionCentAmount(refundOrder.getTotalAmount()));
        parameters.put("refund_fee", Util.conversionCentAmount(refundOrder.getRefundAmount()));
        parameters.put("op_user_id", payConfigStorage.getPid());
        setParameters(parameters, "notify_url",  payConfigStorage.getNotifyUrl());

        //设置签名
        setSign(parameters);
        return requestTemplate.postForObject(getReqUrl(WxTransactionType.REFUND), XML.getMap2Xml(parameters), JSONObject.class);
    }


    /**
     * 查询退款
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(String transactionId, String outTradeNo) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.REFUNDQUERY);
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
        setParameters(parameters, "transaction_id", refundOrder.getTradeNo());
        setParameters(parameters, "out_trade_no", refundOrder.getOutTradeNo());
        setParameters(parameters, "out_refund_no", refundOrder.getRefundNo());
        //设置签名
        setSign(parameters);
        return  requestTemplate.postForObject(getReqUrl( WxTransactionType.REFUNDQUERY), XML.getMap2Xml(parameters) , JSONObject.class);
    }


    /**
     * 目前只支持日账单
     *
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();

        parameters.put("bill_type", billType);
        //目前只支持日账单

        parameters.put("bill_date", DateUtils.formatDate(billDate, DateUtils.YYYYMMDD));

        //设置签名
        setSign(parameters);
        String respStr = requestTemplate.postForObject(getReqUrl(WxTransactionType.DOWNLOADBILL), XML.getMap2Xml(parameters), String.class);
        if (respStr.indexOf("<") == 0) {
            return XML.toJSONObject(respStr);
        }

        Map<String, Object> ret = new HashMap<String, Object>(3);
        ret.put(RETURN_CODE, SUCCESS);
        ret.put(RETURN_MSG_CODE, "ok");
        ret.put("data", respStr);
        return ret;
    }


    /**
     * @param transactionIdOrBillDate 支付平台订单号或者账单类型， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType      商户单号或者 账单类型
     * @param transactionType         交易类型
     * @return 返回支付方对应接口的结果
     */
    @Override
    public Map<String, Object> secondaryInterface(Object transactionIdOrBillDate, String outTradeNoBillType, TransactionType transactionType) {

        if (transactionType == WxTransactionType.REFUND) {
            throw new PayErrorException(new PayException(FAILURE, "通用接口不支持:" + transactionType));
        }

        if (transactionType == WxTransactionType.DOWNLOADBILL) {
            if (transactionIdOrBillDate instanceof Date) {
                return downloadbill((Date) transactionIdOrBillDate, outTradeNoBillType);
            }
            throw new PayErrorException(new PayException(FAILURE, "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        if (!(null == transactionIdOrBillDate || transactionIdOrBillDate instanceof String)) {
            throw new PayErrorException(new PayException(FAILURE, "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        if (StringUtils.isEmpty((String) transactionIdOrBillDate)) {
            parameters.put("out_trade_no", outTradeNoBillType);
        } else {
            parameters.put("transaction_id", transactionIdOrBillDate);
        }
        //设置签名
        setSign(parameters);
        return  requestTemplate.postForObject(getReqUrl(transactionType), XML.getMap2Xml(parameters) , JSONObject.class);
    }

    /**
     * 转账
     *
     * @param order 转账订单
     *              <pre>
     *
     *              注意事项：
     *              ◆ 当返回错误码为“SYSTEMERROR”时，请不要更换商户订单号，一定要使用原商户订单号重试，否则可能造成重复支付等资金风险。
     *              ◆ XML具有可扩展性，因此返回参数可能会有新增，而且顺序可能不完全遵循此文档规范，如果在解析回包的时候发生错误，请商户务必不要换单重试，请商户联系客服确认付款情况。如果有新回包字段，会更新到此API文档中。
     *              ◆ 因为错误代码字段err_code的值后续可能会增加，所以商户如果遇到回包返回新的错误码，请商户务必不要换单重试，请商户联系客服确认付款情况。如果有新的错误码，会更新到此API文档中。
     *              ◆ 错误代码描述字段err_code_des只供人工定位问题时做参考，系统实现时请不要依赖这个字段来做自动化处理。
     *
     *              </pre>
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        Map<String, Object> parameters = new TreeMap<String, Object>();


        parameters.put("partner_trade_no", order.getOutNo());
        parameters.put("amount", Util.conversionCentAmount(order.getAmount()));
        if (!StringUtils.isEmpty(order.getRemark())) {
            parameters.put("desc", order.getRemark());
        }
        parameters.put(NONCE_STR, SignUtils.randomStr());
        if (null != order.getTransferType() && TRANSFERS == order.getTransferType()) {
            transfers(parameters, order);
            parameters.put("mchid", payConfigStorage.getPid());
        } else {
            parameters.put(MCH_ID, payConfigStorage.getPid());
            order.setTransferType(WxTransferType.PAY_BANK);
            payBank(parameters, order);
        }
        parameters.put(SIGN, createSign(SignUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));

        return getHttpRequestTemplate().postForObject(getReqUrl(order.getTransferType()),  XML.getMap2Xml(parameters), JSONObject.class);
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
    public Map<String, Object> transfers(Map<String, Object> parameters, TransferOrder order) {
        //转账到余额, 申请商户号的appid或商户号绑定的appid
        parameters.put("mch_appid", payConfigStorage.getAppid());
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
    public Map<String, Object> payBank(Map<String, Object> parameters, TransferOrder order) {

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
     *                       <p>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">企业付款到零钱</a>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3">商户企业付款到银行卡</a>
     *                       </p>
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(String outNo, String wxTransferType) {
        Map<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put(MCH_ID, payConfigStorage.getPid());
        parameters.put("partner_trade_no", outNo);
        parameters.put(NONCE_STR, SignUtils.randomStr());
        parameters.put(SIGN, createSign(SignUtils.parameterText(parameters, "&", SIGN), payConfigStorage.getInputCharset()));
        if (StringUtils.isEmpty(wxTransferType)) {
            throw new PayErrorException(new WxPayError(FAILURE, "微信转账类型 #transferQuery(String outNo, String wxTransferType) 必填，详情com.egzosn.pay.wx.bean.WxTransferType"));
        }
        //如果类型为余额方式
        if (TRANSFERS.getType().equals(wxTransferType) || GETTRANSFERINFO.getType().equals(wxTransferType)){
            return getHttpRequestTemplate().postForObject(getReqUrl(GETTRANSFERINFO),  XML.getMap2Xml(parameters), JSONObject.class);
        }
        //默认查询银行卡的记录
        return getHttpRequestTemplate().postForObject(getReqUrl(QUERY_BANK),  XML.getMap2Xml(parameters), JSONObject.class);
    }


    public String keyPublic(String content) {
        try {
            return RSA2.encrypt(content, payConfigStorage.getKeyPublic(), CIPHER_ALGORITHM, payConfigStorage.getInputCharset());
        } catch (Exception e) {
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
}
