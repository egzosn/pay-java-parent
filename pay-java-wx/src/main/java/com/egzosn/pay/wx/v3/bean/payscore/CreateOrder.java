package com.egzosn.pay.wx.v3.bean.payscore;

import com.egzosn.pay.common.bean.AssistOrder;

import java.math.BigDecimal;

public class CreateOrder extends AssistOrder {

    private String openId;

    private String serviceIntroduction;

    /**
     * 服务开始时间
     * 支持三种格式：yyyyMMddHHmmss、yyyyMMdd和OnAccept
     */
    private String startTime;

    private String endTime;


    private String riskFundName;

    private BigDecimal riskFundAmount;

    private String attach;


    private Boolean needUserConfirm;


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getServiceIntroduction() {
        return serviceIntroduction;
    }

    public void setServiceIntroduction(String serviceIntroduction) {
        this.serviceIntroduction = serviceIntroduction;
    }

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

    public String getRiskFundName() {
        return riskFundName;
    }

    public void setRiskFundName(String riskFundName) {
        this.riskFundName = riskFundName;
    }

    public BigDecimal getRiskFundAmount() {
        return riskFundAmount;
    }

    public void setRiskFundAmount(BigDecimal riskFundAmount) {
        this.riskFundAmount = riskFundAmount;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Boolean getNeedUserConfirm() {
        return needUserConfirm;
    }

    public void setNeedUserConfirm(Boolean needUserConfirm) {
        this.needUserConfirm = needUserConfirm;
    }
}
