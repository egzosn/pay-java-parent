package com.egzosn.pay.union.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 银联回调信息
 *
 * @author egan
 *         <pre>
 *                         email egzosn@gmail.com
 *                         date 2019/7/3.22:07
 *                 </pre>
 */

public class UnionPayMessage extends PayMessage {
//    查询流水号	queryId	AN20..21	M-必填	消费交易的流水号，供后续查询用
    private String queryId;
    //    交易币种	currencyCode	AN3	M-必填	默认为156
    private String currencyCode;
    //    交易传输时间	traceTime	MMDDhhmmss	M-必填
    @JSONField(name = "traceTime", format = "MMDDhhmmss")
    private String traceTime;
    //    签名	signature	ANS1..1024	M-必填
    private String signature;
    //    签名方法	signMethod	N2	M-必填
    private String signMethod;
    //    清算币种	settleCurrencyCode	AN3	M-必填
    private String settleCurrencyCode;
    //    清算金额	settleAmt	N1..12	M-必填
    private BigDecimal settleAmt;
    //    清算日期	settleDate	MMDD	M-必填
    @JSONField(name = "settleDate", format = "MMDD")
    private String settleDate;
    //    系统跟踪号	traceNo	N6	M-必填
    private String traceNo;
    //    应答码	respCode	AN2	M-必填
    private String respCode;
    //    应答信息	respMsg	ANS1..256	M-必填
    private String respMsg;
    //    兑换日期	exchangeDate	MMDD	C-按条件必填	交易成功，交易币种和清算币种不一致的时候返回
    @JSONField(name = "exchangeDate", format = "MMDD")
    private String exchangeDate;
    //    签名公钥证书	signPubKeyCert	AN1..2048	C-按条件必填	使用RSA签名方式时必选，此域填写银联签名公钥证书。
    private String signPubKeyCert;
    //    清算汇率	exchangeRate	N8	C-按条件必填	交易成功，交易币种和清算币种不一致的时候返回
    private String exchangeRate;
    //    账号	accNo	AN1..1024	C-按条件必填	根据商户配置返回
    private String accNo;
    //    支付方式	payType	N4	C-按条件必填	根据商户配置返回
    private String payType;
    //    支付卡标识	payCardNo	ANS1..19	C-按条件必填	移动支付交易时，根据商户配置返回
    private String payCardNo;
    //    支付卡类型	payCardType	N2	C-按条件必填	根据商户配置返回
    private String payCardType;
    //    支付卡名称	payCardIssueName	ANS1..64	C-按条件必填	移动支付交易时，根据商户配置返回
    private String payCardIssueName;
    //    版本号	version	NS5	R-需要返回
    private String version;
    //    绑定标识号	bindId	ANS1..128	R-需要返回	绑定支付时，根据商户配置返回
    private String bindId;
    //    编码方式	encoding	ANS1..20	R-需要返回
    private String encoding;
    //    产品类型	bizType	N6	R-需要返回
    private String bizType;
    //    订单发送时间	txnTime	YYYYMMDDhhmmss	R-需要返回
    @JSONField(name = "txnTime", format = "YYYYMMDDhhmmss")
    private String txnTime;
    //    交易金额	txnAmt	N1..12	R-需要返回
    private BigDecimal txnAmt;
    //    交易类型	txnType	N2	R-需要返回
    private String txnType;
    //    交易子类	txnSubType	N2	R-需要返回
    private String txnSubType;
    //    接入类型	accessType	N1	R-需要返回	0：商户直连接入	1：收单机构接入2：平台商户接入
    private String accessType;
    //    请求方保留域	reqReserved	ANS1..1024	R-需要返回
    private String reqReserved;
    //    商户代码	merId	AN15	R-需要返回
    private String merId;
    //    商户订单号	orderId	AN8..40	R-需要返回	商户订单号，不能含“-”或“_”;    商户自定义，同一交易日期内不可重复;    商户代码merId、商户订单号orderId、订单发送时间txnTime三要素唯一确定一笔交易。  保留域 reserved    ANS1..2048O-    选填 查看详情
    private String orderId;
    //    分账域 accSplitData    ANS1..512O-    选填 查看详情
    private String accSplitData;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public BigDecimal getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(BigDecimal settleAmt) {
        this.settleAmt = settleAmt;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getSignPubKeyCert() {
        return signPubKeyCert;
    }

    public void setSignPubKeyCert(String signPubKeyCert) {
        this.signPubKeyCert = signPubKeyCert;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    @Override
    public String getPayType() {
        return payType;
    }

    @Override
    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(String payCardNo) {
        this.payCardNo = payCardNo;
    }

    public String getPayCardType() {
        return payCardType;
    }

    public void setPayCardType(String payCardType) {
        this.payCardType = payCardType;
    }

    public String getPayCardIssueName() {
        return payCardIssueName;
    }

    public void setPayCardIssueName(String payCardIssueName) {
        this.payCardIssueName = payCardIssueName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public BigDecimal getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(BigDecimal txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccSplitData() {
        return accSplitData;
    }

    public void setAccSplitData(String accSplitData) {
        this.accSplitData = accSplitData;
    }

    public static final UnionPayMessage create(Map<String, Object> message) {
        UnionPayMessage payMessage = new JSONObject(message).toJavaObject(UnionPayMessage.class);
        payMessage.setPayMessage(message);
        return payMessage;
    }

}
