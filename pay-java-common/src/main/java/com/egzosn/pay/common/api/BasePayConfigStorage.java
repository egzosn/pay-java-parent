package com.egzosn.pay.common.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 支付基础配置存储
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017/3/5 20:33
 *  </pre>
 */
public abstract class BasePayConfigStorage implements PayConfigStorage {

    private Object attach;

    /**
     * 应用私钥，rsa_private pkcs8格式 生成签名时使用
     */
    private String keyPrivate;

    /**
     * 支付平台公钥(签名校验使用)
     */
    private String keyPublic;
    /**
     * 异步回调地址
     */
    private String notifyUrl;
    /**
     * 同步回调地址，支付完成后展示的页面
     */
    private String returnUrl;
    /**
     * 签名加密类型
     */
    private String signType;
    /**
     * 字符类型
     */
    private String inputCharset;


    /**
     * 支付类型 aliPay 支付宝， wxPay微信..等等，扩展支付模块定义唯一。
     */
    private String payType;


    /**
     * 访问令牌 每次请求其他方法都要传入的值
     */
    private volatile String accessToken;
    /**
     * access token 到期时间时间戳
     */
    private volatile long expiresTime;
    /**
     * 授权码锁
     */
    private Lock accessTokenLock;
    /**
     * 是否为沙箱环境，默认为正式环境
     */
    private boolean isTest = false;

    /**
     * 是否为证书签名
     */
    private boolean certSign = false;

    /**
     * 配置附加信息，可用于预设未提供的参数，这里会覆盖以上所有的配置信息，
     */
    private volatile Map<String, Object> attr;

    @Override
    public Object getAttach() {
        return attach;
    }

    public void setAttach(Object attach) {
        this.attach = attach;
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

    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * 获取access token锁
     *
     * @return access token锁
     */
    public Lock getAccessTokenLock() {
        return this.accessTokenLock;
    }

    /**
     * 强制将access token过期掉
     *
     * @return 过期时间
     */
    public long getExpiresTime() {
        return expiresTime;
    }

    /**
     * 访问令牌是否过期
     *
     * @return true过期
     */
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }


    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        updateAccessToken(accessToken, System.currentTimeMillis() + (expiresInSeconds - 600) * 1000L);
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, long expiresTime) {
        this.accessToken = accessToken;
        this.expiresTime = expiresTime;
    }


    /**
     * 强制将access token过期掉
     */
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

    public boolean isCertSign() {
        return certSign;
    }

    public void setCertSign(boolean certSign) {
        this.certSign = certSign;
    }

    @Override
    public Map<String, Object> getAttrs() {
        if (null == attr) {
            attr = new HashMap<>();
        }
        return attr;
    }

    @Override
    public Object getAttr(String key) {
        return getAttrs().get(key);
    }


    /**
     * 添加配置信息
     *
     * @param key   key
     * @param value 值
     */
    public void addAttr(String key, Object value) {
        getAttrs().put(key, value);
    }
}
