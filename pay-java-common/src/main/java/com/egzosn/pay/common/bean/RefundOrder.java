package com.egzosn.pay.common.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款订单信息
 * @author: egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2018/1/15 21:40
 *   </pre>
 */
public class RefundOrder {
    /**
     * 退款单号，每次进行退款的单号，此处唯一
     */
    private String refundNo;
    /**
     * 支付平台订单号,交易号
     */
    private String tradeNo;
    /**
     * 商户单号
     */
    private String outTradeNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款交易日期
     */
    private Date orderDate;

    /**
     * 货币
     */
    private CurType curType;
    /**
     * 退款说明
     */
    private String description;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public CurType getCurType() {
        return curType;
    }

    public void setCurType(CurType curType) {
        this.curType = curType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RefundOrder() {
    }

    public RefundOrder(String refundNo, String tradeNo, BigDecimal refundAmount) {
        this.refundNo = refundNo;
        this.tradeNo = tradeNo;
        this.refundAmount = refundAmount;
    }

    public RefundOrder(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    public RefundOrder(String refundNo, String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.refundNo = refundNo;
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }


}
