package com.egzosn.pay.ali.before.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.before.bean.AliTransactionType;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.egzosn.pay.ali.api.AliPayService.SIGN;

/**
 *  支付宝支付服务
 * @author  egan
 *
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 * 旧版本支付服务，2015年之前的支付方式，之后版本请看新类
 * @see com.egzosn.pay.ali.api.AliPayService
 */
@Deprecated
public class AliPayService extends BasePayService<AliPayConfigStorage> {



    private static final String HTTPS_REQ_URL = "https://mapi.alipay.com/gateway.do";
    private static final String QUERY_REQ_URL = "https://openapi.alipay.com/gateway.do";
    public static final String NOTIFY_ID = "notify_id";
    
    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    static {
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }
    public AliPayService(AliPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public AliPayService(AliPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }


    public String getHttpsVerifyUrl() {
        return HTTPS_REQ_URL + "?service=notify_verify";
    }

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {

        if (params.get(SIGN) == null || params.get(NOTIFY_ID) == null) {
            LOG.debug("支付宝支付异常：params：" + params);
            return false;
        }

        try {
            return signVerify(params, (String) params.get(SIGN)) && verifySource((String) params.get(NOTIFY_ID));
        } catch (PayErrorException e) {
            LOG.error(e);
        }

        return false;
    }
    /**
     * 校验数据来源
     *
     * @param id 业务id, 数据的真实性.
     * @return true通过
     */
    @Override
    public boolean verifySource(String id) {
        return "true".equals(requestTemplate.getForObject( getHttpsVerifyUrl() + "&partner=" + payConfigStorage.getPid() + "&notify_id=" + id, String.class));
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {

        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params,  sign,  payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }


    /**
     *  生成并设置签名
     * @param parameters 请求参数
     * @return 订单信息
     */
    private Map<String, Object> setSign(Map<String, Object> parameters){
        parameters.put("sign_type", payConfigStorage.getSignType());
        String sign = createSign(SignUtils.parameterText(parameters, "&",  SIGN, "appId"), payConfigStorage.getInputCharset());
        parameters.put(SIGN, sign);

        return parameters;
    }

    /**
     * 获取公共请求参数
     * @param transactionType 交易类型
     * @return 公共请求参数
     */
    private Map<String, Object> getPublicParameters(TransactionType transactionType ){
        Map<String, Object> orderInfo = new TreeMap<>();
        orderInfo.put("app_id", payConfigStorage.getAppid());
        orderInfo.put("method", transactionType.getMethod());
        orderInfo.put("format", "json");
        orderInfo.put("charset", payConfigStorage.getInputCharset());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderInfo.put("timestamp", df.format(new Date()));
        orderInfo.put("version", "1.0");
        return  orderInfo;
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

        Map<String, Object> orderInfo =  getOrder(order);

        String sign = null;
        if (AliTransactionType.APP == order.getTransactionType() ){
            sign = createSign(getOrderInfo(order), payConfigStorage.getInputCharset());
        }else {
            sign = createSign(orderInfo, payConfigStorage.getInputCharset());
        }

        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        orderInfo.put(SIGN, sign);
        orderInfo.put("sign_type", payConfigStorage.getSignType());
        return orderInfo;
    }

    private String getOrderInfo(PayOrder order) {
        String orderInfo = "partner=\"" + this.payConfigStorage.getPid() + "\"";
        orderInfo = orderInfo + "&seller_id=\"" + this.payConfigStorage.getSeller() + "\"";
        orderInfo = orderInfo + "&out_trade_no=\"" + order.getOutTradeNo() + "\"";
        orderInfo = orderInfo + "&subject=\"" + order.getSubject() + "\"";
        orderInfo = orderInfo + "&body=\"" + order.getBody() + "\"";
        orderInfo = orderInfo + "&total_fee=\"" + Util.conversionAmount(order.getPrice()).toString()  + "\"";
        orderInfo = orderInfo + "&notify_url=\"" + this.payConfigStorage.getNotifyUrl() + "\"";
        orderInfo = orderInfo + "&service=\"mobile.securitypay.pay\"";
        orderInfo = orderInfo + "&payment_type=\"1\"";
        orderInfo = orderInfo + "&_input_charset=\""+ payConfigStorage.getInputCharset()+"\"";
        orderInfo = orderInfo + "&it_b_pay=\"30m\"";
        orderInfo = orderInfo + "&return_url=\""+payConfigStorage.getReturnUrl()+"\"";
        return orderInfo;
    }

    /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder
     */
    private  Map<String, Object> getOrder(PayOrder order) {
        Map<String, Object> orderInfo = new TreeMap<>();
        // 签约合作者身份ID
        orderInfo.put("partner", payConfigStorage.getPid());
        // 签约卖家支付宝账号
        orderInfo.put("seller_id", payConfigStorage.getSeller());
        // 商户网站唯一订单号
        orderInfo.put("out_trade_no", order.getOutTradeNo());
        // 商品名称
        orderInfo.put("subject", order.getSubject());
        // 商品详情
        orderInfo.put("body", order.getBody());
        // 商品金额
        orderInfo.put("total_fee", Util.conversionAmount(order.getPrice()).toString() );
        // 服务器异步通知页面路径
        orderInfo.put("notify_url", payConfigStorage.getNotifyUrl() );
        // 服务接口名称， 固定值
        orderInfo.put("service",  order.getTransactionType().getMethod()  );
        // 支付类型， 固定值
        orderInfo.put("payment_type",  "1" );
        // 参数编码， 固定值
        orderInfo.put("_input_charset",  payConfigStorage.getInputCharset());
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // TODO 2017/2/6 11:05 author: egan  目前写死，这一块建议配置

        if (null != order.getExpirationTime()) {
            orderInfo.put("timeout_express", DateUtils.minutesRemaining(order.getExpirationTime()) + "m");
        }else {
            orderInfo.put("it_b_pay",  "30m");
        }
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo.put("return_url", payConfigStorage.getReturnUrl());

        return preOrderHandler(orderInfo, order);
    }



    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code 状态
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
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return PayOutMessage.TEXT().content("success").build();
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

        StringBuffer formHtml = new StringBuffer();

        formHtml.append("<form id=\"_alipaysubmit_\" name=\"alipaysubmit\" action=\"" )
                .append( HTTPS_REQ_URL)
                .append(  "?_input_charset=" )
                .append( payConfigStorage.getInputCharset())
                .append( "\" method=\"")
                .append( method.name().toLowerCase()) .append( "\">");

        for (Map.Entry<String, Object> entry : orderInfo.entrySet()) {
            Object o = entry.getValue();
            if (StringUtils.isEmpty((String)o) || "null".equals(o) ) {
                continue;
            }
            formHtml.append("<input type=\"hidden\" name=\"" + entry.getKey() + "\" value=\"" + o + "\"/>");
        }


        //submit按钮控件请不要含有name属性
        formHtml.append("</form>");
        formHtml.append("<script>document.forms['_alipaysubmit_'].submit();</script>");

        return formHtml.toString();
    }



    /**
     * 生成二维码支付
     * 暂未实现或无此功能
     * @param orderInfo 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public String getQrPay(PayOrder orderInfo) {
        throw new UnsupportedOperationException();
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * @param order 发起支付的订单信息
     * @return 支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        throw new UnsupportedOperationException();
    }


    /**
     *  交易查询接口
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {

        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.QUERY);
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

        return  secondaryInterface(tradeNo, outTradeNo, AliTransactionType.CLOSE);
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
     * 申请退款接口
     * 废弃
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param refundAmount 退款金额
     * @param totalAmount 总金额
     * @return 返回支付方申请退款后的结果
     * @see #refund(RefundOrder, Callback)
     */
    @Deprecated
    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {

        return refund(new RefundOrder(tradeNo, outTradeNo, refundAmount, totalAmount));
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder   退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.REFUND);

        Map<String, Object> bizContent = getBizContent(refundOrder.getTradeNo(), refundOrder.getOutTradeNo(), null);
        if (!StringUtils.isEmpty(refundOrder.getRefundNo())){
            bizContent.put("out_request_no", refundOrder.getRefundNo());
        }
        bizContent.put("refund_amount", refundOrder.getRefundAmount());
        //设置请求参数的集合
        parameters.put("biz_content", JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return  requestTemplate.getForObject(QUERY_REQ_URL + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);
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
        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.REFUNDQUERY);
    }
    /**
     * 查询退款
     *
     * @param refundOrder   退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder){

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.REFUNDQUERY);

        Map<String, Object> bizContent = getBizContent(refundOrder.getTradeNo(), refundOrder.getOutTradeNo(), null);
        if (!StringUtils.isEmpty(refundOrder.getRefundNo())){
            bizContent.put("out_request_no", refundOrder.getRefundNo());
        }
        //设置请求参数的集合
        parameters.put("biz_content",  JSON.toJSONString(bizContent));

        //设置签名
        setSign(parameters);
        return  requestTemplate.getForObject(QUERY_REQ_URL + "?"  + UriVariables.getMapToParameters(parameters), JSONObject.class);

    }


    /**
     * 目前只支持日账单
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.DOWNLOADBILL);

        Map<String, Object> bizContent = new TreeMap<>();
        bizContent.put("bill_type", billType);
        //目前只支持日账单

        bizContent.put("bill_date", df.format(billDate));
        //设置请求参数的集合
        parameters.put("biz_content", JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return  requestTemplate.getForObject(QUERY_REQ_URL + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);
    }


    /**
     *
     * @param tradeNoOrBillDate 支付平台订单号或者账单类型， 具体请
     *                          类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType  商户单号或者 账单类型
     * @param transactionType 交易类型
     * @return 返回支付方对应接口的结果
     */
    @Override
    public Map<String, Object> secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType) {
        if (transactionType == AliTransactionType.DOWNLOADBILL){
            if (tradeNoOrBillDate instanceof  Date){
                return downloadbill((Date) tradeNoOrBillDate, outTradeNoBillType);
            }
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + tradeNoOrBillDate.getClass()));
        }

        if (!(tradeNoOrBillDate instanceof  String)){
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + tradeNoOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(transactionType);
        //设置请求参数的集合
        parameters.put("biz_content", getContentToJson(tradeNoOrBillDate.toString(), outTradeNoBillType));
        //设置签名
        setSign(parameters);
        return requestTemplate.getForObject(QUERY_REQ_URL + "?" + UriVariables.getMapToParameters(parameters), JSONObject.class);

    }

    /**
     * 转账
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        return null;
    }

    /**
     * 获取支付请求地址
     *
     * @param transactionType 交易类型
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return null;
    }


    /**
     *  获取biz_content。请求参数的集合 不包含下载账单
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @param bizContent  请求参数的集合
     * @return 请求参数的集合 不包含下载账单
     */
    private  Map<String, Object> getBizContent(String tradeNo, String outTradeNo,  Map<String, Object> bizContent){
        if (null == bizContent){
            bizContent = new TreeMap<>();
        }
        if (null != outTradeNo){
            bizContent.put("out_trade_no", outTradeNo);
        }
        if (null != tradeNo){
            bizContent.put("trade_no", tradeNo);
        }
        return bizContent;
    }

    /**
     *  获取biz_content。不包含下载账单
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return 请求参数的集合 不包含下载账单
     */
    private String getContentToJson(String tradeNo, String outTradeNo){

        return JSON.toJSONString(getBizContent(tradeNo, outTradeNo, null));
    }

}
