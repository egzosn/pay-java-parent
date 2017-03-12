package in.egan.pay.common.api;

import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.TransactionType;
import in.egan.pay.common.http.HttpConfigStorage;
import in.egan.pay.common.http.HttpRequestTemplate;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 支付服务
 *
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public interface PayService {


    /**
     * 设置支付配置
     *
     * @param payConfigStorage
     */
    PayService setPayConfigStorage(PayConfigStorage payConfigStorage);

    /**
     * 获取支付配置
     *
     * @return
     */
    PayConfigStorage getPayConfigStorage();
    /**
     * 获取http请求工具
     *
     * @return
     */
    HttpRequestTemplate getHttpRequestTemplate();

    /**
     * 设置 请求工具配置
     * @param configStorage http请求配置
     * @return
     */
    PayService setRequestTemplateConfigStorage(HttpConfigStorage configStorage);

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return
     */
    boolean verify(Map<String, String> params);

    /**
     * 签名校验
     *
     * @param params 参数集
     * @param sign   签名
     * @return
     */
    boolean signVerify(Map<String, String> params, String sign);


    /**
     * 校验数据来源
     *
     * @param id 业务id, 数据的真实性.
     * @return
     */
    boolean verifySource(String id);


    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return
     * @see PayOrder
     */
    Map orderInfo(PayOrder order);

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    String createSign(String content, String characterEncoding);

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    String createSign(Map<String, Object> content, String characterEncoding);

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return
     */
    Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is);

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code
     * @param message
     * @return
     */
    PayOutMessage getPayOutMessage(String code, String message);

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return
     * @see MethodType
     */
    String buildRequest(Map<String, Object> orderInfo, MethodType method);

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param orderInfo 发起支付的订单信息
     * @return
     */
    BufferedImage genQrPay(Map<String, Object> orderInfo);

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> query(String tradeNo, String outTradeNo);

    /**
     * 交易查询接口
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T query(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> close(String tradeNo, String outTradeNo);

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T close(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 申请退款接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param refundAmount 退款金额
     * @param totalAmount 总金额
     * @return
     */
    Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount);
    /**
     * 申请退款接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param refundAmount 退款金额
     * @param totalAmount 总金额
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback);

    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> refundquery(String tradeNo, String outTradeNo);
    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T refundquery(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @return
     */
    Object downloadbill(Date billDate, String billType);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T downloadbill(Date billDate, String billType, Callback<T> callback);

    /**
     * 通用查询接口
     *
     * @param tradeNoOrBillDate 支付平台订单号或者账单日期， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link in.egan.pay.common.exception.PayErrorException}
     *
     * @param outTradeNoBillType  商户单号或者 账单类型
     * @param transactionType 交易类型
     * @param callback 处理器
     * @param <T> 返回类型
     * @return
     */
    <T>T secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback);




}
