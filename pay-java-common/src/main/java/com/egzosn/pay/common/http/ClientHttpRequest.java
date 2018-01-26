package com.egzosn.pay.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.util.XML;
import com.egzosn.pay.common.exception.PayErrorException;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import static com.egzosn.pay.common.http.UriVariables.getMapToParameters;

/**
 * 一个HTTP请求的客户端
 * @author: egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2017/3/4 17:56
 *  </pre>
 */
public class ClientHttpRequest<T> extends HttpEntityEnclosingRequestBase implements  org.apache.http.client.ResponseHandler<T>{

    public static final ContentType APPLICATION_FORM_URLENCODED_UTF_8 = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);;


    /**
     * http请求方式 get pos
     */
    private MethodType method;
    /**
     *  响应类型
     */
    private Class<T> responseType;


    public ClientHttpRequest<T> setResponseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 空构造
     */
    public ClientHttpRequest() {
    }

    /**
     *  根据请求地址 请求方法，请求内容对象
     * @param uri 请求地址
     * @param method  请求方法
     * @param request 请求内容
     */
    public ClientHttpRequest(URI uri, MethodType method, Object request) {
       this(uri, method);
        setParameters(request);
    }
    /**
     * 根据请求地址 请求方法
     * @param uri 请求地址
     * @param method  请求方法
     */
    public ClientHttpRequest(URI uri, MethodType method) {
        this.setURI(uri);
        this.method = method;
    }

    /**
     * 根据请求地址
     * @param uri  请求地址
     */
    public ClientHttpRequest(URI uri) {
        this.setURI(uri);
    }
    /**
     * 根据请求地址
     * @param uri  请求地址
     */
    public ClientHttpRequest(String uri) {
        this.setURI(URI.create(uri));
    }
    /**
     * 根据请求地址 请求方法
     * @param uri 请求地址
     * @param method  请求方法
     */
    public ClientHttpRequest(String uri, MethodType method) {
        this.setURI(URI.create(uri));
        this.method = method;
    }
    /**
     *  根据请求地址 请求方法，请求内容对象
     * @param uri 请求地址
     * @param method  请求方法
     * @param request 请求内容
     */
    public ClientHttpRequest(String uri, MethodType method, Object request) {
        this(uri, method);
        setParameters(request);
    }

    /**
     * 设置请求方式
     *
     * @param method 请求方式
     * {@link com.egzosn.pay.common.bean.MethodType} 请求方式
     */
    public void setMethod(MethodType method) {
        this.method = method;
    }

    /**
     * 获取请求方式
     * @return 请求方式
     */
    @Override
    public String getMethod() {
        return method.name();
    }

    /**
     * 设置代理
     * @param httpProxy http代理配置信息
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setProxy(HttpHost httpProxy){
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            setConfig(config);
        }
        return this;
    }


    /**
     * 设置请求参数
     *
     * @param request 请求参数
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setParameters(Object request) {
        if (null == request){
            return this;
        }
        if (request instanceof HttpStringEntity){
            HttpStringEntity entity = (HttpStringEntity)request;
            setEntity(entity);
            if (null != entity.getHeaders() ){
                for (Header header : entity.getHeaders()){
                    addHeader(header);
                }
            }
        } else if (request instanceof HttpEntity){
            setEntity((HttpEntity)request);
        } else if (request instanceof Map) {
            StringEntity entity = new StringEntity(getMapToParameters((Map) request), APPLICATION_FORM_URLENCODED_UTF_8);
            setEntity(entity);
        } else if (request instanceof String) {
            StringEntity entity = new StringEntity((String) request,  APPLICATION_FORM_URLENCODED_UTF_8);
            setEntity(entity);
        } else {
            StringEntity entity = new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON);
            setEntity(entity);
        }

        return this;

    }


    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();

        if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode() != 304) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (null == responseType){
            responseType = (Class<T>) String.class;
        }

        String[] value = null;
        if (null == entity.getContentType()){
            value = new String[]{"application/x-www-form-urlencoded"};
        }else {
            value = entity.getContentType().getValue().split(";");
        }

        if (ContentType.APPLICATION_OCTET_STREAM.getMimeType().equals(value[0])){

            if (responseType.isAssignableFrom(InputStream.class)){
                return (T)entity.getContent();
            }
            if (responseType.isAssignableFrom(OutputStream.class)){
                try {
                    T t = responseType.newInstance();
                    entity.writeTo((OutputStream)t);
                    return t;
                } catch (InstantiationException e) {
                    throw new PayErrorException(new PayException("InstantiationException", e.getMessage()));
                } catch (IllegalAccessException e) {
                    throw new PayErrorException(new PayException("IllegalAccessException", e.getMessage()));
                }

            }
        }
        String charset = "UTF-8";
        if (null != value && 2 == charset.length()) {
            charset = value[1].substring(value[1].indexOf("=") + 1);
        }
        String result = EntityUtils.toString(entity, charset);
        if (responseType.isAssignableFrom(String.class)){
            return (T)result;
        }

        String first = result.substring(0, 1);
        if ( ContentType.APPLICATION_JSON.getMimeType().equals( value[0]) || "{[".indexOf(first) >= 0 ){
            try {
                return JSON.parseObject(result, responseType);
            }catch (JSONException e){
                throw new PayErrorException(new PayException("failure", String.format("类型转化异常,contentType: %s\n%s", entity.getContentType().getValue(), e.getMessage() ), result));
            }
        }

        if (ContentType.APPLICATION_XML.getMimeType().equals( value[0]) || "<".indexOf(first) >= 0){
            return XML.toJSONObject(result).toJavaObject(responseType);
        }

        throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue(), result));

    }
}
