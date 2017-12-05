package com.egzosn.pay.union.request;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 *  <pre>
create 2017 2017/11/4 0004
 * </pre>
 */
public class UnionQueryOrder {

    private Integer payId;
    /**
     * 支付平台订单号
     */
    private String orderId;
    /**
     * 金额
     */
    private String txnAmt;
    /**
     * 原交易查询流水号
     */
    private String origQryId;
    /**
     * 原交易商户订单号
     */
    private String origOrderId;
    /**
     * 原交易商户发送交易时间：
     */
    private String origTxnTime;


    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId (String orderId) {
        this.orderId = orderId;
    }

    public String getTxnAmt () {
        return txnAmt;
    }

    public void setTxnAmt (String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getOrigQryId () {
        return origQryId;
    }

    public void setOrigQryId (String origQryId) {
        this.origQryId = origQryId;
    }

    public String getOrigOrderId () {
        return origOrderId;
    }

    public void setOrigOrderId (String origOrderId) {
        this.origOrderId = origOrderId;
    }

    public String getOrigTxnTime () {
        return origTxnTime;
    }

    public void setOrigTxnTime (String origTxnTime) {
        this.origTxnTime = origTxnTime;
    }
}
