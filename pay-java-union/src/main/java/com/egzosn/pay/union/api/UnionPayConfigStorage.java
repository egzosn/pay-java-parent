package com.egzosn.pay.union.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;


/**
 * @author Actinia
 *
 *  <pre>
 * email hayesfu@qq.com
 *   create 2017 2017/11/4 0004
 * </pre>
 */
public class UnionPayConfigStorage extends BasePayConfigStorage {


    /**
     * 商户号
     */
    private volatile String merId;



    /**
     * 商户收款账号
     */
    private volatile String seller;

    private volatile String version = "5.1.0";
    /**
     * 0：普通商户直连接入
     * 1： 收单机构
     * 2：平台类商户接入
     */
    private volatile String accessType = "0";


    @Override
    public void setKeyPrivate(String keyPrivate) {
        super.setKeyPrivate(keyPrivate);
        if (isCertSign() && keyPrivate.length() < 1024 && keyPrivate.contains(";")){
            String[] split = keyPrivate.split(";");
            setKeyPrivateCertPwd( split[1]);
            super.setKeyPrivate(split[0]);
            getCertDescriptor().initPrivateSignCert(getKeyPrivate(), getKeyPrivateCertPwd(), "PKCS12");
        }
    }


    @Override
    public void setKeyPublic(String keyPublic) {
        super.setKeyPublic(keyPublic);
        if (isCertSign() && keyPublic.length() < 1024 ){
            String[] split = keyPublic.split(";");
            getCertDescriptor().initPublicCert(split[0]);
            getCertDescriptor().initRootCert(split[1]);
        }
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}
