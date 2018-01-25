package com.egzosn.pay.payoneer.api;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.AdvancedPayService;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.bean.outbuilder.PayTextOutMessage;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.payoneer.bean.PayoneerRequestBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @descrption payoneer业务逻辑
 * @author Actinia
 * @email hayesfu@qq.com
 * @date 2018-01-19
 */
public class PayoneerPayService extends BasePayService implements AdvancedPayService {
    /**
     * 日志
     */
    protected final Log log = LogFactory.getLog(PayoneerPayService.class);
    /**
     * 测试地址
     */
    public final static String SANDBOX_DOMAIN= "https://api.sandbox.payoneer.com/v2/programs/";

    /**
     * 正式地址
     */
    public final static String RELEASE_DOMAIN = "https://api.payoneer.com/v2/programs/";

    /**
     * 授权登录url
     * https://api.sandbox.payoneer.com/v2/programs/{Program_Id}/payees/login-link
     */
    public String urlLoginLink = payConfigStorage.getPid()+"/payees/login-link";

    /**
     * 授权注册url
     * https://api.sandbox.payoneer.com/v2/programs/{Program_Id}/payees/registration-link
     */
    public String urlRegistrationLink = payConfigStorage.getPid()+"/payees/registration-link";

    /**
     * 收款url
     * https://api.sandbox.payoneer.com/v2/programs/{Program_Id}/charges
     */
    public String urlChargesLink = payConfigStorage.getPid()+"/charges";

    /**
     * 取消收款url
     * https://api.sandbox.payoneer.com/v2/programs/{Program_Id}/charges/{client_reference_id}/cancel
     */
    public String urlCancelChargesLink = payConfigStorage.getPid()+"/charges/%s/cancel";

    /**
     * 查看收款状态
     * https://api.sandbox.payoneer.com/v2/programs/{Program_Id}/charges/{client_reference_id}/status
     */
    public String urlStatusLink = payConfigStorage.getPid()+"/charges/%s/status";


    public PayoneerPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public PayoneerPayService(PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     * 获取授权页面
     *
     * @param payeeId      收款id
     * @param authPageType 授权类型
     * @return 返回请求结果
     */
    @Override
    public String getAuthorizationPage(String payeeId, AuthPageType authPageType) {
        PayoneerRequestBean params = new PayoneerRequestBean();
        params.setPayeeId(payeeId);
        JSONObject response = getHttpRequestTemplate().postForObject("login".equals(authPageType.name())?getUrlLoginLink():getUrlRegistrationLink(),params,JSONObject.class);
        if(response != null && "0".equals(response.getString("code"))){
            return response.getString(authPageType.name()+"_link");
        }

        throw new PayErrorException(new PayException("fail", "Payoneer获取授权页面失败,原因:"+response.getString("hint"), response.toJSONString()));
    }

    /**
     * 发起授权
     * @param payeeId 授权id(收款id)
     * @param payOrder 订单信息
     * @return 返回请求结果
     */
    @Override
    public Map<String ,Object> charges(String payeeId,PayOrder payOrder){
        PayoneerRequestBean params = new PayoneerRequestBean(payeeId,payOrder.getPrice().toString(),payOrder.getOutTradeNo(),payOrder.getCurType(),payOrder.getBody());
        JSONObject response = getHttpRequestTemplate().postForObject(getUrlChargesLink(),params,JSONObject.class);
        if(response != null && "0".equals(response.getString("code"))){
            return response;
        }
        throw  new PayErrorException(new PayException("fail", "Payoneer申请收款失败,原因:"+response.getString("hint"), response.toJSONString()));
    }

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        return false;
    }

    /**
     * 签名校验
     *
     * @param params 参数集
     * @param sign   签名原文
     * @return 签名校验 true通过
     */
    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
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
    public boolean verifySource(String id) {
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
    public Map<String, Object> orderInfo(PayOrder order) {

        return null;
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
        return null;
    }

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    @Override
    public String createSign(Map<String, Object> content, String characterEncoding) {
        return null;
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
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayTextOutMessage.TEXT().content(code.toLowerCase()).build();
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
        return getPayOutMessage("ok", null);
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
        return null;
    }

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public BufferedImage genQrPay(PayOrder order) {
        return null;
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        return null;
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
    public <T> T query(String tradeNo, String outTradeNo, Callback<T> callback) {
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
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
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
    public <T> T close(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 申请退款接口
     * 废弃
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @return 返回支付方申请退款后的结果
     * @see #refund(RefundOrder)
     */
    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        return null;
    }

    /**
     * 申请退款接口
     * 废弃
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @param callback     处理器
     * @return 返回支付方申请退款后的结果
     * @see #refund(RefundOrder, Callback)
     */
    @Override
    public <T> T refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public <T> T refund(RefundOrder refundOrder, Callback<T> callback) {
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
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
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
    public <T> T refundquery(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Object downloadbill(Date billDate, String billType) {
        return null;
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
    public <T> T downloadbill(Date billDate, String billType, Callback<T> callback) {
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
    public <T> T secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {
        return null;
    }

    public String getUrlLoginLink() {
        return (payConfigStorage.isTest()?SANDBOX_DOMAIN:RELEASE_DOMAIN)+urlLoginLink;
    }

    public String getUrlRegistrationLink() {
        return (payConfigStorage.isTest()?SANDBOX_DOMAIN:RELEASE_DOMAIN)+urlRegistrationLink;
    }

    public String getUrlChargesLink() {
        return (payConfigStorage.isTest()?SANDBOX_DOMAIN:RELEASE_DOMAIN)+urlChargesLink;
    }

    public String getUrlCancelChargesLink() {
        return (payConfigStorage.isTest()?SANDBOX_DOMAIN:RELEASE_DOMAIN)+urlCancelChargesLink;
    }

    public String getUrlStatusLink() {
        return (payConfigStorage.isTest()?SANDBOX_DOMAIN:RELEASE_DOMAIN)+urlStatusLink;
    }
}
