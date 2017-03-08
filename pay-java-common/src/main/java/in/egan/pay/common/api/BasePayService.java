package in.egan.pay.common.api;

import in.egan.pay.common.http.HttpConfigStorage;
import in.egan.pay.common.http.HttpRequestTemplate;
import in.egan.pay.common.util.sign.SignUtils;

import java.util.Map;

/**
 * 支付基础服务
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/3/5 20:36
 */
public abstract class BasePayService implements PayService {

    protected PayConfigStorage payConfigStorage;

    protected HttpRequestTemplate requestTemplate;
    protected int retrySleepMillis = 1000;

    protected int maxRetryTimes = 5;

    /**
     * 设置支付配置
     * @param payConfigStorage 支付配置
     */
    public BasePayService setPayConfigStorage(PayConfigStorage payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
        return this;
    }

    @Override
    public PayConfigStorage getPayConfigStorage() {
        return payConfigStorage;
    }
    @Override
    public HttpRequestTemplate getHttpRequestTemplate() {
        return requestTemplate;
    }

    /**
     * 设置并创建请求模版， 代理请求配置这里是否合理？？，
     * @param configStorage http请求配置
     * @return
     */
    @Override
    public BasePayService setRequestTemplateConfigStorage(HttpConfigStorage configStorage) {
        this.requestTemplate = new HttpRequestTemplate(configStorage);
        return this;
    }


    public BasePayService(PayConfigStorage payConfigStorage) {
        this(payConfigStorage, null);
    }

    public BasePayService(PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        setPayConfigStorage(payConfigStorage);
        setRequestTemplateConfigStorage(configStorage);
    }
    @Override
    public String createSign(String content, String characterEncoding) {

        return  SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(),characterEncoding);
    }

    @Override
    public String createSign(Map<String, Object> content, String characterEncoding) {
        return  SignUtils.valueOf(payConfigStorage.getSignType()).sign(content, payConfigStorage.getKeyPrivate(),characterEncoding);
    }



}
