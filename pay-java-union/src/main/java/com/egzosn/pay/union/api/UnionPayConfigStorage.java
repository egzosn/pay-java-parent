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

    /**
     * 中级证书路径
     */
    private String acpMiddleCert;
    /**
     * 根证书路径
     */
    private String acpRootCert;

    /**
     * 私钥证书是否已经初始化
     *  默认没有
     */
    private boolean keyPrivateInit = false;

    /**
     * 公钥证书是否已经初始化
     *  默认没有
     */
    private boolean keyPublicInit = false;


    /**
     * 设置私钥证书
     * @param certificatePath 私钥证书地址
     *  私钥证书密码 {@link #setKeyPrivateCertPwd(String)}
     */
    public void setKeyPrivateCert(String certificatePath){
        super.setKeyPrivate(certificatePath);
    }

    /**
     * 设置中级证书
     * @param certificatePath 证书地址
     */
    public void setAcpMiddleCert(String certificatePath){
        this.acpMiddleCert = certificatePath;
    }
    /**
     * 设置根证书路径
     * @param certificatePath 证书路径
     */
    public void setAcpRootCert(String certificatePath){
        this.acpRootCert = certificatePath;
    }

    public String getAcpMiddleCert() {
        return acpMiddleCert;
    }

    public String getAcpRootCert() {
        return acpRootCert;
    }

    /**
     *
     *  设置私钥证书与证书密码
     * @param keyPrivate 私钥证书与证书对应的密码 格式: D:/certs/acp_test_sign.pfx;000000
     *  替代方法
     * {@link #setKeyPrivateCert(String)}
     * {@link #setKeyPrivateCertPwd(String)}
     */
    @Deprecated
    @Override
    public void setKeyPrivate(String keyPrivate) {
        super.setKeyPrivate(keyPrivate);
        if (isCertSign() && keyPrivate.length() < 1024 && keyPrivate.contains(";")){
            String[] split = keyPrivate.split(";");
            super.setKeyPrivateCertPwd( split[1]);
            super.setKeyPrivate(split[0]);
            getCertDescriptor().initPrivateSignCert(getKeyPrivate(), getKeyPrivateCertPwd(), "PKCS12");
            keyPrivateInit = true;
        }
    }

    /**
     * 设置中级证书与根证书  格式：D:/certs/acp_test_middle.cer;D:/certs/acp_test_root.cer
     * @param keyPublic 中级证书与根证书
     *  替代方法
     * {@link #setAcpRootCert(String)}
     * {@link #setAcpMiddleCert(String)}
     */
    @Deprecated
    @Override
    public void setKeyPublic(String keyPublic) {
        super.setKeyPublic(keyPublic);
        if (isCertSign() && keyPublic.length() < 1024 ){
            String[] split = keyPublic.split(";");
            getCertDescriptor().initPublicCert(split[0]);
            getCertDescriptor().initRootCert(split[1]);
            keyPublicInit = true;
        }
    }

    @Override
    public String getAppid() {
        return null;
    }

    /**
     * @return 合作者id
     * @see #getPid()
     */
    @Deprecated
    public String getPartner() {
        return merId;
    }


    /**
     * 设置合作者id
     *
     * @param partner 合作者id
     * @see #setPid(String)
     */
    @Deprecated
    public void setPartner(String partner) {
        this.merId = partner;
    }

    @Override
    public String getPid() {
        return merId;
    }

    public void setPid (String pid) {
        this.merId = pid;
    }
    @Override
    public String getSeller() {
        return seller;
    }

    public void setSeller (String seller) {
        this.seller = seller;
    }

    public String getMerId() {
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

    public boolean isKeyPrivateInit() {
        return keyPrivateInit;
    }

    public boolean isKeyPublicInit() {
        return keyPublicInit;
    }
}
