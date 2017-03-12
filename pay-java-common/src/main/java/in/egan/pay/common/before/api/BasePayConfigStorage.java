package in.egan.pay.common.before.api;

import in.egan.pay.common.bean.MsgType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 支付基础配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
@Deprecated
public abstract class BasePayConfigStorage implements PayConfigStorage{


    // ali rsa_private 商户私钥，pkcs8格式
    //wx api_key 商户密钥
    protected volatile  String keyPrivate ;
    // 支付公钥
    protected volatile  String keyPublic;
    //异步回调地址
    protected volatile  String notifyUrl;
    //同步回调地址
    protected volatile  String returnUrl;;
    //签名加密类型
    protected volatile  String signType;
    //字符类型
    protected volatile  String inputCharset;


    //支付类型 aliPay 支付宝， wxPay微信..等等，开发者自定义，唯一
    protected volatile  String payType;
    /**
     * 消息来源类型
     * @see PayConsts#MSG_XML
     * @see PayConsts#MSG_TEXT
     * @see PayConsts#MSG_JSON
     */
    protected volatile  MsgType msgType;


    // 访问令牌 每次请求其他方法都要传入的值
    protected volatile String accessToken;
    // access token 到期时间时间戳
    protected volatile long expiresTime;
    //授权码锁
    protected Lock accessTokenLock = new ReentrantLock();


    protected volatile  String httpProxyHost;
    protected volatile  int httpProxyPort;
    protected volatile  String httpProxyUsername;
    protected volatile  String httpProxyPassword;

    private boolean isTest = false;

    /**
     * 合作商唯一标识
     * @see #getPartner 代替者
     */
    public String getPid(){
        throw null;
    }

    @Override
    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }



    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String getSignType() {
        return signType;
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



    @Override
    public String getToken() {
        return null;
    }


    /**
     * 支付类型 自定义
     * 这里暂定 aliPay 支付宝， wxPay微信支付
     * @return
     */
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }


    @Override
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public Lock getAccessTokenLock() {
        return this.accessTokenLock;
    }

    @Override
    public long getExpiresTime() {
        return expiresTime;
    }

    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }


    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 600) * 1000L;
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, long expiresTime) {
        this.accessToken = accessToken;
        this.expiresTime = expiresTime;
    }

    @Override
    public void expireAccessToken() {
        this.expiresTime = 0;
    }
    @Override
    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
