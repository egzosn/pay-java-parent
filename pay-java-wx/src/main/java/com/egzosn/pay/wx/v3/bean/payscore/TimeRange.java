package com.egzosn.pay.wx.v3.bean.payscore;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TimeRange implements Serializable {

    @JSONField(name = "start_time")
    private String startTime;

    @JSONField(name = "end_time")
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
