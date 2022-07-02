package com.egzosn.pay.common.api;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.DefaultNoticeRequest;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.Order;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.HttpRequestTemplate;
import com.egzosn.pay.common.util.MatrixToImageWriter;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 支付基础服务
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2017/3/5 20:36
 *   </pre>
 */
public abstract class BasePayService<PC extends PayConfigStorage> implements PayService<PC> {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    protected PC payConfigStorage;

    protected HttpRequestTemplate requestTemplate;
    protected int retrySleepMillis = 1000;

    protected int maxRetryTimes = 5;
    /**
     * 支付消息处理器
     */
    protected PayMessageHandler handler;
    /**
     * 支付消息拦截器
     */
    protected List<PayMessageInterceptor<PayMessage, PayService>> interceptors = new ArrayList<PayMessageInterceptor<PayMessage, PayService>>();

    private Charset inputCharset = Consts.UTF_8;

    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     */
    @Override
    public BasePayService setPayConfigStorage(PC payConfigStorage) {
        this.payConfigStorage = payConfigStorage;

        if (StringUtils.isNotEmpty(payConfigStorage.getInputCharset())) {
            this.inputCharset = Charset.forName(payConfigStorage.getInputCharset());
        }
        return this;
    }

    @Override
    public PC getPayConfigStorage() {
        return payConfigStorage;
    }

    @Override
    public HttpRequestTemplate getHttpRequestTemplate() {
        return requestTemplate;
    }

    /**
     * 设置并创建请求模版， 代理请求配置这里是否合理？？，
     *
     * @param configStorage http请求配置
     * @return 支付服务
     */
    @Override
    public BasePayService setRequestTemplateConfigStorage(HttpConfigStorage configStorage) {
        this.requestTemplate = new HttpRequestTemplate(configStorage);
        return this;
    }


    public BasePayService(PC payConfigStorage) {
        this(payConfigStorage, null);
    }

    public BasePayService(PC payConfigStorage, HttpConfigStorage configStorage) {
        setPayConfigStorage(payConfigStorage);
        setRequestTemplateConfigStorage(configStorage);

    }


    /**
     * Generate a Base64 encoded String from  user , password
     *
     * @param user     用户名
     * @param password 密码
     * @return authorizationString
     */
    protected String authorizationString(String user, String password) {
        String base64ClientID = null;
        try {
            base64ClientID = com.egzosn.pay.common.util.sign.encrypt.Base64.encode(String.format("%s:%s", user, password).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            LOG.error("", e);
        }

        return base64ClientID;
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

        return SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(), characterEncoding);
    }

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    public String createSign(Map<String, Object> content, String characterEncoding) {
        return SignUtils.valueOf(payConfigStorage.getSignType()).sign(content, payConfigStorage.getKeyPrivate(), characterEncoding);
    }

    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @return 对应页面重定向信息
     */
    @Override
    public <O extends PayOrder> String toPay(O order) {
        if (StringUtils.isNotEmpty(order.getSubject()) && order.getSubject().contains("'")) {
            order.setSubject(order.getSubject().replace("'", ""));
        }
        if (StringUtils.isNotEmpty(order.getBody()) && order.getBody().contains("'")) {
            order.setBody(order.getBody().replace("'", ""));
        }
        Map<String, Object> orderInfo = orderInfo(order);
        return buildRequest(orderInfo, MethodType.POST);
    }

    /**
     * app支付
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应app所需参数信息
     */
    @Override
    public <O extends PayOrder> Map<String, Object> app(O order) {
        return orderInfo(order);
    }

    /**
     * 生成二维码支付
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public <O extends PayOrder> BufferedImage genQrPay(O order) {
        return MatrixToImageWriter.writeInfoToJpgBuff(getQrPay(order));
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
        return getNoticeParams(new DefaultNoticeRequest(parameterMap, is)).getBody();
    }

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param request 通知请求
     * @return 获得回调的请求参数
     */
    @Override
    public NoticeParams getNoticeParams(NoticeRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();

        Map<String, Object> params = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = values.length; i < len; i++) {
                sb.append(values[i]).append((i == len - 1) ? "" : ',');
            }
            String valueStr = sb.toString();
            if (StringUtils.isNotEmpty(payConfigStorage.getInputCharset()) && !valueStr.matches("\\w+")) {
                if (valueStr.equals(new String(valueStr.getBytes(Consts.ISO_8859_1), Consts.ISO_8859_1))) {
                    valueStr = new String(valueStr.getBytes(Consts.ISO_8859_1), inputCharset);
                }
            }
            params.put(name, valueStr);
        }
        return new NoticeParams(params);
    }

    /**
     * 交易查询接口，带处理器
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回查询回来的结果集
     */
    @Override
    public <T> T query(String tradeNo, String outTradeNo, Callback<T> callback) {

        return callback.perform(query(tradeNo, outTradeNo));
    }

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
    @Override
    public <T> T close(String tradeNo, String outTradeNo, Callback<T> callback) {
        return callback.perform(close(tradeNo, outTradeNo));
    }

    /**
     * 交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public <T> T cancel(String tradeNo, String outTradeNo, Callback<T> callback) {
        return callback.perform(cancel(tradeNo, outTradeNo));
    }

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public Map<String, Object> cancel(String tradeNo, String outTradeNo) {
        return Collections.emptyMap();
    }


    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public <T> T refund(RefundOrder refundOrder, Callback<T> callback) {

        return callback.perform(refund(refundOrder).getAttrs());
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public <T> T refundquery(RefundOrder refundOrder, Callback<T> callback) {
        return callback.perform(refundquery(refundOrder));
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型 内部自动转化 {@link BillType}
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String billType) {
        return Collections.emptyMap();
    }

    /**
     * 转账
     *
     * @param order    转账订单
     * @param callback 处理器
     * @return 对应的转账结果
     */
    @Override
    public <T> T transfer(TransferOrder order, Callback<T> callback) {
        return callback.perform(transfer(order));
    }


    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        return Collections.emptyMap();
    }

    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(String outNo, String tradeNo) {
        return Collections.emptyMap();
    }

    /**
     * 转账查询
     *
     * @param outNo    商户转账订单号
     * @param tradeNo  支付平台转账订单号
     * @param callback 处理器
     * @param <T>      返回类型
     * @return 对应的转账订单
     */
    @Override
    public <T> T transferQuery(String outNo, String tradeNo, Callback<T> callback) {
        return callback.perform(transferQuery(outNo, tradeNo));
    }

    /**
     * 设置支付消息处理器,这里用于处理具体的支付业务
     *
     * @param handler 消息处理器
     *                配合{@link  PayService#payBack(Map, InputStream)}进行使用
     *                <p>
     *                默认使用{@link  DefaultPayMessageHandler }进行实现
     */
    @Override
    public void setPayMessageHandler(PayMessageHandler handler) {
        this.handler = handler;
    }

    /**
     * 获取支付消息处理器,这里用于处理具体的支付业务
     * 配合{@link  PayService#payBack(Map, InputStream)}进行使用
     * <p>
     *
     * @return 默认使用{@link  DefaultPayMessageHandler }进行实现
     */
    public PayMessageHandler getPayMessageHandler() {
        if (null == handler) {
            setPayMessageHandler(new DefaultPayMessageHandler());
        }
        return handler;
    }

    /**
     * 设置支付消息拦截器
     *
     * @param interceptor 消息拦截器
     *                    配合{@link  PayService#payBack(Map, InputStream)}进行使用, 做一些预前处理
     */
    @Override
    public void addPayMessageInterceptor(PayMessageInterceptor interceptor) {
        interceptors.add(interceptor);
    }


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调响应信息
     */
    @Deprecated
    @Override
    public PayOutMessage payBack(Map<String, String[]> parameterMap, InputStream is) {
        return payBack(new DefaultNoticeRequest(parameterMap, is));
    }

    /**
     * 回调处理
     *
     * @param request 请求参数
     * @return 获得回调响应信息
     */
    @Override
    public PayOutMessage payBack(NoticeRequest request) {
        final NoticeParams noticeParams = getNoticeParams(request);
        if (LOG.isDebugEnabled()) {
            LOG.debug("回调响应:{}", JSON.toJSONString(noticeParams));
        }
        if (!verify(noticeParams)) {
            return getPayOutMessage("fail", "失败");
        }
        PayMessage payMessage = this.createMessage(noticeParams.getBody());
        Map<String, Object> context = new HashMap<>();
        for (PayMessageInterceptor interceptor : interceptors) {
            if (!interceptor.intercept(payMessage, context, this)) {
                return successPayOutMessage(payMessage);
            }
        }
        return getPayMessageHandler().handle(payMessage, context, this);
    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return new PayMessage(message);
    }

    /**
     * 预订单回调处理器，用于订单信息的扩展
     * 签名之前使用
     * 如果需要进行扩展请重写该方法即可
     *
     * @param orderInfo 预订单信息
     * @param orderInfo 订单信息
     * @return 处理后订单信息
     */
    @Override
    public <O extends PayOrder> Map<String, Object> preOrderHandler(Map<String, Object> orderInfo, O payOrder) {
        return orderInfo;
    }

    /**
     * 过时
     *
     * @param parameters 参数map
     * @param key        key
     * @param value      值
     * @return 返回订单参数
     */
    @Deprecated
    protected Map<String, Object> setParameters(Map<String, Object> parameters, String key, String value) {
        return OrderParaStructure.loadParameters(parameters, key, value);
    }

    /**
     * 过时
     *
     * @param parameters 参数map
     * @param key        key
     * @param order      订单对象
     * @return 返回订单参数
     */
    @Deprecated
    protected Map<String, Object> setParameters(Map<String, Object> parameters, String key, Order order) {
        return OrderParaStructure.loadParameters(parameters, key, order);
    }


}
