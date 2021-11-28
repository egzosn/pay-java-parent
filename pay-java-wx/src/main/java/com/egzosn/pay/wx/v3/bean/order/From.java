package com.egzosn.pay.wx.v3.bean.order;

/**
 * 退款出资的账户类型及金额信息
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class From {

    /**
     * 出资账户类型
     * 下面枚举值多选一。
     * 枚举值：
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     */
    private String account;
    /**
     * 对应账户出资金额
     */
    private Integer amount;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
