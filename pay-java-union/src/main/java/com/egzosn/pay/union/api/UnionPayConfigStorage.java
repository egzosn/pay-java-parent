package com.egzosn.pay.union.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 *  <pre>
    create 2017 2017/11/4 0004
 * </pre>
 */
public class UnionPayConfigStorage extends BasePayConfigStorage {

    /**
     * 商户号
     */
    public volatile String merId;

    /**
     * 商户收款账号
     */
    public volatile String seller;
    //公钥
    private volatile String aliPublicKey;



    public String getAliPublicKey () {
        return aliPublicKey;
    }

    public void setAliPublicKey (String aliPublicKey) {
        setKeyPublic(aliPublicKey);
        this.aliPublicKey = aliPublicKey;
    }


    @Override
    public String getAppid () {
        return null;
    }

    /**
     * @return 合作者id
     * @see #getPid()
     */
    @Deprecated
    @Override
    public String getPartner () {
        return merId;
    }


    /**
     * 设置合作者id
     *
     * @param partner 合作者id
     * @see #setPid(String)
     */
    @Deprecated
    public void setPartner (String partner) {
        this.merId = partner;
    }

    @Override
    public String getPid () {
        return merId;
    }

    public void setPid (String pid) {
        this.merId = pid;
    }
    @Override
    public String getSeller () {
        return seller;
    }

    public void setSeller (String seller) {
        this.seller = seller;
    }

    public String getMerId () {
        return merId;
    }

    public void setMerId (String merId) {
        this.merId = merId;
    }

}
