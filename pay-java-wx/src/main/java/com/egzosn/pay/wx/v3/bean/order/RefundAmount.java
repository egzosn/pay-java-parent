package com.egzosn.pay.wx.v3.bean.order;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退款金额
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class RefundAmount extends Amount {


    /**
     * 退款金额,单位分
     */
    private int refund;

    /**
     * 退款出资的账户类型及金额信息
     */
    private List<From> from;

    /**
     * 用户支付金额,单位分
     */
    @JSONField(name = "payer_total")
    private int payerTotal;
    /**
     * 用户退款金额
     * 退款给用户的金额，不包含所有优惠券金额
     */
    @JSONField(name = "payer_refund")
    private int payerRefund;
    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，单位为分，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    @JSONField(name = "settlement_refund")
    private int settlementRefund;
    /**
     * 应结订单金额
     * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额，单位为分
     */
    @JSONField(name = "settlement_total")
    private int settlementTotal;
    /**
     * 优惠退款金额
     * 优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠，单位为分
     */
    @JSONField(name = "discount_refund")
    private int discountRefund;


    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public List<From> getFrom() {
        return from;
    }

    public void setFrom(List<From> from) {
        this.from = from;
    }

    public int getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(int payerTotal) {
        this.payerTotal = payerTotal;
    }

    public int getPayerRefund() {
        return payerRefund;
    }

    public void setPayerRefund(int payerRefund) {
        this.payerRefund = payerRefund;
    }

    public int getSettlementRefund() {
        return settlementRefund;
    }

    public void setSettlementRefund(int settlementRefund) {
        this.settlementRefund = settlementRefund;
    }

    public int getSettlementTotal() {
        return settlementTotal;
    }

    public void setSettlementTotal(int settlementTotal) {
        this.settlementTotal = settlementTotal;
    }

    public int getDiscountRefund() {
        return discountRefund;
    }

    public void setDiscountRefund(int discountRefund) {
        this.discountRefund = discountRefund;
    }
}
