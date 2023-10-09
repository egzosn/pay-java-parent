package com.egzosn.pay.wx.v3.api;

import java.io.IOException;
import java.io.InputStream;

import com.egzosn.pay.common.api.BasePayConfigStorage;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.v3.bean.CertEnvironment;
import com.egzosn.pay.wx.v3.utils.AntCertificationUtil;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信配置存储
 *
 * @author egan
 *
 * <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 * </pre>
 */
public class WxPayConfigStorage extends BasePayConfigStorage {


    /**
     * 微信分配的公众账号ID
     */
    private String appId;
    /**
     * 服务商申请的公众号appid。
     */
    private String spAppId;
    /**
     * 服务商户号，由微信支付生成并下发 。
     */
    private String spMchId;
    /**
     * 子商户应用ID, 非必填
     * 子商户申请的公众号appid。
     * 若sub_openid有传的情况下，sub_appid必填，且sub_appid需与sub_openid对应
     * 示例值：wxd678efh567hg6999
     */
    private String subAppId;
    /**
     * 微信支付分配的商户号 合作者id
     */
    private String mchId;
    /**
     * 微信支付分配的子商户号，开发者模式下必填 合作者id
     */
    private String subMchId;
    /**
     * V2 Api密钥
     */
    private String apiKey;

    /**
     * 商户API证书
     * 包含商户的商户号、公司名称、公钥信息
     * 详情 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
     */
    private Object apiClientKeyP12;

    /**
     * 证书存储类型
     */
    private CertStoreType certStoreType;


    /**
     * 证书信息
     */
    private volatile CertEnvironment certEnvironment;

    /**
     * 是否为服务商模式, 默认为false
     */
    private boolean partner = false;
    /**
     * 微信支付分服务服务ID
     */
    private String serviceId;

    @Deprecated
    @Override
    public String getAppid() {
        return appId;
    }


    @Deprecated
    public void setAppid(String appId) {
        this.appId = appId;
    }


    /**
     * 合作商唯一标识
     */
    @Override
    public String getPid() {
        return mchId;
    }


    @Override
    public String getSeller() {
        return mchId;
    }


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
        addAttr("mchId", mchId);
    }

    /**
     * 为商户平台设置的密钥key
     *
     * @return 微信v2密钥
     */
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        addAttr("apiKey", apiKey);
    }

    public void setV3ApiKey(String v3ApiKey) {
        setKeyPrivate(v3ApiKey);
    }
    /**
     * 为商户平台设置的密钥key
     *
     * @return 微信v3密钥
     */
    public String getV3ApiKey() {
        return getKeyPrivate();
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     */
    @Override
    public String getAppId() {
        return appId;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
        addAttr("subAppId", subAppId);
    }

    public String getSpAppId() {
        return getAppId();
    }

    public void setSpAppId(String spAppId) {
        setAppId(spAppId);
        addAttr("spAppId", spAppId);
    }

    public String getSpMchId() {
        return getMchId();
    }

    public void setSpMchId(String spMchId) {
        setMchId(spMchId);
        addAttr("spMchId", spMchId);
    }

    /**
     * 应用id
     * 纠正名称
     *
     * @return 应用id
     * @see #getSubAppId()
     */
    @Deprecated
    public String getSubAppid() {
        return subAppId;
    }

    @Deprecated
    public void setSubAppid(String subAppid) {
        this.subAppId = subAppid;
        addAttr("subAppId", subAppId);
    }


    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
        addAttr("subMchId", subMchId);
    }

    public Object getApiClientKeyP12() {
        return apiClientKeyP12;
    }

    public void setApiClientKeyP12(Object apiClientKeyP12) {
        this.apiClientKeyP12 = apiClientKeyP12;
        addAttr("apiClientKeyP12", apiClientKeyP12);
    }

    public CertStoreType getCertStoreType() {
        return certStoreType;
    }

    public void setCertStoreType(CertStoreType certStoreType) {
        this.certStoreType = certStoreType;
        addAttr("certStoreType", certStoreType);
    }

    public CertEnvironment getCertEnvironment() {
        loadCertEnvironment();
        return certEnvironment;
    }

    public void setCertEnvironment(CertEnvironment certEnvironment) {
        this.certEnvironment = certEnvironment;
    }

    /**
     * 初始化证书信息
     */
    public void loadCertEnvironment() {
        if (null != this.certEnvironment) {
            return;
        }
        try (InputStream apiKeyCert = certStoreType.getInputStream(getApiClientKeyP12())) {
            this.certEnvironment = AntCertificationUtil.initCertification(apiKeyCert, WxConst.CERT_ALIAS, getMchId());
        }
        catch (IOException e) {
            throw new PayErrorException(new PayException("读取证书异常", e.getMessage()));
        }
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
