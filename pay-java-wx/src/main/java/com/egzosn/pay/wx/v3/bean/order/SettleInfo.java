package com.egzosn.pay.wx.v3.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 结算信息
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/5
 * </pre>
 */
public class SettleInfo {
    /**
     * 是否指定分账，枚举值
     * true：是
     * false：否
     * 示例值：true
     */
    @JSONField(name = "profit_sharing")
    private Boolean profitSharing;
    /**
     * 补差金额
     * SettleInfo.profit_sharing为true时，该金额才生效。
     * 注意：单笔订单最高补差金额为5000元
     */
    @JSONField(name = "subsidy_amount")
    private Integer subsidyAmount;

    public Boolean getProfitSharing() {
        return profitSharing;
    }

    public void setProfitSharing(Boolean profitSharing) {
        this.profitSharing = profitSharing;
    }

    public Integer getSubsidyAmount() {
        return subsidyAmount;
    }

    public void setSubsidyAmount(Integer subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }
}
