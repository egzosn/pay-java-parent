package com.egzosn.pay.common.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2018/1/15 21:40
 *   </pre>
 */
public class RefundOrder extends AssistOrder {
    /**
     * 退款单号，每次进行退款的单号，此处唯一
     */
    private String refundNo;
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
    /**
     * 退款用户
     */
    private String userId;

    /**
     * 退款URL
     */
    private String refundUrl;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RefundOrder() {
    }

    public RefundOrder(String refundNo, String tradeNo, BigDecimal refundAmount) {
        this.refundNo = refundNo;
        setTradeNo(tradeNo);
        this.refundAmount = refundAmount;
    }

    public RefundOrder(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        setTradeNo(tradeNo);
        setOutTradeNo(outTradeNo);
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    public RefundOrder(String refundNo, String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.refundNo = refundNo;
        setTradeNo(tradeNo);
        setOutTradeNo(outTradeNo);
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
}
