package in.egan.pay.common.api;

import in.egan.pay.common.bean.MsgType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 支付基础配置存储
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/3/5 20:33
 */
public abstract class BasePayConfigStorage implements PayConfigStorage{


    // ali rsa_private 商户私钥，pkcs8格式
    //wx api_key 商户密钥
    private volatile  String keyPrivate ;
    // 支付公钥
    private volatile  String keyPublic;
    //异步回调地址
    private volatile  String notifyUrl;
    //同步回调地址
    private volatile  String returnUrl;;
    //签名加密类型
    private volatile  String signType;
    //字符类型
    private volatile  String inputCharset;


    //支付类型 aliPay 支付宝， wxPay微信..等等，开发者自定义，唯一
    private volatile  String payType;

    /**
     * 消息来源类型
     */
    private volatile MsgType msgType;


    // 访问令牌 每次请求其他方法都要传入的值
    private volatile String accessToken;
    // access token 到期时间时间戳
    private volatile long expiresTime;
    //授权码锁
    private Lock accessTokenLock = new ReentrantLock();

    private boolean isTest = false;


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

    @Override
    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    @Override
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
    public String getToken() {
        return null;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public void setAccessTokenLock(Lock accessTokenLock) {
        this.accessTokenLock = accessTokenLock;
    }

    @Override
    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
