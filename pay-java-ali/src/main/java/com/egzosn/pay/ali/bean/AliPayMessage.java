package com.egzosn.pay.ali.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 支付宝回调信息
 *
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/6/30.19:19
 */

public class AliPayMessage extends PayMessage {
    //notify_time 	通知时间 	Date 	是 	通知的发送时间。格式为yyyy-MM-dd HH:mm:ss 	2015-14-27 15:45:58
    @JSONField(name = "notify_time")
    private Date notifyTime;
    //notify_type 	通知类型 	String(64) 	是 	通知的类型 	trade_status_sync
    @JSONField(name = "notify_type")
    private String notifyType;
    //    notify_id 	通知校验ID 	String(128) 	是 	通知校验ID 	ac05099524730693a8b330c5ecf72da9786
    @JSONField(name = "notify_id")
    private String notifyId;
    //    app_id 	支付宝分配给开发者的应用Id 	String(32) 	是 	支付宝分配给开发者的应用Id 	2014072300007148
    @JSONField(name = "app_id")
    private String appId;
    //    charset 	编码格式 	String(10) 	是 	编码格式，如utf-8、gbk、gb2312等 	utf-8
    private String charset;
    //    version 	接口版本 	String(3) 	是 	调用的接口版本，固定为：1.0 	1.0
    private String version;
    //    sign_type 	签名类型 	String(10) 	是 	商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2 	RSA2
    @JSONField(name = "sign_type")
    private String signType;
    //    sign 	签名 	String(256) 	是 	请参考文末 异步返回结果的验签 	601510b7970e52cc63db0f44997cf70e
    private String sign;
    //    trade_no 	支付宝交易号 	String(64) 	是 	支付宝交易凭证号 	2013112011001004330000121536
    @JSONField(name = "trade_no")
    private String tradeNo;
    //    out_trade_no 	商户订单号 	String(64) 	是 	原支付请求的商户订单号 	6823789339978248
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    //    out_biz_no 	商户业务号 	String(64) 	否 	商户业务ID，主要是退款通知中返回退款申请的流水号 	HZRF001
    @JSONField(name = "out_biz_no")
    private String outBizNo;
    //    buyer_id 	买家支付宝用户号 	String(16) 	否 	买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字 	2088102122524333
    @JSONField(name = "buyer_id")
    private String buyerId;
    //    buyer_logon_id 	买家支付宝账号 	String(100) 	否 	买家支付宝账号 	15901825620
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    //    seller_id 	卖家支付宝用户号 	String(30) 	否 	卖家支付宝用户号 	2088101106499364
    @JSONField(name = "seller_id")
    private String sellerId;
    //    seller_email 	卖家支付宝账号 	String(100) 	否 	卖家支付宝账号 	zhuzhanghu@alitest.com
    @JSONField(name = "seller_email")
    private String sellerEmail;
    //    trade_status 	交易状态 	String(32) 	否 	交易目前所处的状态，见下张表 交易状态说明 	TRADE_CLOSED
    @JSONField(name = "trade_status")
    private String tradeStatus;
    //    total_amount 	订单金额 	Number(9,2) 	否 	本次交易支付的订单金额，单位为人民币（元） 	20
    @JSONField(name = "total_amount")
    private BigDecimal totalAmount;
    //    receipt_amount 	实收金额 	Number(9,2) 	否 	商家在交易中实际收到的款项，单位为元 	15
    @JSONField(name = "receipt_amount")
    private BigDecimal receiptAmount;
    //    invoice_amount 	开票金额 	Number(9,2) 	否 	用户在交易中支付的可开发票的金额 	10.00
    @JSONField(name = "invoice_amount")
    private BigDecimal invoiceAmount;
    //    buyer_pay_amount 	付款金额 	Number(9,2) 	否 	用户在交易中支付的金额 	13.88
    @JSONField(name = "buyer_pay_amount")
    private BigDecimal buyerPayAmount;
    //    point_amount 	集分宝金额 	Number(9,2) 	否 	使用集分宝支付的金额 	12.00
    @JSONField(name = "point_amount")
    private BigDecimal pointAmount;
    //    refund_fee 	总退款金额 	Number(9,2) 	否 	退款通知中，返回总退款金额，单位为元，支持两位小数 	2.58
    @JSONField(name = "refund_fee")
    private BigDecimal refundFee;
    //    subject 	订单标题 	String(256) 	否 	商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来 	当面付交易
    @JSONField(name = "subject")
    private String subject;
    //    body 	商品描述 	String(400) 	否 	该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来 	当面付交易内容
    @JSONField(name = "body")
    private String body;
    //    gmt_create 	交易创建时间 	Date 	否 	该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss 	2015-04-27 15:45:57
    @JSONField(name = "gmt_create")
    private Date gmtCreate;
    //    gmt_payment 	交易付款时间 	Date 	否 	该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss 	2015-04-27 15:45:57
    @JSONField(name = "gmt_payment")
    private Date gmtPayment;
    //    gmt_refund 	交易退款时间 	Date 	否 	该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S 	2015-04-28 15:45:57.320
    @JSONField(name = "gmt_refund")
    private Date gmtRefund;
    //    gmt_close 	交易结束时间 	Date 	否 	该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss 	2015-04-29 15:45:57
    @JSONField(name = "gmt_close")
    private Date gmtClose;
    //    fund_bill_list 	支付金额信息 	String(512) 	否 	支付成功的各个渠道金额信息，详见下表 资金明细信息说明 	[{“amount”:“15.00”,“fundChannel”:“ALIPAYACCOUNT”}]
    @JSONField(name = "fund_bill_list")
    private String fundBillList;
    //    passback_params 	回传参数 	String(512) 	否 	公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝 	merchantBizType%3d3C%26merchantBizNo%3d2016010101111
    @JSONField(name = "passback_params")
    private String passbackParams;
    //    voucher_detail_list 	优惠券信息 	String 	否 	本交易支付时所使用的所有优惠券信息，详见下表 优惠券信息说明 	[{“amount”:“0.20”,“merchantContribute”:“0.00”,“name”:“一键创建券模板的券名称”,“otherContribute”:“0.20”,“type”:“ALIPAY_DISCOUNT_VOUCHER”,“memo”:“学生卡8折优惠”]
    @JSONField(name = "voucher_detail_list")
    private String voucherDetailList;

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public static final AliPayMessage create(Map<String, Object> message){
        AliPayMessage payMessage = new JSONObject(message).toJavaObject(AliPayMessage.class);
        payMessage.setPayMessage(message);
        return payMessage;
    }

}
