package com.egzosn.pay.common.http;


import com.egzosn.pay.common.bean.CertStoreType;

import java.io.*;

/**
 * HTTP 配置
 * @author: egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2017/3/3 20:48
 *  </pre>
 */
public class HttpConfigStorage {
    /**
     * http代理地址
     */
    private String httpProxyHost;
    /**
     * 代理端口
     */
    private int httpProxyPort;
    /**
     * 请求授权用户名
     */
    private String authUsername;
    /**
     * 请求授权密码
     */
    private String authPassword;


    /**
     * 证书存储类型
     * @see #keystore 是否为https请求所需的证书（PKCS12）的地址,默认为地址，否则为证书信息串，文件流
     */
    private CertStoreType certStoreType = CertStoreType.PATH;

    /**
     * https请求所需的证书（PKCS12）
     * 证书内容
     */
    private Object keystore;
    /**
     * 证书对应的密码
     */
    private String storePassword;
    /**
     * 最大连接数
     */
    private int maxTotal = 0;
    /**
     * 默认的每个路由的最大连接数
     */
    private int defaultMaxPerRoute = 0;
    /**
     * 默认使用的响应编码
     */
    private String charset;

    private int socketTimeout = -1;

    private int connectTimeout = -1;

    /**
     * http代理地址
     * @return http代理地址
     */
    public String getHttpProxyHost() {
        return httpProxyHost;
    }


    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    /**
     *   代理端口
     * @return 代理端口
     */
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    /**
     * 请求授权用户名
     * @return 用户名
     */
    public String getAuthUsername() {
        return authUsername;
    }

    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }
    /**
     * 请求授权密码
     * @return 密码
     */
    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }


    public CertStoreType getCertStoreType() {
        return certStoreType;
    }

    public void setCertStoreType(CertStoreType certStoreType) {
        this.certStoreType = certStoreType;
    }

    /**
     * 获取证书信息
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串
     * @throws IOException 找不到文件异常
     */
    public InputStream getKeystoreInputStream() throws IOException {
        if (null == keystore) {
            return null;
        }
        return certStoreType.getInputStream(keystore);
    }
    /**
     * 获取证书信息
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串
     */
    public Object getKeystore() {
        return  keystore;
    }
    /**
     * 获取证书信息 证书地址
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串
     */
    public String getKeystoreStr() {
        return (String) keystore;
    }

    /**
     * 设置证书字符串信息或证书绝对地址
     * @param keystore 证书信息字符串信息或证书绝对地址
     */
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
    /**
     * 设置证书字符串信息输入流
     * @param keystore 证书信息 输入流
     */
    public void setKeystore(InputStream keystore) {
        this.keystore = keystore;
    }

    /**
     * 证书对应的密码
     * @return 密码
     */
    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
