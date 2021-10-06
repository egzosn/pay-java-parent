package com.egzosn.pay.common.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 支付订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2016/10/19 22:34
 *  </pre>
 */
public class PayOrder extends AssistOrder {
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
     * 银行卡类型
     */
    private String bankType;
    /**
     * 设备信息
     */
    @Deprecated
    private String deviceInfo;
    /**
     * 支付创建ip
     */
    @Deprecated
    private String spbillCreateIp;
    /**
     * 付款条码串,人脸凭证，有关支付代码相关的，
     */
    private String authCode;
    /**
     * 微信专用，，，，
     * WAP支付链接
     */
    @Deprecated
    private String wapUrl;
    /**
     * 微信专用，，，，
     * WAP支付网页名称
     */
    @Deprecated
    private String wapName;
    /**
     * 用户唯一标识
     * 微信含 sub_openid 字段
     * 支付宝 buyer_id
     */
    private String openid;

    /**
     * 支付币种
     */
    private CurType curType;
    /**
     * 订单过期时间
     */
    private Date expirationTime;


    public PayOrder() {
    }


    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo) {
        this(subject, body, price, outTradeNo, null);
    }

    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        this.subject = StringUtils.tryTrim(subject);
        this.body = StringUtils.tryTrim(body);
        this.price = price;
        setOutTradeNo(StringUtils.tryTrim(outTradeNo));
        setTransactionType(transactionType);
    }


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

}
