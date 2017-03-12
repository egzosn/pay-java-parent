package in.egan.pay.common.before.api;

import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

/**
 * 支付服务
 *
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 * @see in.egan.pay.common.api.PayService
 */
@Deprecated
 public interface PayService {

    /**
     * 回调校验URL
     * @return
     */
     String getHttpsVerifyUrl();

    /**
     * 设置支付配置
     * @param payConfigStorage
     */
     void setPayConfigStorage(PayConfigStorage payConfigStorage);

    /**
     * 获取支付配置
     * @return
     */
     PayConfigStorage getPayConfigStorage();

    /**
     * 回调校验
     * @param params 回调回来的参数集
     * @return
     */
     boolean verify(Map<String, String> params);

    /**
     * 签名校验
     * @param params 参数集
     * @param sign 签名
     * @return
     */
     boolean getSignVerify(Map<String, String> params, String sign);

    /**
     * URL校验
     * @param notify_id
     * @return
     * @throws PayErrorException
     */
     String verifyUrl(String notify_id) throws PayErrorException;

    /**
     *  请求接口
     * @param executor 请求的具体执行者
     * @param uri 请求地址
     * @param data 请求数据
     * @param <T> 返回类型
     * @param <E> 请求数据类型
     * @return
     * @throws PayErrorException
     * @source
     */
     <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException;

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
     * 将请求参数或者请求流转化为 Map
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return
     */
     Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is);

    /**
     * 获取输出消息，用户返回给支付端
     * @param code
     * @param message
     * @return
     */
    PayOutMessage getPayOutMessage(String code, String message);

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     * @param orderInfo 发起支付的订单信息
     * @param method 请求方式  "post" "get",
     *               @see MethodType
     * @return
     */
     String buildRequest(Map<String, Object> orderInfo, MethodType method);

    /**
     * 获取输出二维码，用户返回给支付端,
     * @param orderInfo 发起支付的订单信息
     * @return
     */
    BufferedImage genQrPay(Map<String, Object> orderInfo);

    /**
     *  交易查询接口
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> query(String tradeNo, String outTradeNo);

    /**
     *  交易关闭接口
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> close(String tradeNo, String outTradeNo);

    /**
     *  交易关闭接口
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> refund(String tradeNo, String outTradeNo);

    /**
     *  查询退款
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> refundquery(String tradeNo, String outTradeNo);

    /**
     *  下载对账单
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @return
     */
   Object downloadbill(Date billDate, String billType);


}
