package com.egzosn.pay.baidu.bean;

import com.egzosn.pay.common.bean.RefundOrder;

import java.math.BigDecimal;

public class BaiduRefundOrder extends RefundOrder {

    /**
     * 退款类型
     */
    private Integer refundType;


    public BaiduRefundOrder(Long orderId,
                            String userId,
                            Integer refundType,
                            String refundReason,
                            String tpOrderId) {
        super();
        setOutTradeNo(String.valueOf(orderId));
        setUserId(userId);
        setRefundType(refundType);
        setDescription(refundReason);
        setTradeNo(tpOrderId);
    }

    /**
     * 退款金额，单位：分，发起部分退款时必传
     *
     * @return 退款金额
     */
    public BigDecimal getApplyRefundMoney() {
        return getRefundAmount();
    }

    /**
     * 退款金额，单位：分，发起部分退款时必传
     *
     * @param applyRefundMoney 退款金额
     */
    public void setApplyRefundMoney(BigDecimal applyRefundMoney) {
        setRefundAmount(applyRefundMoney);
    }

    /**
     * 业务方退款批次id，退款业务流水唯一编号，发起部分退款时必传
     *
     * @return 退款业务流水
     */
    public String getBizRefundBatchId() {
        return getRefundNo();
    }

    /**
     * 业务方退款批次id，退款业务流水唯一编号，发起部分退款时必传
     * @param bizRefundBatchId 业务方退款批次id
     */
    public void setBizRefundBatchId(String bizRefundBatchId) {
        setRefundNo(bizRefundBatchId);
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
        addAttr("refundType", refundType);
    }

    public Integer getRefundType() {
        return refundType;
    }

}
