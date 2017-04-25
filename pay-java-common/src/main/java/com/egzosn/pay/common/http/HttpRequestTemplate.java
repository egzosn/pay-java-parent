package com.egzosn.pay.common.http;

import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * http请求工具
 * @author: egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2017/3/3 21:33
 *  </pre>
 */
public class HttpRequestTemplate {

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpRequestTemplate(HttpConfigStorage configStorage) {
        setHttpConfigStorage(configStorage);
    }

    public HttpRequestTemplate() {
        setHttpConfigStorage(null);
    }

    /**
     * 设置HTTP请求的配置
     * @param configStorage 请求配置
     * @return 当前HTTP请求的客户端模板
     */
    public HttpRequestTemplate setHttpConfigStorage(HttpConfigStorage configStorage){

        if (null == configStorage){
            httpClient = HttpClients.createDefault();
            return this;
        }


        if (StringUtils.isNotBlank(configStorage.getHttpProxyHost())) {
            // 使用代理服务器
            if (StringUtils.isNotBlank(configStorage.getHttpProxyUsername())) {
                // 需要用户认证的代理服务器
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort()),
                        new UsernamePasswordCredentials(configStorage.getHttpProxyUsername(), configStorage.getHttpProxyPassword()));
                httpClient = HttpClients
                        .custom()
                        .setDefaultCredentialsProvider(credsProvider)
                        .build();
            } else {
                // 无需用户认证的代理服务器
                httpClient = HttpClients.createDefault();
            }
            httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());
        } else {
            httpClient = HttpClients.createDefault();
        }
        return this;
    }


    /**
     *
     * post
     * @param uri 请求地址
     * @param request 请求参数
     * @param responseType 为响应类(需要自己依据响应格式来确定)
     * @param uriVariables 地址通配符对应的值
     * @param <T> 响应类型
     * @return 类型对象
     */
    public <T> T postForObject(String uri, Object request, Class<T> responseType, Object... uriVariables){
        return doExecute(URI.create(UriVariables.getUri(uri, uriVariables)), request, responseType, MethodType.POST);
    }

    public <T> T postForObject(String uri, Object request, Class<T> responseType, Map<String, Object> uriVariables) {
        return doExecute(URI.create(UriVariables.getUri(uri, uriVariables)), request, responseType, MethodType.POST);
    }

    public <T> T postForObject(URI uri, Object request, Class<T> responseType){
        return doExecute(uri, request, responseType, MethodType.POST);
    }



    /**
     * get 请求
     * @param uri 请求地址
     * @param responseType 响应类型
     * @param uriVariables 用于匹配表达式
     * @param <T> 响应类型
     * @return 类型对象
     *
     * <code>
     *    getForObject(&quot;http://egan.in/pay/{id}/f/{type}&quot;, String.class, &quot;1&quot;, &quot;APP&quot;)
     * </code>
     */
    public <T> T getForObject(String uri, Class<T> responseType, Object... uriVariables){

        return doExecute(URI.create(UriVariables.getUri(uri, uriVariables)), null, responseType, MethodType.GET);
    }

    /**
     * get 请求
     *
     * @param uri          请求地址
     * @param responseType 响应类型
     * @param uriVariables 用于匹配表达式
     * @param <T>          响应类型
     * @return 类型对象
     * <code>
     * Map&lt;String, String&gt; uriVariables = new HashMap&lt;String, String&gt;();
     * uriVariables.put(&quot;id&quot;, &quot;1&quot;);
     * uriVariables.put(&quot;type&quot;, &quot;APP&quot;);
     * getForObject(&quot;http://egan.in/pay/{id}/f/{type}&quot;, String.class, uriVariables)
     * </code>
     */
    public <T> T getForObject(String uri, Class<T> responseType, Map<String, ?> uriVariables){
        return doExecute(URI.create(UriVariables.getUri(uri, uriVariables)), null, responseType, MethodType.GET);
    }


    /**
     * http 请求执行
     * @param uri 地址
     * @param request 请求数据
     * @param responseType 响应类型
     * @param method 请求方法
     * @param <T> 响应类型
     * @return 类型对象
     */
    public <T>T doExecute(URI uri, Object request, Class<T> responseType, MethodType method){
        ClientHttpRequest<T> httpRequest = new ClientHttpRequest(uri ,method, request);
        httpRequest.setProxy(httpProxy).setResponseType(responseType);
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
          return httpRequest.handleResponse(response);
        }catch (  IOException e){
            e.printStackTrace();
        }finally {
            httpRequest.releaseConnection();
        }
        return null;
    }

    /**
     * http 请求执行
     * @param uri 地址
     * @param request 请求数据
     * @param responseType 响应类型
     * @param method 请求方法
     * @param <T> 响应类型
     * @return 类型对象
     */
    public <T>T doExecute(String uri, Object request, Class<T> responseType, MethodType method){
       return doExecute(URI.create(uri), request, responseType, method);
    }




}
