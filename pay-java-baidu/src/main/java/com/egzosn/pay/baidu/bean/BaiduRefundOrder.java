package com.egzosn.pay.baidu.bean;

import com.egzosn.pay.common.bean.RefundOrder;

import java.math.BigDecimal;

public class BaiduRefundOrder extends RefundOrder {
    private Long userId;
    private Integer refundType;
    private String refundReason;
    private String tpOrderId;
    /**
     * 退款金额，单位：分，发起部分退款时必传
     */
    private BigDecimal applyRefundMoney;
    /**
     * 业务方退款批次id，退款业务流水唯一编号，发起部分退款时必传
     */
    private String bizRefundBatchId;
    
    public BaiduRefundOrder(Long orderId,
                            Long userId,
                            Integer refundType,
                            String refundReason,
                            String tpOrderId) {
        super();
        setTradeNo(String.valueOf(orderId));
        this.userId = userId;
        this.refundType = refundType;
        this.refundReason = refundReason;
        this.tpOrderId = tpOrderId;
    }
    
    public BigDecimal getApplyRefundMoney() {
        return applyRefundMoney;
    }
    
    public void setApplyRefundMoney(BigDecimal applyRefundMoney) {
        setRefundAmount(applyRefundMoney);
    }
    
    public String getBizRefundBatchId() {
        return bizRefundBatchId;
    }
    
    public void setBizRefundBatchId(String bizRefundBatchId) {
        this.bizRefundBatchId = bizRefundBatchId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Integer getRefundType() {
        return refundType;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public String getTpOrderId() {
        return tpOrderId;
    }
}
