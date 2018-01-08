package com.egzosn.pay.common.api;

import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.HttpRequestTemplate;
import com.egzosn.pay.common.util.sign.SignUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 支付基础服务
 * @author: egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2017/3/5 20:36
 *   </pre>
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
    @Override
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
     * @return 支付服务
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
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    @Override
    public String createSign(Map<String, Object> content, String characterEncoding) {
        return  SignUtils.valueOf(payConfigStorage.getSignType()).sign(content, payConfigStorage.getKeyPrivate(),characterEncoding);
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

        Map<String, Object> params = new TreeMap<String,Object>();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for (int i = 0,len =  values.length; i < len; i++) {
                valueStr += (i == len - 1) ?  values[i] : values[i] + ",";
            }
            if (!valueStr.matches("\\w+")){
                try {
                    if(valueStr.equals(new String(valueStr.getBytes("iso8859-1"), "iso8859-1"))){
                        valueStr=new String(valueStr.getBytes("iso8859-1"), payConfigStorage.getInputCharset());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            params.put(name, valueStr);
        }
        return params;
    }


}
