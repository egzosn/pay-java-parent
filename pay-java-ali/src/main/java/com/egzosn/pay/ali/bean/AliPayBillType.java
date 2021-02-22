package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.util.DateUtils;

/**
 * 支付宝账单类型
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/2/22
 * </pre>
 */
public enum  AliPayBillType implements BillType {
    /**
     * 商户基于支付宝交易收单的业务账单；每日账单
     */
    TRADE_DAY("trade", DateUtils.YYYY_MM_DD),
    /**
     * 商户基于支付宝交易收单的业务账单；每月账单
     */
    TRADE_MONTH("trade", DateUtils.YYYY_MM),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每日账单
     */
    SIGNCUSTOMER_DAY("signcustomer", DateUtils.YYYY_MM_DD),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每月账单
     */
    SIGNCUSTOMER_MONTH("signcustomer", DateUtils.YYYY_MM),

    ;

    /**
     * 账单类型
     */
    private String type;
    /**
     * 日期格式化表达式
     */
    private String datePattern;

    AliPayBillType(String type, String datePattern) {
        this.type = type;
        this.datePattern = datePattern;
    }

    /**
     * 获取类型名称
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * 获取类型对应的日期格式化表达式
     *
     * @return 日期格式化表达式
     */
    @Override
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    @Override
    public String getFileType() {
        return null;
    }


    /**
     * 自定义属性
     *
     * @return 自定义属性
     */
    @Override
    public String getCustom() {
        return null;
    }


}
