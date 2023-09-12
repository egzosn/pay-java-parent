package com.egzosn.pay.common.api;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.HttpRequestTemplate;

/**
 * 支付服务
 *
 * @author egan
 * <pre>
 *         email egzosn@gmail.com
 *         date 2016-5-18 14:09:01
 *         </pre>
 */
public interface PayService<PC extends PayConfigStorage> {


    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    PayService setPayConfigStorage(PC payConfigStorage);

    /**
     * 获取支付配置
     *
     * @return 支付配置
     */
    PC getPayConfigStorage();

    /**
     * 获取http请求工具
     *
     * @return http请求工具
     */
    HttpRequestTemplate getHttpRequestTemplate();

    /**
     * 设置 请求工具配置  设置并创建请求模版， 代理请求配置这里是否合理？？，
     *
     * @param configStorage http请求配置
     * @return 支付服务
     */
    PayService setRequestTemplateConfigStorage(HttpConfigStorage configStorage);

    /**
     * 回调校验
     * 已过时方法，详情{@link #verify(NoticeParams)}
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     * @see #verify(NoticeParams)
     */
    @Deprecated
    boolean verify(Map<String, Object> params);

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    boolean verify(NoticeParams params);


    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @param <O>   预订单类型
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    <O extends PayOrder> Map<String, Object> orderInfo(O order);

    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应页面重定向信息
     */
    <O extends PayOrder> String toPay(O order);

    /**
     * app支付
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应app所需参数信息
     */
    <O extends PayOrder> Map<String, Object> app(O order);

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    String createSign(String content, String characterEncoding);


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调的请求参数
     * @see #getNoticeParams(NoticeRequest)
     */
    @Deprecated
    Map<String, Object> getParameter2Map(Map<String, String[]> parameterMap, InputStream is);

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param request 通知请求
     * @return 获得回调的请求参数
     */
    NoticeParams getNoticeParams(NoticeRequest request);

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    PayOutMessage getPayOutMessage(String code, String message);

    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    PayOutMessage successPayOutMessage(PayMessage payMessage);

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    String buildRequest(Map<String, Object> orderInfo, MethodType method);


    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回图片信息，支付时需要的
     */
    <O extends PayOrder> BufferedImage genQrPay(O order);

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回二维码信息,，支付时需要的
     */
    <O extends PayOrder> String getQrPay(O order);

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * 刷脸付
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回支付结果
     */
    <O extends PayOrder> Map<String, Object> microPay(O order);

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     * @see #query(AssistOrder)
     */
    @Deprecated
    Map<String, Object> query(String tradeNo, String outTradeNo);

    /**
     * 交易查询接口，带处理器
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回查询回来的结果集
     */
    @Deprecated
    <T> T query(String tradeNo, String outTradeNo, Callback<T> callback);


    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    Map<String, Object> query(AssistOrder assistOrder);


    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易关闭后的结果
     * @see #close(AssistOrder)
     */
    @Deprecated
    Map<String, Object> close(String tradeNo, String outTradeNo);

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    Map<String, Object> close(AssistOrder assistOrder);


    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回支付方交易关闭后的结果
     */
    @Deprecated
    <T> T close(String tradeNo, String outTradeNo, Callback<T> callback);


    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    Map<String, Object> cancel(String tradeNo, String outTradeNo);

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回支付方交易撤销后的结果
     */
    @Deprecated
    <T> T cancel(String tradeNo, String outTradeNo, Callback<T> callback);


    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    RefundResult refund(RefundOrder refundOrder);

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方申请退款后的结果
     */
    @Deprecated
    <T> T refund(RefundOrder refundOrder, Callback<T> callback);


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    Map<String, Object> refundquery(RefundOrder refundOrder);

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方查询退款后的结果
     */
    @Deprecated
    <T> T refundquery(RefundOrder refundOrder, Callback<T> callback);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型 内部自动转化 {@link BillType}
     * @return 返回支付方下载对账单的结果
     */
    Map<String, Object> downloadBill(Date billDate, String billType);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型
     * @return 返回支付方下载对账单的结果
     */
    Map<String, Object> downloadBill(Date billDate, BillType billType);


    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    Map<String, Object> transfer(TransferOrder order);

    /**
     * 转账
     *
     * @param order    转账订单
     * @param callback 处理器
     * @param <T>      返回类型
     * @return 对应的转账结果
     */
    @Deprecated
    <T> T transfer(TransferOrder order, Callback<T> callback);


    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     * @return 对应的转账订单
     * @deprecated 替代{@link TransferService#transferQuery(com.egzosn.pay.common.bean.AssistOrder)}
     */
    @Deprecated
    Map<String, Object> transferQuery(String outNo, String tradeNo);

    /**
     * 转账查询
     *
     * @param outNo    商户转账订单号
     * @param tradeNo  支付平台转账订单号
     * @param callback 处理器
     * @param <T>      返回类型
     * @return 对应的转账订单
     */
    @Deprecated
    <T> T transferQuery(String outNo, String tradeNo, Callback<T> callback);

    /**
     * 回调处理
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调响应信息
     * 过时方法，详情查看 {@link #payBack(NoticeRequest)}
     */
    @Deprecated
    PayOutMessage payBack(Map<String, String[]> parameterMap, InputStream is);

    /**
     * 回调处理
     *
     * @param request 请求参数
     * @return 获得回调响应信息
     */
    PayOutMessage payBack(NoticeRequest request);


    /**
     * 设置支付消息处理器,这里用于处理具体的支付业务
     *
     * @param handler 消息处理器
     *                配合{@link  com.egzosn.pay.common.api.PayService#payBack(NoticeRequest)}进行使用
     *                <p>
     *                默认使用{@link  com.egzosn.pay.common.api.DefaultPayMessageHandler }进行实现
     */
    void setPayMessageHandler(PayMessageHandler handler);

    /**
     * 设置支付消息处理器,这里用于处理具体的支付业务
     *
     * @param interceptor 消息拦截器
     *                    配合{@link  com.egzosn.pay.common.api.PayService#payBack(NoticeRequest)}进行使用
     *                    <p>
     *                    默认使用{@link  com.egzosn.pay.common.api.DefaultPayMessageHandler }进行实现
     */
    void addPayMessageInterceptor(PayMessageInterceptor interceptor);

    /**
     * 获取支付请求地址
     *
     * @param transactionType 交易类型
     * @return 请求地址
     */
    String getReqUrl(TransactionType transactionType);

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    PayMessage createMessage(Map<String, Object> message);

    /**
     * 预订单回调处理器，用于订单信息的扩展
     * 签名之前使用
     * 如果需要进行扩展请重写该方法即可
     *
     * @param orderInfo 商户平台预订单信息
     * @param payOrder  订单信息
     * @param <O>       预订单类型
     * @return 处理后订单信息
     */
    @Deprecated
    <O extends PayOrder> Map<String, Object> preOrderHandler(Map<String, Object> orderInfo, O payOrder);

}
