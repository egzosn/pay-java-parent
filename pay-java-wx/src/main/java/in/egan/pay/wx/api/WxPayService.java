package in.egan.pay.wx.api;

import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.api.BasePayService;
import in.egan.pay.common.api.Callback;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.TransactionType;
import in.egan.pay.common.bean.result.PayException;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.http.ClientHttpRequest;
import in.egan.pay.common.http.HttpConfigStorage;
import in.egan.pay.common.util.MatrixToImageWriter;
import in.egan.pay.common.util.sign.SignUtils;
import in.egan.pay.wx.bean.WxPayError;
import in.egan.pay.wx.bean.WxTransactionType;
import in.egan.pay.common.util.XML;
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
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class WxPayService extends BasePayService {
    protected final Log log = LogFactory.getLog(WxPayService.class);




    public final static String httpsVerifyUrl = "https://gw.tenpay.com/gateway";
    public final static String uri = "https://api.mch.weixin.qq.com/";
//    public final static String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //    public final static String orderqueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";



    public WxPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public WxPayService(PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     *  微信支付V2版本所需
     *  当前版本不需要  ?
     * @return
     */
    public String getHttpsVerifyUrl() {
        return httpsVerifyUrl + "/verifynotifyid.xml";
    }

    private String getUrl(TransactionType transactionType){

        return uri + transactionType.getMethod();
    }

    @Override
    public boolean verify(Map<String, String> params) {
        if (!"SUCCESS".equals(params.get("return_code"))){
            log.debug(String.format("微信支付异常：return_code=%s,参数集=%s", params.get("return_code"), params));
            return false;
        }

        if(null == params.get("sign")) {
            log.debug("微信支付异常：签名为空！out_trade_no=" + params.get("out_trade_no"));
            return false;
        }

        try {
            return signVerify(params, params.get("sign")) && verifySource(params.get("out_trade_no"));
        } catch (PayErrorException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 支付宝需要,微信是否也需要再次校验来源，进行订单查询
     * @param id 商户单号
     * @return
     */
    @Override
    public boolean verifySource(String id) {
        return true;
    }


    /**
     * 根据反馈回来的信息，生成签名结果
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public boolean signVerify(Map<String, String> params, String sign) {
       return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params,  sign, "&key=" +  payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }

    /**
     * 获取公共参数
     * @return
     */
    private Map<String,Object> getPublicParameters() {

        Map<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("mch_id", payConfigStorage.getPartner());
        parameters.put("nonce_str", SignUtils.randomStr());
        return parameters;


    }



    /**
     * 获取支付平台所需的订单信息
     *
     * @param order 支付订单
     * @return
     * @see PayOrder
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {

        ////统一下单
        Map<String, Object> parameters = getPublicParameters();
     /*   parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("mch_id", payConfigStorage.getPartner());
        parameters.put("nonce_str", SignUtils.randomStr());*/
        parameters.put("body", order.getSubject());// 购买支付信息
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("out_trade_no", order.getOutTradeNo());// 订单号
        parameters.put("spbill_create_ip", "192.168.1.150");
        parameters.put("total_fee", order.getPrice().multiply(new BigDecimal(100)).intValue());// 总金额单位为分
        parameters.put("trade_type", order.getTransactionType().getType());
        parameters.put("attach", order.getBody());
        if (WxTransactionType.NATIVE == order.getTransactionType()){
            parameters.put("product_id",  order.getOutTradeNo());
        }
        String sign = createSign(SignUtils.parameterText(parameters), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);

       String requestXML = XML.getMap2Xml(parameters);
        log.debug("requestXML：" + requestXML);
        /////////APP端调起支付的参数列表
        JSONObject result = requestTemplate.postForObject(getUrl(order.getTransactionType()), requestXML, JSONObject.class);

        if (!"SUCCESS".equals(result.get("return_code"))){
            throw new PayErrorException(new WxPayError(result.getString("return_code"),  result.getString("return_msg"), result.toJSONString()));
        }
        //如果是扫码支付无需处理，直接返回
        if (WxTransactionType.NATIVE == order.getTransactionType()){
            return result;
        }

        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", payConfigStorage.getAppid());
        params.put("partnerid", payConfigStorage.getPid());
        params.put("prepayid", result.get("prepay_id"));
        params.put("timestamp", System.currentTimeMillis() / 1000);
        params.put("noncestr", result.get("nonce_str"));

        if (WxTransactionType.JSAPI == order.getTransactionType()){
            params.put("package", "prepay_id=" + result.get("prepay_id"));
            params.put("signType", payConfigStorage.getSignType());
        }else  if (WxTransactionType.APP == order.getTransactionType()){
            params.put("package", "Sign=WXPay");
        }
        String paySign = createSign(SignUtils.parameterText(params), payConfigStorage.getInputCharset());
        params.put("sign", paySign);
        return params;


    }

    /**
     *  生成并设置签名
     * @param parameters 请求参数
     * @return
     */
    private Map<String, Object> setSign(Map<String, Object> parameters){
        parameters.put("sign_type", payConfigStorage.getSignType());
        String sign = createSign( SignUtils.parameterText(parameters, "&", "sign", "appId"), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);
        return parameters;
    }

    /**
     * 签名
     * @param content 需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @return
     */
    @Override
    public String createSign(String content, String characterEncoding) {
       return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "&key=" + payConfigStorage.getKeyPrivate(), characterEncoding).toUpperCase();
    }

    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
        TreeMap<String, String> map = new TreeMap();
        try {
            return  XML.inputStream2Map(is, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.XML().code(code.toUpperCase()).content(message).build();
    }

    /**
     * 针对web端的即时付款
     *  暂未实现或无此功能
     * @param orderInfo 发起支付的订单信息
     * @param method 请求方式  "post" "get",
     * @return
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BufferedImage genQrPay(Map<String, Object> orderInfo) {
        //获取对应的支付账户操作工具（可根据账户id）
        if (!"SUCCESS".equals(orderInfo.get("result_code"))) {
            throw new PayErrorException(new WxPayError("-1", (String) orderInfo.get("err_code")));
        }


        return  MatrixToImageWriter.writeInfoToJpgBuff((String) orderInfo.get("code_url"));
    }

    /**
     *  交易查询接口
     * @param transactionId 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    @Override
    public Map<String, Object> query(String transactionId, String outTradeNo) {

        return  query(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     *
     * @param transactionId    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T>
     * @return
     */
    @Override
    public <T> T query(String transactionId, String outTradeNo, Callback<T> callback) {

        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.QUERY, callback);
    }


    @Override
    public Map<String, Object> close(String transactionId, String outTradeNo) {

        return  close(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    @Override
    public <T> T close(String transactionId, String outTradeNo, Callback<T> callback) {
        return  secondaryInterface(transactionId, outTradeNo, WxTransactionType.CLOSE, callback);
    }

    /**
     * 退款
     * @param transactionId 微信订单号
     * @param outTradeNo 商户单号
     * @param refundAmount 退款金额
     * @param totalAmount 总金额
     * @return
     */
    @Override
    public Map<String, Object> refund(String transactionId, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {

        return  refund(transactionId, outTradeNo, refundAmount, totalAmount, new Callback<Map<String, Object>>() {
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
     * @param <T>
     * @return
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
        parameters.put("total_fee", totalAmount.multiply(new BigDecimal(100)).intValue());
        parameters.put("refund_fee", refundAmount.multiply(new BigDecimal(100)).intValue());
        parameters.put("op_user_id", payConfigStorage.getPid());

        //设置签名
        setSign(parameters);
        return callback.perform(requestTemplate.postForObject(getUrl(WxTransactionType.REFUND), XML.getMap2Xml(parameters), JSONObject.class));
    }

    @Override
    public Map<String, Object> refundquery(String transactionId, String outTradeNo) {
        return  refundquery(transactionId, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    @Override
    public <T> T refundquery(String transactionId, String outTradeNo, Callback<T> callback) {
        return secondaryInterface(transactionId, outTradeNo, WxTransactionType.REFUNDQUERY, callback);
    }

    /**
     * 目前只支持日账单
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @return
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return  downloadbill(billDate, billType, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     *  目前只支持日账单
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @param <T>
     * @return
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
        return callback.perform(requestTemplate.postForObject(getUrl(WxTransactionType.DOWNLOADBILL),  XML.getMap2Xml(parameters), JSONObject.class));
    }

    /**
     *
     * @param transactionIdOrBillDate 支付平台订单号或者账单类型， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link in.egan.pay.common.exception.PayErrorException}
     * @param outTradeNoBillType  商户单号或者 账单类型
     * @param transactionType 交易类型
     * @param callback 处理器
     * @param <T>
     * @return
     */
    @Override
    public <T> T secondaryInterface(Object transactionIdOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {

        if (transactionType == WxTransactionType.REFUND){
            throw new PayErrorException(new PayException("failure", "通用接口不支持:" + transactionType));
        }
        
        
        if (transactionType == WxTransactionType.DOWNLOADBILL){
            if (transactionIdOrBillDate instanceof  Date){
                return downloadbill((Date) transactionIdOrBillDate, outTradeNoBillType, callback);
            }
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        if (!(transactionIdOrBillDate instanceof  String)){
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + transactionIdOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters();
        if (null != transactionIdOrBillDate){
            parameters.put("transaction_id", transactionIdOrBillDate);
        }else {
            parameters.put("out_trade_no", outTradeNoBillType);
        }
        //设置签名
        setSign(parameters);
        return  callback.perform(requestTemplate.postForObject(getUrl(transactionType), XML.getMap2Xml(parameters) , JSONObject.class));
    }



}
