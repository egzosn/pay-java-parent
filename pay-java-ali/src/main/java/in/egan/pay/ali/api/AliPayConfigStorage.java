package in.egan.pay.ali.api;

import in.egan.pay.common.api.PayConfigStorage;

/**
 * 支付客户端配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class AliPayConfigStorage implements PayConfigStorage{

    // 商户PID
    public  volatile String partner ;
    // 商户收款账号
    public  volatile String seller;
    // 商户私钥，pkcs8格式
    public  volatile String rsa_private ;
    // 支付宝公钥
    public  volatile String rsa_public;
    //回调地址
    private  volatile String notify_url;
    //加密类型
    private  volatile String sign_type;
    private  volatile String ali_public_key;
    private  volatile String input_charset;

    protected volatile String http_proxy_host;
    protected volatile int http_proxy_port;
    protected volatile String http_proxy_username;
    protected volatile String http_proxy_password;
    protected volatile String logPath;
    protected volatile Boolean showLog;



    public String getAli_public_key() {
        return ali_public_key;
    }

    public void setAli_public_key(String ali_public_key) {
        this.ali_public_key = ali_public_key;
    }

    @Override
    public String getInput_charset() {
        return input_charset;
    }

    @Override
    public String getSecretKey() {
        return ali_public_key;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    @Override
    public String getAppid() {
        return null;
    }

    @Override
    public String getPartner() {
        return partner;
    }

    @Override
    public String getToken() {
        return null;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRsa_private() {
        return rsa_private;
    }

    @Override
    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    @Override
    public Boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(Boolean showLog) {
        this.showLog = showLog;
    }

    public void setRsa_private(String rsa_private) {
        this.rsa_private = rsa_private;
    }

    public String getRsa_public() {
        return rsa_public;
    }

    public void setRsa_public(String rsa_public) {
        this.rsa_public = rsa_public;
    }

    @Override
    public String getNotify_url() {
        return notify_url;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    @Override
    public String getSign_type() {
        return sign_type;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    @Override
    public String getHttp_proxy_host() {
        return http_proxy_host;
    }

    public void setHttp_proxy_host(String http_proxy_host) {
        this.http_proxy_host = http_proxy_host;
    }

    @Override
    public int getHttp_proxy_port() {
        return http_proxy_port;
    }

    public void setHttp_proxy_port(int http_proxy_port) {
        this.http_proxy_port = http_proxy_port;
    }

    @Override
    public String getHttp_proxy_username() {
        return http_proxy_username;
    }

    public void setHttp_proxy_username(String http_proxy_username) {
        this.http_proxy_username = http_proxy_username;
    }

    @Override
    public String getHttp_proxy_password() {
        return http_proxy_password;
    }

    public void setHttp_proxy_password(String http_proxy_password) {
        this.http_proxy_password = http_proxy_password;
    }
}
