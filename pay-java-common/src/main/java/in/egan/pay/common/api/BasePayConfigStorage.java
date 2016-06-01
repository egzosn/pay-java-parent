package in.egan.pay.common.api;

/**
 * 支付基础配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public abstract class BasePayConfigStorage implements PayConfigStorage{


    // ali rsa_private 商户私钥，pkcs8格式
    //wx api_key 商户密钥
    protected volatile String keyPrivate ;
    // 支付公钥
    protected volatile String keyPublic;
    //回调地址
    protected volatile String notifyUrl;
    //加密类型
    protected volatile String signType;
    //字符类型
    protected volatile String inputCharset;

    //日志的存放路径
    @Deprecated
    protected volatile String logPath;
    //是否显示日志
    protected volatile Boolean showLog;

    //支付类型 0支付宝， 1微信
    protected volatile Short payType;
    protected volatile String httpProxyHost;
    protected volatile int httpProxyPort;
    protected volatile String httpProxyUsername;
    protected volatile String httpProxyPassword;




    @Override
    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
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

    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String getSignType() {
        return signType;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    @Override
    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    @Override
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }


    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    @Override
    public String getKeyPrivate() {
        return keyPrivate;
    }

    public void setKeyPrivate(String keyPrivate) {
        this.keyPrivate = keyPrivate;
    }

    @Override
    public String getKeyPublic() {
        return keyPublic;
    }

    public void setKeyPublic(String keyPublic) {
        this.keyPublic = keyPublic;
    }

    public Boolean getShowLog() {
        return showLog;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }
}
