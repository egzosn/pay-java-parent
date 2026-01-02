package com.egzosn.pay.baidu.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

public class BaiduPayConfigStorage extends BasePayConfigStorage {

    private String appid;

    private String dealId;
    /**
     * 支付平台公钥(签名校验使用)
     */
    private String keyPublic;

    @Override
    public String getAppid() {
        return this.appid;
    }

    @Override
    public String getAppId() {
        return this.appid;
    }

    @Override
    public String getPid() {
        return getDealId();
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAppKey() {
        return this.appid;
    }

    public void setAppKey(String appKey) {
        this.setAppid(appKey);
    }

    @Override
    public String getKeyPublic() {
        return keyPublic;
    }

    @Override
    public void setKeyPublic(String keyPublic) {
        this.keyPublic = keyPublic;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

}
