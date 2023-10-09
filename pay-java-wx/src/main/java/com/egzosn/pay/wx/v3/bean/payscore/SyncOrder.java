package com.egzosn.pay.wx.v3.bean.payscore;

import com.egzosn.pay.common.bean.AssistOrder;

import java.util.Date;

public class SyncOrder extends AssistOrder {

    private Date paidTime;

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }
}
