package in.egan.pay.common.http;


/**
 * HTTP 配置
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/3/3 20:48
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


    /**
     * http代理地址
     * @return
     */
    public String getHttpProxyHost() {
        return httpProxyHost;
    }


    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    /**
     *   代理端口
     * @return
     */
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }
    /**
     * 代理用户名
     * @return
     */
    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    /**
     *  代理密码
     * @return
     */
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }


}
