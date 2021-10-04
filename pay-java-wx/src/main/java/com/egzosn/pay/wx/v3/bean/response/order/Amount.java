package com.egzosn.pay.wx.v3.bean.response.order;

import java.math.BigDecimal;

/**
 * 回调中的订单金额信息
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class Amount extends com.egzosn.pay.wx.v3.bean.order.Amount{

    /**
     * 用户支付金额，单位为分。
     */
    private BigDecimal payerTotal;
    /**
     * 用户支付币种
     */
    private String payerCurrency;

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
}
