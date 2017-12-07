package com.egzosn.pay.union.api;

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
import com.egzosn.pay.union.SDK.CertUtil;
import com.egzosn.pay.union.SDK.SDKConfig;
import com.egzosn.pay.union.SDK.SDKConstants;
import com.egzosn.pay.union.enums.UnionTransactionType;
import com.egzosn.pay.union.request.UnionQueryOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 * @create 2017 2017/11/5 0005
 */
public class UnionPayService extends BasePayService {
    //日志
    protected static final Log log = LogFactory.getLog(UnionPayService.class);
    /**
     * 测试域名
     */
    private static final String TEST_BASE_DOMAIN = "test.95516.com";
    /**
     * 正式域名
     */
    private static final String RELEASE_BASE_DOMAIN = "95516.com";
    /**
     * 交易请求地址
     */
    private static final String FRONT_TRANS_URL= "https://gateway.%s/gateway/api/frontTransReq.do";
    private static final String BACK_TRANS_URL= "https://gateway.%s/gateway/api/backTransReq.do";
    private static final String SINGLE_QUERY_URL= "https://gateway.%s/gateway/api/queryTrans.do";
    private static final String BATCH_TRANS_URL= "https://gateway.%s/gateway/api/batchTrans.do";
    private static final String FILE_TRANS_URL= "https://filedownload.%s/";
    private static final String APP_TRANS_URL= "https://gateway.%s/gateway/api/appTransReq.do";
    private static final String CARD_TRANS_URL= "https://gateway.%s/gateway/api/cardTransReq.do";
    /**
     * 以下缴费产品使用，其余产品用不到
     */
//    private static final String JF_FRONT_TRANS_URL= "https://gateway.%s/jiaofei/api/frontTransReq.do";
//    private static final String JF_BACK_TRANS_URL= "https://gateway.%s/jiaofei/api/backTransReq.do";
//    private static final String JF_SINGLE_QUERY_URL= "https://gateway.%s/jiaofei/api/queryTrans.do";
//    private static final String JF_APP_TRANS_URL= "https://gateway.%s/jiaofei/api/appTransReq.do";
//    private static final String JF_CARD_TRANS_URL= "https://gateway.%s/jiaofei/api/cardTransReq.do";

    public UnionPayService (PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public UnionPayService (PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
        SDKConfig.getConfig().loadPropertiesFromSrc();
    }


    /**
     * 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改
     * @return 返回参数集合
     */
    private Map<String ,String> getCommonParam(){
        Map<String ,String> params = new HashMap<>();
        params.put(SDKConstants.param_version, SDKConfig.getConfig().getVersion());
        params.put(SDKConstants.param_encoding, payConfigStorage.getInputCharset().toUpperCase());

        params.put(SDKConstants.param_merId, payConfigStorage.getPid());
        //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
        params.put(SDKConstants.param_accessType, "0");
        DateFormat df = new SimpleDateFormat("YYYYMMDDhhmmss");
        params.put(SDKConstants.param_txnTime, df.format(new Date()));
        params.put(SDKConstants.param_backUrl, SDKConfig.getConfig().getBackUrl());
        return params;
    }


    /**
     * 回调校验
     *
     * @param result 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify (Map<String, Object> result) {
        if(result != null){
            if(this.vailSign(result)){
                String respCode = result.get("respCode").toString();
                if(("00").equals(respCode)){

                    //成功,获取tn号
                    //String tn = resmap.get("tn");
                    //TODO
                    return true;
                }else{
                    //其他应答码为失败请排查原因或做失败处理
                    //TODO
                }
            }else{
//                校验失败
            }
        }else{

        }
        return false;
    }

    /**
     * 签名校验
     *
     * @param params 参数集
     * @param sign   签名
     * @return 签名校验 true通过
     */
    @Override
    public boolean signVerify (Map<String, Object> params, String sign) {
        return false;
    }

    /**
     * 支付宝需要,微信是否也需要再次校验来源，进行订单查询
     * 校验数据来源
     *
     * @param id 业务id, 数据的真实性.
     * @return true通过
     */
    @Override
    public boolean verifySource (String id) {
        return false;
    }

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    @Override
    public Map orderInfo (PayOrder order) {
        Map<String, String> params = this.getCommonParam();

        UnionTransactionType type =  (UnionTransactionType)order.getTransactionType();
        type.convertMap(params);
        switch (type){
            case APPLY_QR_CODE:
                params.put(SDKConstants.param_orderId, order.getOutTradeNo());
                params.put(SDKConstants.param_txnAmt, order.getPrice().toString());
                params.put(SDKConstants.param_currencyCode, "156");
                break;
            case CONSUME:
                params.put(SDKConstants.param_orderId, order.getOutTradeNo());
                params.put(SDKConstants.param_txnAmt, order.getPrice().toString());
                params.put(SDKConstants.param_currencyCode, "156");

                params.put(SDKConstants.param_qrNo, order.getAuthCode());
                params.put(SDKConstants.param_termId, order.getDeviceInfo());
                break;
            default:
        }

        return params;
    }


    /**
     * 根据签名类型获取银联签名对应的参数
     * @param signType 签名类型
     * @return 签名参数
     */
    public String getSignMethod(SignUtils signType) {
        switch (signType) {
            case RSA:
            case RSA2:
                return SDKConstants.SIGNMETHOD_RSA;
            case SHA256:
                return SDKConstants.SIGNMETHOD_SHA256;
            case SM3:
                return SDKConstants.SIGNMETHOD_SM3;
            default:
                return SDKConstants.SIGNMETHOD_RSA;
        }
    }



    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    @Override
    public String createSign(String content, String characterEncoding) {

        return  SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(),characterEncoding);
    }

    /**
     *  生成并设置签名
     * @param parameters 请求参数
     * @return 请求参数
     */
    private Map<String, String> setSign(Map<String, String> parameters){
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());
        String data = SignUtils.parameterText(parameters);
        parameters.put(SDKConstants.param_signMethod, getSignMethod(signUtils));
        String stringSign = "";
        switch (signUtils){
            case RSA:
                parameters.put(SDKConstants.param_certId, CertUtil.getSignCertId());
                stringSign = SignUtils.SHA1.createSign(data,"",payConfigStorage.getInputCharset());
                stringSign = signUtils.createSign(stringSign,payConfigStorage.getKeyPrivate(),payConfigStorage.getInputCharset());
                break;
            case SHA256:
                String before = SignUtils.SHA256.createSign(SDKConfig.getConfig().getSecureKey(),"",payConfigStorage.getInputCharset());
                stringSign = SignUtils.SHA256.createSign(data,"&"+before,payConfigStorage.getInputCharset());
                break;
            case SM3:
                stringSign = SignUtils.SM3.createSign(data,SDKConfig.getConfig().getSecureKey(),payConfigStorage.getInputCharset());
                break;
            default:
                parameters.put(SDKConstants.param_certId, CertUtil.getSignCertId());
                stringSign = SignUtils.SHA1.createSign(data,"",payConfigStorage.getInputCharset());
                stringSign = signUtils.createSign(stringSign,payConfigStorage.getKeyPrivate(),payConfigStorage.getInputCharset());
        }
        parameters.put(SDKConstants.param_signature, stringSign);
        return parameters;
    }

    /**
     *  验证数据合法性
     * @param resData 请求参数
     * @return 请求参数
     */
    private boolean vailSign(Map<String, Object> resData){
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());
        //签名原文
        String stringSign = resData.get(SDKConstants.param_signature).toString();
        String data = SignUtils.parameterText(resData);
        switch (signUtils){
            case RSA:
                //todo 不确定这样可靠
                return SignUtils.RSA.verify(resData,stringSign,payConfigStorage.getKeyPublic(),payConfigStorage.getInputCharset());
            case SHA256:
                String before = SignUtils.SHA256.createSign(SDKConfig.getConfig().getSecureKey(),"",payConfigStorage.getInputCharset());
                String nowSign = SignUtils.SHA256.createSign(data,"&"+before,payConfigStorage.getInputCharset());
                return stringSign.equals(nowSign);
            case SM3:
                nowSign = SignUtils.SM3.createSign(data,SDKConfig.getConfig().getSecureKey(),payConfigStorage.getInputCharset());
                return stringSign.equals(nowSign);
            default:
                    return false;
        }
    }

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public BufferedImage genQrPay (PayOrder order) {
        Map<String ,String > params = orderInfo(order);
        this.setSign(params);
        JSONObject response =  getHttpRequestTemplate().postForObject(this.getBackTransUrl(),params,JSONObject.class);
        if(this.vailSign(response)){
            if("00".equals(response.getString(SDKConstants.param_respCode))){
                //成功,获取tn号
                return MatrixToImageWriter.writeInfoToJpgBuff( response.getString(SDKConstants.param_respCode));
            }else{
                throw new PayErrorException(new PayException(response.getString(SDKConstants.param_respCode), response.getString(SDKConstants.param_respMsg), response.toJSONString()));
            }
        }else{
            throw new PayErrorException(new PayException("1000", "验证签名失败", response.toJSONString()));
        }
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay (PayOrder order) {
        Map<String ,String > params = orderInfo(order);
        this.setSign(params);
        return getHttpRequestTemplate().postForObject(this.getBackTransUrl(),params,JSONObject.class);
    }


    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query (String tradeNo, String outTradeNo) {
        Map<String ,String > params = this.getCommonParam();
        UnionTransactionType.QUERY.convertMap(params);
        params.put(SDKConstants.param_orderId,outTradeNo);
        this.setSign(params);
        JSONObject response =  getHttpRequestTemplate().postForObject(this.getSingleQueryUrl(),params,JSONObject.class);
        if(this.vailSign(response)){
            if("00".equals(response.getString(SDKConstants.param_respCode))){
                String origRespCode = response.getString(SDKConstants.param_origRespCode);
                if(("00").equals(origRespCode)){
                    //交易成功，更新商户订单状态
                    //TODO
                    return response;
                }else{
                    throw new PayErrorException(new PayException(response.getString(SDKConstants.param_respCode), response.getString(SDKConstants.param_respMsg), response.toJSONString()));
                }
            }else{
                throw new PayErrorException(new PayException(response.getString(SDKConstants.param_respCode), response.getString(SDKConstants.param_respMsg), response.toJSONString()));
            }
        }else{
            throw new PayErrorException(new PayException("1000", "验证签名失败", response.toJSONString()));
        }
    }

    /**
     * 消费撤销/退货接口
     *
     * @return 返回支付方申请退款后的结果
     */
    public Map<String, Object> unionRefundOrConsumeUndo (UnionQueryOrder queryOrder,UnionTransactionType type) {
        Map<String ,String> params = this.getCommonParam();
        type.convertMap(params);
        params.put(SDKConstants.param_orderId,queryOrder.getOrderId());
        params.put(SDKConstants.param_txnAmt,queryOrder.getTxnAmt());
        if(StringUtils.isNotBlank(queryOrder.getOrigQryId())) {
            params.put(SDKConstants.param_origQryId, queryOrder.getOrigQryId());
        }
        if(StringUtils.isNotBlank(queryOrder.getOrigOrderId())){
            params.put(SDKConstants.param_origOrderId,queryOrder.getOrigOrderId());
        }
        if(StringUtils.isNotBlank(queryOrder.getOrigTxnTime())) {
            params.put(SDKConstants.param_origTxnTime, queryOrder.getOrigOrderId());
        }
        this.setSign(params);
        JSONObject response =  getHttpRequestTemplate().postForObject(this.getBackTransUrl(),params,JSONObject.class);
        if(this.vailSign(response)){
            if("00".equals(response.getString(SDKConstants.param_respCode))){
                String origRespCode = response.getString(SDKConstants.param_origRespCode);
                //交易成功，更新商户订单状态
                //TODO
                return response;

            }else{
                throw new PayErrorException(new PayException(response.getString(SDKConstants.param_respCode), response.getString(SDKConstants.param_respMsg), response.toJSONString()));
            }
        }else{
            throw new PayErrorException(new PayException("1000", "验证签名失败", response.toJSONString()));
        }
    }

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调的请求参数
     */
    @Override
    public Map<String, Object> getParameter2Map (Map<String, String[]> parameterMap, InputStream is) {
        return null;
    }

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage getPayOutMessage (String code, String message) {
        return null;
    }

    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage successPayOutMessage (PayMessage payMessage) {
        return null;
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
    public String buildRequest (Map<String, Object> orderInfo, MethodType method) {
        return null;
    }


    /**
     * 交易查询接口，带处理器
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回查询回来的结果集
     */
    @Override
    public <T> T query (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close (String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public <T> T close (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund (String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        return null;
    }



    /**
     * 申请退款接口
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @param callback     处理器
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public <T> T refund (String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {
        return null;
    }

    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery (String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public <T> T refundquery (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间
     * @param billType 账单类型
     * @return 返回fileContent 请自行将数据落地
     */
    @Override
    public Object downloadbill (Date billDate, String billType) {
        Map<String ,String > params = this.getCommonParam();
        UnionTransactionType.File_Transfer.convertMap(params);
        DateFormat df = new SimpleDateFormat("MMDD");
        params.put(SDKConstants.param_settleDate,df.format(new Date()));
        params.put(SDKConstants.param_fileType,billType);
        this.setSign(params);
        Map<String ,Object > response =  getHttpRequestTemplate().postForObject(this.getFileTransUrl(),params,Map.class);
        if(this.vailSign(response)){
            if("00".equals(response.get(SDKConstants.param_respCode))){

                return response.get(SDKConstants.param_fileContent).toString();

            }else{
                throw new PayErrorException(new PayException(response.get(SDKConstants.param_respCode).toString(), response.get(SDKConstants.param_respMsg).toString(), response.toString()));
            }
        }else{
            throw new PayErrorException(new PayException("1000", "验证签名失败", response.toString()));
        }
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public <T> T downloadbill (Date billDate, String billType, Callback<T> callback) {
        return null;
    }

    /**
     * 通用查询接口
     *
     * @param tradeNoOrBillDate  支付平台订单号或者账单日期， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType 商户单号或者 账单类型
     * @param transactionType    交易类型
     * @param callback           处理器
     * @return 返回支付方对应接口的结果
     */
    @Override
    public <T> T secondaryInterface (Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {
        return null;
    }

//    public  String getFrontTransUrl () {
//        return payConfigStorage.isTest() ? String.format(FRONT_TRANS_URL,TEST_BASE_DOMAIN):String.format(FRONT_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
    public  String getBackTransUrl () {
        return payConfigStorage.isTest() ? String.format(BACK_TRANS_URL,TEST_BASE_DOMAIN):String.format(BACK_TRANS_URL,RELEASE_BASE_DOMAIN);
    }

    public  String getSingleQueryUrl () {
        return payConfigStorage.isTest() ? String.format(SINGLE_QUERY_URL,TEST_BASE_DOMAIN):String.format(SINGLE_QUERY_URL,RELEASE_BASE_DOMAIN);
    }
//
//    public  String getBatchTransUrl () {
//        return payConfigStorage.isTest() ? String.format(BATCH_TRANS_URL,TEST_BASE_DOMAIN):String.format(BATCH_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }

    public  String getFileTransUrl () {
        return payConfigStorage.isTest() ? String.format(FILE_TRANS_URL,TEST_BASE_DOMAIN):String.format(FILE_TRANS_URL,RELEASE_BASE_DOMAIN);
    }

//    public  String getAppTransUrl () {
//        return payConfigStorage.isTest() ? String.format(APP_TRANS_URL,TEST_BASE_DOMAIN):String.format(APP_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getCardTransUrl () {
//        return payConfigStorage.isTest() ? String.format(CARD_TRANS_URL,TEST_BASE_DOMAIN):String.format(CARD_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getJfFrontTransUrl () {
//        return payConfigStorage.isTest() ? String.format(JF_FRONT_TRANS_URL,TEST_BASE_DOMAIN):String.format(JF_FRONT_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getJfBackTransUrl () {
//        return payConfigStorage.isTest() ? String.format(JF_BACK_TRANS_URL,TEST_BASE_DOMAIN):String.format(JF_BACK_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getJfSingleQueryUrl () {
//        return payConfigStorage.isTest() ? String.format(JF_SINGLE_QUERY_URL,TEST_BASE_DOMAIN):String.format(JF_SINGLE_QUERY_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getJfAppTransUrl () {
//        return payConfigStorage.isTest() ? String.format(JF_APP_TRANS_URL,TEST_BASE_DOMAIN):String.format(JF_APP_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
//
//    public  String getJfCardTransUrl () {
//        return payConfigStorage.isTest() ? String.format(JF_CARD_TRANS_URL,TEST_BASE_DOMAIN):String.format(JF_CARD_TRANS_URL,RELEASE_BASE_DOMAIN);
//    }
}
