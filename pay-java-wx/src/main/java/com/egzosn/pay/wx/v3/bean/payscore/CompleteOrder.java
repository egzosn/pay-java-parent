package com.egzosn.pay.wx.v3.bean.payscore;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

public class CompleteOrder extends CreateOrder {

    private BigDecimal totalAmount;
    @JSONField(name="post_payments")
    private List<PostPayment> postPayments;

    private Boolean profitSharing = false;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PostPayment> getPostPayments() {
        return postPayments;
    }

    public void setPostPayments(List<PostPayment> postPayments) {
        this.postPayments = postPayments;
    }

    public Boolean getProfitSharing() {
        return profitSharing;
    }

    public void setProfitSharing(Boolean profitSharing) {
        this.profitSharing = profitSharing;
    }
}
