package com.egzosn.pay.common.http;


/**
 * HTTP 配置
 * @author: egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2017/3/3 20:48
 *  </pre>
 */
public class HttpConfigStorage {
    //http代理地址
    protected String httpProxyHost;
    //代理端口
    protected int httpProxyPort;
    //代理用户名
    protected String httpProxyUsername;
    //代理密码
    protected String httpProxyPassword;

    //https请求所需的证书（PKCS12）地址，请使用绝对路径
    private String keystorePath;
    //证书对应的密码
    private String storePassword;


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
     * 代理用户名
     * @return 代理用户名
     */
    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    /**
     *  代理密码
     * @return 代理密码
     */
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    /**
     * https请求所需的证书（PKCS12）地址，请使用绝对路径
     * @return 证书（PKCS12）地址
     */
    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
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
}
