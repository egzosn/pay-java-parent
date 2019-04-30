

package com.egzosn.pay.demo.entity;

import com.egzosn.pay.common.bean.MsgType;

//import javax.persistence.*;

/**
 * 支付账户
 * @author: egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:36
 */
//@Table(name = "apy_account")
//@Entity
public class ApyAccount {
    // 支付账号id
//    @Id
//    @GeneratedValue
//    @Column(name = "pay_id")
    private Integer payId;
    // 支付合作id,商户id，差不多是支付平台的账号或id
//    @Column(name = "partner")
    private String partner;
    // 应用id
//    @Column(name = "appid")
    private String appid;
    //   支付平台公钥(签名校验使用)，sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
    private String publicKey;
    // 应用私钥(生成签名)
//    @Column(name = "private_key")
    private String privateKey;
    // 异步回调地址
//    @Column(name = "notify_url")
    private String notifyUrl;
    // 同步回调地址
//    @Column(name = "return_url")
    private String returnUrl;
    // 收款账号
//    @Column(name = "seller")
    private String seller;

    //请求证书地址，请使用绝对路径
//    @Column(name = "keystore_path")
    private String keystorePath;
    //证书对应的密码
//    @Column(name = "store_password")
    private String storePassword;


    // 签名类型
//    @Column(name = "sign_type")
    private String signType;
    // 编码类型 枚举值，字符编码 utf-8,gbk等等
//    @Column(name = "input_charset")
    private String inputCharset;
    //支付类型,aliPay：支付宝，wxPay：微信, youdianPay: 友店微信,此处开发者自定义对应com.egzosn.pay.demo.entity.PayType枚举值
//    @Enumerated(EnumType.STRING)
//    @Column(name = "pay_type")
    private PayType payType;
    // 消息类型，text,xml,json
//    @Enumerated(EnumType.STRING)
//    @Column(name = "msg_type")
    private MsgType msgType;
    //是否为测试环境
    private boolean isTest = false;
    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public boolean isTest() {
        return isTest;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    @Override
    public String toString() {
        return "ApyAccount{" +
                "payId=" + payId +
                ", partner='" + partner + '\'' +
                ", appid='" + appid + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", seller='" + seller + '\'' +
                ", signType='" + signType + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", payType=" + payType +
                ", msgType=" + msgType +
                '}';
    }
}
