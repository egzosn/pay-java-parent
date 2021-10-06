package com.egzosn.pay.wx.v3.bean.response.order;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 回调中的订单金额信息
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class Amount extends com.egzosn.pay.wx.v3.bean.order.Amount {

    /**
     * 用户支付金额，单位为分。
     */
    @JSONField(name = "payer_total")
    private BigDecimal payerTotal;
    /**
     * 用户支付币种
     */
    @JSONField(name = "payer_currency")
    private String payerCurrency;

    /**
     * 退款金额，单位为分
     */
    private Integer refund;

    /**
     * 退款给用户的金额，单位为分，不包含所有优惠券金额
     */
    @JSONField(name = "payer_refund")
    private Integer payerRefund;

    public BigDecimal getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(BigDecimal payerTotal) {
        this.payerTotal = payerTotal;
    }

    public String getPayerCurrency() {
        return payerCurrency;
    }

    public void setPayerCurrency(String payerCurrency) {
        this.payerCurrency = payerCurrency;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public Integer getPayerRefund() {
        return payerRefund;
    }

    public void setPayerRefund(Integer payerRefund) {
        this.payerRefund = payerRefund;
    }
}
