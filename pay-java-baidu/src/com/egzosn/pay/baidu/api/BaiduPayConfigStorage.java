package com.egzosn.pay.baidu.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

public class BaiduPayConfigStorage extends BasePayConfigStorage {
    private String appid;
    private String dealId;
    
    @Override
    public String getAppid() {
        return this.appid;
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
    
    public void setAppid(String appid) {
        this.appid = appid;
    }
}
