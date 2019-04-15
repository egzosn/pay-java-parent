package com.egzosn.pay.common.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单信息
 *
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2016/10/19 22:34
 *  </pre>
 */
public class PayOrder {
    /**
     * 商品名称
     */
    private String subject;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 附加信息
     */
    private String addition;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 支付平台订单号,交易号
     */
    private String tradeNo;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 银行卡类型
     */
    private String bankType;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 支付创建ip
     */
    private String spbillCreateIp;
    /**
     * 付款条码串,人脸凭证，有关支付代码相关的，
     */
    private String authCode;
    /**
     * 微信专用，，，，
     * WAP支付链接
     */
    private String wapUrl;
    /**
     * 微信专用，，，，
     * WAP支付网页名称
     */

    private String wapName;
    /**
     * 用户唯一标识
     *  微信含 sub_openid 字段
     */
    private String openid;
    /**
     * 交易类型
     */
    private TransactionType transactionType;
    /**
     * 支付币种
     */
    private CurType curType;
    /**
     * 订单过期时间
     */
    private Date expirationTime;




    public CurType getCurType() {
        return curType;
    }

    public void setCurType(CurType curType) {
        this.curType = curType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 支付平台订单号,交易号
     * @return 支付平台订单号,交易号
     */
    public String getTradeNo() {
        return tradeNo;
    }
    /**
     * 支付平台订单号,交易号
     * @param tradeNo 支付平台订单号,交易号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     *  获取商户订单号
     * @return 商户订单号
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * 设置商户订单号
     * @param outTradeNo  商户订单号
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public PayOrder() {
    }


    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        this.subject = subject;
        this.body = body;
        this.price = price;
        this.outTradeNo = outTradeNo;
        this.transactionType = transactionType;
    }
    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo) {
        this.subject = subject;
        this.body = body;
        this.price = price;
        this.outTradeNo = outTradeNo;
    }

    public String getWapUrl() {
        return wapUrl;
    }

    public void setWapUrl(String wapUrl) {
        this.wapUrl = wapUrl;
    }

    public String getWapName() {
        return wapName;
    }

    public void setWapName(String wapName) {
        this.wapName = wapName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "PayOrder{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", price=" + price +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", bankType='" + bankType + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", spbillCreateIp='" + spbillCreateIp + '\'' +
                ", authCode='" + authCode + '\'' +
                ", wapUrl='" + wapUrl + '\'' +
                ", wapName='" + wapName + '\'' +
                ", openid='" + openid + '\'' +
                ", transactionType=" + transactionType +
                ", curType=" + curType +
                '}';
    }
}
