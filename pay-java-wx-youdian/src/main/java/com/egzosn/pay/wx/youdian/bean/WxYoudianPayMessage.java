package com.egzosn.pay.wx.youdian.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 友店回调信息
 *
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/7/3.20:25
 */

public class WxYoudianPayMessage extends PayMessage {
    //    公众账号ID 	appid 	是 	String(32) 	wx8888888888888888 	微信分配的公众账号ID（企业号corpid即为此appId）
    @JSONField(name = "appid")
    private String appid;
    //    商户号 	mch_id 	是 	String(32) 	1900000109 	微信支付分配的商户号
    @JSONField(name = "mch_id")
    private String mchId;
    //    设备号 	device_info 	否 	String(32) 	013467007045764 	微信支付分配的终端设备号，
    @JSONField(name = "device_info")
    private String deviceInfo;
    //    随机字符串 	nonce_str 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	随机字符串，不长于32位
    @JSONField(name = "nonce_str")
    private String nonceStr;
    //    签名 	sign 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	签名，详见签名算法
    @JSONField(name = "sign")
    private String sign;
    //    签名类型 	sign_type 	否 	String(32) 	HMAC-SHA256 	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JSONField(name = "sign_type")
    private String signType;
    //    业务结果 	result_code 	是 	String(16) 	SUCCESS 	SUCCESS/FAIL
    @JSONField(name = "result_code")
    private String resultCode;
    //    错误代码 	err_code 	否 	String(32) 	SYSTEMERROR 	错误返回的信息描述
    @JSONField(name = "err_code")
    private String errCode;
    //    错误代码描述 	err_code_des 	否 	String(128) 	系统错误 	错误返回的信息描述
    @JSONField(name = "err_code_des")
    private String errCodeDes;
    //    用户标识 	openid 	是 	String(128) 	wxd930ea5d5a258f4f 	用户在商户appid下的唯一标识
    @JSONField(name = "openid")
    private String openid;
    //    是否关注公众账号 	is_subscribe 	是 	String(1) 	Y 	用户是否关注公众账号，Y-关注，N-未关注
    @JSONField(name = "is_subscribe")
    private String isSubscribe;
    //    交易类型 	trade_type 	是 	String(16) 	JSAPI 	JSAPI、NATIVE、APP
    @JSONField(name = "trade_type")
    private String tradeType;
    //    付款银行 	bank_type 	是 	String(16) 	CMC 	银行类型，采用字符串类型的银行标识，银行类型见银行列表
    @JSONField(name = "bank_type")
    private String bankType;
    //    订单金额 	total_fee 	是 	Int 	100 	订单总金额，单位为分
    @JSONField(name = "total_fee")
    private BigDecimal totalFee;
    //    应结订单金额 	settlement_total_fee 	否 	Int 	100 	应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
    @JSONField(name = "settlement_total_fee")
    private BigDecimal settlementTotalFee;
    //    货币种类 	fee_type 	否 	String(8) 	CNY 	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    @JSONField(name = "fee_type")
    private String feeType;
    //    现金支付金额 	cash_fee 	是 	Int 	100 	现金支付金额订单现金支付金额，详见支付金额
    @JSONField(name = "cash_fee")
    private BigDecimal cashFee;
    //    现金支付货币类型 	cash_fee_type 	否 	String(16) 	CNY 	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    @JSONField(name = "cash_fee_type")
    private String cashFeeType;
    //    总代金券金额 	coupon_fee 	否 	Int 	10 	代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
    @JSONField(name = "coupon_fee")
    private BigDecimal couponFee;
    //    代金券使用数量 	coupon_count 	否 	Int 	1 	代金券使用数量
    @JSONField(name = "coupon_count")
    private Integer couponCount;
    //    代金券类型 	coupon_type_$n 	否 	String 	CASH    CASH--充值代金券    NO_CASH---非充值代金券    并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
    @JSONField(name = "coupon_type_$0")
    private String couponType0;
    //    代金券ID 	coupon_id_$n 	否 	String(20) 	10000 	代金券ID,$n为下标，从0开始编号
    @JSONField(name = "coupon_id_$0")
    private String couponId0;
    //    单个代金券支付金额 	coupon_fee_$n 	否 	Int 	100 	单个代金券支付金额,$n为下标，从0开始编号
    @JSONField(name = "coupon_fee_$0")
    private Integer couponFee0;
    //    微信支付订单号 	transaction_id 	是 	String(32) 	1217752501201407033233368018 	微信支付订单号
    @JSONField(name = "transaction_id")
    private String transactionId;
    //    商户订单号 	out_trade_no 	是 	String(32) 	1212321211201407033568112322 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    //    商家数据包 	attach 	否 	String(128) 	123456 	商家数据包，原样返回
    @JSONField(name = "attach")
    private String attach;
    //    支付完成时间 	time_end 	是 	String(14) 	20141030133525 	支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    @JSONField(name = "time_end", format="yyyyMMddHHmmss")
    private Date timeEnd;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    @Override
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(BigDecimal settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getCashFee() {
        return cashFee;
    }

    public void setCashFee(BigDecimal cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public BigDecimal getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(BigDecimal couponFee) {
        this.couponFee = couponFee;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public String getCouponType0() {
        return couponType0;
    }

    public void setCouponType0(String couponType0) {
        this.couponType0 = couponType0;
    }

    public String getCouponId0() {
        return couponId0;
    }

    public void setCouponId0(String couponId0) {
        this.couponId0 = couponId0;
    }

    public Integer getCouponFee0() {
        return couponFee0;
    }

    public void setCouponFee0(Integer couponFee0) {
        this.couponFee0 = couponFee0;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public static final WxYoudianPayMessage create(Map<String, Object> message) {
        WxYoudianPayMessage payMessage = new JSONObject(message).toJavaObject(WxYoudianPayMessage.class);
        payMessage.setPayMessage(message);
        return payMessage;
    }

}
