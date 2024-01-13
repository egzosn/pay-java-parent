package com.egzosn.pay.paypal.api;

import java.util.concurrent.locks.ReentrantLock;

import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * 贝宝支付配置存储
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2018-4-8 22:11:42
 */
public class PayPalConfigStorage extends BasePayConfigStorage {

    private String clientId;

    /**
     * 回调验签使用
     */
    private String webHookId;


    @Override
    @Deprecated
    public String getAppid() {
        return clientId;
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     */
    @Override
    public String getAppId() {
        return clientId;
    }

    @Override
    public String getPid() {
        return clientId;
    }

    @Override
    public String getSeller() {
        return clientId;
    }

    public String getClientID() {
        return clientId;
    }

    public void setClientID(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getClientSecret() {
        return getKeyPrivate();
    }

    public void setClientSecret(String clientSecret) {
        setKeyPrivate(clientSecret);
    }


    /**
     * 设置取消页面的url
     * <pre>
     * 注意：这里不是异步回调的通知
     * IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * </pre>
     *
     * @param cancelUrl 取消页面的url
     */
    public void setCancelUrl(String cancelUrl) {
        setNotifyUrl(cancelUrl);
    }

    /**
     * 获取取消页面的url
     * <pre>
     * 注意：这里不是异步回调的通知
     * </pre>
     *
     * @return 取消页面的url
     */
    public String getCancelUrl() {
        return getNotifyUrl();
    }

    public PayPalConfigStorage() {
        setAccessTokenLock(new ReentrantLock());
    }

    public String getWebHookId() {
        return webHookId;
    }

    public void setWebHookId(String webHookId) {
        this.webHookId = webHookId;
    }
}
