package com.egzosn.pay.common.http;

import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.security.*;
import java.util.Map;

/**
 * http请求工具
 * @author: egan
 *  <code>
 * email egzosn@gmail.com <br>
 * date 2017/3/3 21:33
 *  </code>
 */
public class HttpRequestTemplate {

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    /**
     *  获取代理带代理地址的 HttpHost
     * @return 获取代理带代理地址的 HttpHost
     */
    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     *  初始化
     * @param configStorage 请求配置
     */
    public HttpRequestTemplate(HttpConfigStorage configStorage) {
        setHttpConfigStorage(configStorage);
    }

    public HttpRequestTemplate() {
        setHttpConfigStorage(null);
    }


    /**
     *  创建ssl配置
     * @param configStorage 请求配置
     * @return SSLConnectionSocketFactory  Layered socket factory for TLS/SSL connections.
     */
    public SSLConnectionSocketFactory createSSL( HttpConfigStorage configStorage){

        if (StringUtils.isEmpty(configStorage.getKeystorePath()) || StringUtils.isEmpty(configStorage.getKeystorePath())){
            return null;
        }

            //读取本机存放的PKCS12证书文件
        try(InputStream instream = configStorage.isPath() ? new FileInputStream(new File(configStorage.getKeystore())) : new ByteArrayInputStream(configStorage.getKeystore().getBytes())){
                //指定读取证书格式为PKCS12
                KeyStore keyStore = KeyStore.getInstance("PKCS12");

                char[] password = configStorage.getStorePassword().toCharArray();
                //指定PKCS12的密码
                keyStore.load(instream, password);
                SSLContext sslcontext = SSLContexts.custom()
                        .loadKeyMaterial(keyStore, password).build();

                //指定TLS版本
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslcontext, new String[]{"TLSv1"}, null,
                        new DefaultHostnameVerifier());

                return sslsf;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 创建代理服务器
     * @param configStorage 请求配置
     * @return 代理服务器配置
     */
    public CredentialsProvider createProxy(HttpConfigStorage configStorage){

        if (StringUtils.isBlank(configStorage.getHttpProxyHost())) {
            return null;
        }
        //http代理地址设置
        httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());

        if (StringUtils.isBlank(configStorage.getHttpProxyUsername())) {
            return null;
        }

        // 需要用户认证的代理服务器
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort()),
                new UsernamePasswordCredentials(configStorage.getHttpProxyUsername(), configStorage.getHttpProxyPassword()));


        return credsProvider;
    }

    /**
     * 设置HTTP请求的配置
     *
     * @param configStorage 请求配置
     * @return 当前HTTP请求的客户端模板
     */
    public HttpRequestTemplate setHttpConfigStorage(HttpConfigStorage configStorage) {

        if (null == configStorage) {
            httpClient = HttpClients.createDefault();
            return this;
        }

        httpClient = HttpClients
                .custom()
                //设置代理或网络提供者
                .setDefaultCredentialsProvider(createProxy(configStorage))
                //设置httpclient的SSLSocketFactory
                .setSSLSocketFactory(createSSL(configStorage))
                .build();

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
     * Map&lt;String, String&gt; uriVariables = new HashMap&lt;String, String&gt;();<br>
     *
     * uriVariables.put(&quot;id&quot;, &quot;1&quot;);<br>
     *
     * uriVariables.put(&quot;type&quot;, &quot;APP&quot;);<br>
     *
     * getForObject(&quot;http://egan.in/pay/{id}/f/{type}&quot;, String.class, uriVariables)<br>
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
