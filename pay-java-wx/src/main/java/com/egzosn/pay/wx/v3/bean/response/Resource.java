package com.egzosn.pay.wx.v3.bean.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *  通知资源数据
 * json格式，见示例
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class Resource {


    /**
     * 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM。
     */
    private String algorithm;
    /**
     * Base64编码后的开启/停用结果数据密文。
     */
    private String ciphertext;
    /**
     * 附加数据。
     */
    @JSONField(name = "associated_data")
    private String associatedData;

    /**
     * 原始回调类型。
     */
    @JSONField(name = "original_type")
    private String originalType;
    /**
     * 加密使用的随机串。
     */
    private String nonce;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getAssociatedData() {
        return associatedData;
    }

    public void setAssociatedData(String associatedData) {
        this.associatedData = associatedData;
    }

    public String getOriginalType() {
        return originalType;
    }

    public void setOriginalType(String originalType) {
        this.originalType = originalType;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
