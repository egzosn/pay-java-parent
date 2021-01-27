package com.egzosn.pay.baidu.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

public class BaiduPayConfigStorage extends BasePayConfigStorage {
    private String appId;
    private String dealId;

    @Override
    @Deprecated
    public String getAppid() {
        return this.appId;
    }

    @Override
    public String getPid() {
        return getDealId();
    }

    @Override
    public String getSeller() {
        throw new UnsupportedOperationException("不支持");
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAppKey() {
        return this.getKeyPrivate();
    }

    public void setAppKey(String appKey) {
        setKeyPrivate(appKey);
    }

    @Override
    public String getKeyPublic() {
        return super.getKeyPrivate();
    }

    @Override
    public void setKeyPublic(String keyPublic) {
        super.setKeyPublic(keyPublic);
    }

    public void setAppid(String appId) {
        this.appId = appId;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
