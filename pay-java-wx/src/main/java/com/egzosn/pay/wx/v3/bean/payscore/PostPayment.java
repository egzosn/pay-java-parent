package com.egzosn.pay.wx.v3.bean.payscore;

import java.io.Serializable;
import java.math.BigDecimal;

public class PostPayment implements Serializable {

    /**
     * 付费项目名称
     */
    private String name;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 说明
     */
    private String description;

    /**
     * 数量
     */
    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
