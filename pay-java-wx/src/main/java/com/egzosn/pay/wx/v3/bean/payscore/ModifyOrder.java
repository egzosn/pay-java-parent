package com.egzosn.pay.wx.v3.bean.payscore;

import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.AssistOrder;

import java.math.BigDecimal;
import java.util.List;

public class ModifyOrder extends AssistOrder {


    private BigDecimal totalAmount;

    @JSONField(name="post_payments")
    private List<PostPayment> postPayments;

    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
