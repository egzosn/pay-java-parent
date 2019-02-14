package com.egzosn.pay.union.bean;

import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.TransactionType;

import java.math.BigDecimal;

/**
 * 银联订单实体
 * @author Actinia
 * @create 2019-02-13 23:39
 */
public class UnionPayOrder extends PayOrder {

    //请求方保留域(透传字段)
    private String reqReserved;
    //风控信息域
    private String riskRateInfo;

    public UnionPayOrder(String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        setSubject(subject);
        setBody(body);
        setPrice(price);
        setOutTradeNo(outTradeNo);
        setTransactionType(transactionType);
    }
    public UnionPayOrder(String subject, String body, BigDecimal price, String outTradeNo) {
        setSubject(subject);
        setBody(body);
        setPrice(price);
        setOutTradeNo(outTradeNo);
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getRiskRateInfo() {
        return riskRateInfo;
    }

    public void setRiskRateInfo(String riskRateInfo) {
        this.riskRateInfo = riskRateInfo;
    }
}
