package com.egzosn.pay.wx.api;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.MatrixToImageWriter;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.bean.WxTransactionType;
import com.egzosn.pay.common.util.XML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信支付服务
 *
 * @author egan
 *         <pre>
 *         email egzosn@gmail.com
 *         date 2016-5-18 14:09:01
 *         </pre>
 */
public class WxPayService extends BasePayService {
    protected final Log LOG = LogFactory.getLog(WxPayService.class);


    /**
     * 微信请求地址
     */
    public final static String URI = "https://api.mch.weixin.qq.com/";
//    public final static String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //    public final static String orderqueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";


    /**
     * 创建支付服务
     * @param payConfigStorage 微信对应的支付配置
     */
    public WxPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }
    /**
     * 创建支付服务
     * @param payConfigStorage 微信对应的支付配置
     * @param configStorage 微信对应的网络配置，包含代理配置、ssl证书配置
     */
    public WxPayService(PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     * 根据交易类型获取url
     *
     * @param transactionType 交易类型
     * @return 请求url
     */
    private String getUrl(TransactionType transactionType) {

        return URI + transactionType.getMethod();
    }

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        if (!"SUCCESS".equals(params.get("return_code"))){
            LOG.debug(String.format("微信支付异常：return_code=%s,参数集=%s", params.get("return_code"), params));
            return false;
        }

        if(null == params.get("sign")) {
            LOG.debug("微信支付异常：签名为空！out_trade_no=" + params.get("out_trade_no"));
            return false;
        }

        try {
            return signVerify(params, (String) params.get("sign")) && verifySource((String) params.get("out_trade_no"));
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
        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params, sign, "&key=" + payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    private Map<String, Object> getPublicParameters() {

        Map<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("mch_id", payConfigStorage.getPid());
        parameters.put("nonce_str", SignUtils.randomStr());
        return parameters;


    }


    /**
     * 微信统一下单接口
     *
     * @param order 支付订单集
     * @return 下单结果
     */
    public JSONObject unifiedOrder(PayOrder order) {

        ////统一下单
        Map<String, Object> parameters = getPublicParameters();

        parameters.put("body", order.getSubject());// 购买支付信息
        parameters.put("out_trade_no", order.getOutTradeNo());// 订单号
        parameters.put("spbill_create_ip", "192.168.1.150");
        parameters.put("total_fee", order.getPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());// 总金额单位为分

        parameters.put("attach", order.getBody());
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("trade_type", order.getTransactionType().getType());

        switch ((WxTransactionType) order.getTransactionType()) {
            //刷卡付
            case MICROPAY:
                parameters.put("auth_code", order.getAuthCode());
                parameters.remove("notify_url");
                parameters.remove("trade_type");
                break;
            // 公众号支付
            case JSAPI:
                parameters.put("openid", order.getOpenid());
                break;
            //二维码
            case NATIVE:
                parameters.put("product_id", order.getOutTradeNo());
                break;
            case MWEB:
                //H5支付专用
                LinkedHashMap value = new LinkedHashMap();
                value.put("type", "Wap");
                value.put("wap_url", order.getWapUrl());////WAP网站URL地址
                value.put("wap_name", order.getWapName());//WAP 网站名
                JSONObject sceneInfo = new JSONObject();
                sceneInfo.put("h5_info", value);
                parameters.put("scene_info", sceneInfo.toJSONString());
                break;
            default:
        }


        String sign = createSign(SignUtils.parameterText(parameters), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);

        String requestXML = XML.getMap2Xml(parameters);
        LOG.debug("requestXML：" + requestXML);
        //调起支付的参数列表
        JSONObject result = requestTemplate.postForObject(getUrl(order.getTransactionType()), requestXML, JSONObject.class);

        if (!"SUCCESS".equals(result.get("return_code"))) {
            throw new PayErrorException(new WxPayError(result.getString("return_code"), result.getString("return_msg"), result.toJSONString()));
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

        //如果是扫码支付或者刷卡付无需处理，直接返回
        if (WxTransactionType.NATIVE == order.getTransactionType() || WxTransactionType.MICROPAY == order.getTransactionType() || WxTransactionType.MWEB == order.getTransactionType()) {
            return result;
        }

        SortedMap<String, Object> params = new TreeMap<String, Object>();

        params.put("package", "prepay_id=" + result.get("prepay_id"));
        if (WxTransactionType.JSAPI == order.getTransactionType()) {
            params.put("signType", payConfigStorage.getSignType());
            params.put("appId", payConfigStorage.getAppid());
            params.put("timeStamp", System.currentTimeMillis() / 1000);
            params.put("nonceStr", result.get("nonce_str"));
        } else if (WxTransactionType.APP == order.getTransactionType()) {
            params.put("partnerid", payConfigStorage.getPid());
            params.put("appid", payConfigStorage.getAppid());
            params.put("prepayid", result.get("prepay_id"));
            params.put("timestamp", System.currentTimeMillis() / 1000);
            params.put("noncestr", result.get("nonce_str"));
        }
        String paySign = createSign(SignUtils.parameterText(params), payConfigStorage.getInputCharset());
        params.put("sign", paySign);
        return params;


    }

    /**
     * 生成并设置签名
     *
     * @param parameters 请求参数
     * @return 请求参数
     */
    private Map<String, Object> setSign(Map<String, Object> parameters) {
        parameters.put("sign_type", payConfigStorage.getSignType());
        String sign = createSign(SignUtils.parameterText(parameters, "&", "sign", "appId"), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);
        return parameters;
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
        return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "&key=" + payConfigStorage.getKeyPrivate(), characterEncoding).toUpperCase();
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
        if (!"SUCCESS".equals(orderInfo.get("return_code"))) {
            throw new PayErrorException(new WxPayError((String) orderInfo.get("return_code"), (String) orderInfo.get("return_msg")));
        }
        if (WxTransactionType.MWEB.name().equals(orderInfo.get("trade_type"))) {
            return String.format("<script type=\"text/javascript\">location.href=\"%s%s;\"</script>",orderInfo.get("mweb_url"), StringUtils.isEmpty(payConfigStorage.getReturnUrl()) ? "" : "&redirect_url=" + payConfigStorage.getReturnUrl());
        }
        throw new UnsupportedOperationException();

    }

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public BufferedImage genQrPay(PayOrder order) {
        Map<String, Object> orderInfo = orderInfo(order);
        //获取对应的支付账户操作工具（可根据账户id）
        if (!"SUCCESS".equals(orderInfo.get("result_code"))) {
            throw new PayErrorException(new WxPayError("-1", (String) orderInfo.get("err_code")));
        }


        return MatrixToImageWriter.writeInfoToJpgBuff((String) orderInfo.get("code_url"));
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

        return query(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @param callback      处理器
     * @param <T>           返回类型
     * @return 处理过后的类型对象，返回查询回来的结果集，支付方原值返回
     */
    @Override
    public <T> T query(String transactionId, String outTradeNo, Callback<T> callback) {

        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.QUERY, callback);
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

        return close(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     * 交易关闭接口
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @param callback      处理器
     * @param <T>           返回类型
     * @return 处理过后的类型对象，返回支付方交易关闭后的结果
     */
    @Override
    public <T> T close(String transactionId, String outTradeNo, Callback<T> callback) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.CLOSE, callback);
    }

    /**
     * 退款
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户单号
     * @param refundAmount  退款金额
     * @param totalAmount   总金额
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund(String transactionId, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {

        return refund(transactionId, outTradeNo, refundAmount, totalAmount, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     * 退款
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户单号
     * @param refundAmount  退款金额
     * @param totalAmount   总金额
     * @param callback      处理器
     * @param <T>           返回类型
     * @return 处理过后的类型对象， 返回支付方申请退款后的结果
     */
    @Override
    public <T> T refund(String transactionId, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        if (null != transactionId) {
            parameters.put("transaction_id", transactionId);
            parameters.put("out_refund_no", transactionId);
        } else {
            parameters.put("out_trade_no", outTradeNo);
            parameters.put("out_refund_no", outTradeNo);
        }
        parameters.put("total_fee", totalAmount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        parameters.put("refund_fee", refundAmount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        parameters.put("op_user_id", payConfigStorage.getPid());

        //设置签名
        setSign(parameters);
        return callback.perform(requestTemplate.postForObject(getUrl(WxTransactionType.REFUND), XML.getMap2Xml(parameters), JSONObject.class));
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
        return refundquery(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     * 查询退款
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @param callback      处理器
     * @param <T>           返回类型
     * @return 处理过后的类型对象，返回支付方查询退款后的结果
     */
    @Override
    public <T> T refundquery(String transactionId, String outTradeNo, Callback<T> callback) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.REFUNDQUERY, callback);
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
        return downloadbill(billDate, billType, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     * 目前只支持日账单
     *
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @param <T>      返回类型
     * @return 处理过后的类型对象，返回支付方下载对账单的结果
     */
    @Override
    public <T> T downloadbill(Date billDate, String billType, Callback<T> callback) {

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();

        Map<String, Object> bizContent = new TreeMap<>();
        bizContent.put("bill_type", billType);
        //目前只支持日账单
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        bizContent.put("bill_date", df.format(billDate));

        //设置签名
        setSign(parameters);
        return callback.perform(requestTemplate.postForObject(getUrl(WxTransactionType.DOWNLOADBILL), XML.getMap2Xml(parameters), JSONObject.class));
    }

    /**
     * @param transactionIdOrBillDate 支付平台订单号或者账单类型， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType      商户单号或者 账单类型
     * @param transactionType         交易类型
     * @param callback                处理器
     * @param <T>                     返回类型
     * @return 返回支付方对应接口的结果
     */
    @Override
    public  <T> T secondaryInterface(Object transactionIdOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {

        if (transactionType == WxTransactionType.REFUND) {
            throw new PayErrorException(new PayException("failure", "通用接口不支持:" + transactionType));
        }


        if (transactionType == WxTransactionType.DOWNLOADBILL){
            if (transactionIdOrBillDate instanceof  Date){
                return downloadbill((Date) transactionIdOrBillDate, outTradeNoBillType, callback);
            }
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        if (!(null == transactionIdOrBillDate || transactionIdOrBillDate instanceof  String)){
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        if (StringUtils.isEmpty((String)transactionIdOrBillDate)){
            parameters.put("out_trade_no", outTradeNoBillType);
        }else {
            parameters.put("transaction_id", transactionIdOrBillDate);
        }
        //设置签名
        setSign(parameters);
        return  callback.perform(requestTemplate.postForObject(getUrl(transactionType), XML.getMap2Xml(parameters) , JSONObject.class));
    }


}
