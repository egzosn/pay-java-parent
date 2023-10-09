package com.egzosn.pay.wx.v3.bean.payscore;

import com.egzosn.pay.common.bean.AssistOrder;

public class CancelOrder extends AssistOrder {

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
