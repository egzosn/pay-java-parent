package com.egzosn.pay.wx.v3.bean.payscore;

import com.egzosn.pay.common.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;

public class RiskFund implements Serializable {

    /**
     * 风险金名称
     */
    private String name;

    /**
     * 风险金额
     */
    private BigDecimal amount;

    /**
     * 风险说明
     */
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return Util.conversionCentAmount(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
