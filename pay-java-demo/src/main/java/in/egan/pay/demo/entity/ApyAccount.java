/*
 * Copyright 2002-2017 the original huodull or egan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package in.egan.pay.demo.entity;

import in.egan.pay.common.bean.MsgType;

/**
 * 支付账户
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:36
 */
//@Table(name = "apy_account")
//@Entity
public class ApyAccount {
    // 支付账号id
//    @Id
//    @GeneratedValue
//    @Column(name = "pay_id")
    private Integer payId;
    // 支付合作id
//    @Column(name = "partner")
    private String partner;
    // 应用id
//    @Column(name = "appid")
    private String appid;
    // 支付公钥
    private String publicKey;
    // 支付私钥
//    @Column(name = "private_key")
    private String privateKey;
    // 回调地址
//    @Column(name = "notify_url")
    private String notifyUrl;
    // 收款账号
//    @Column(name = "seller")
    private String seller;
    // 签名类型
//    @Column(name = "sign_type")
    private String signType;
    //支付类型 aliPay 支付宝， wxPay微信
//    @Enumerated(EnumType.STRING)
//    @Column(name = "pay_type")
    private PayType payType;
    // 消息类型，text,xml
//    @Enumerated(EnumType.STRING)
//    @Column(name = "msg_type")
    private MsgType msgType;

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
}
