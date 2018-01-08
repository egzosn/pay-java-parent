package com.egzosn.pay.wx.youdian.bean;

import com.egzosn.pay.common.bean.TransactionType;

/**
 * 友店交易类型
 * @author egan
 *
 * email egzosn@gmail.com
 * date 2017/01/12 22:58
 */
public enum  YoudianTransactionType implements TransactionType {

    /**
     * 扫码付
     */
    NATIVE("unifiedorder"),
    /**
     * 刷卡付
     */
    MICROPAY("micropay");//暂未接触

    private String method;

    YoudianTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }
}
