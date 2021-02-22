package com.egzosn.pay.wx.bean;

import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.api.WxConst;

/**
 * 支付宝账单类型
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/2/22
 * </pre>
 */
public enum WxPayBillType implements BillType {
    /**
     * 商户基于支付宝交易收单的业务账单；每日账单
     */
    ALL("ALL"),
    /**
     * 商户基于支付宝交易收单的业务账单；每日账单
     */
    ALL_GZIP("ALL", WxConst.GZIP),
    /**
     * 商户基于支付宝交易收单的业务账单；每月账单
     */
    SUCCESS(WxConst.SUCCESS),
    /**
     * 商户基于支付宝交易收单的业务账单；每月账单
     */
    SUCCESS_GZIP(WxConst.SUCCESS, WxConst.GZIP),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每日账单
     */
    REFUND("REFUND"),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每日账单
     */
    REFUND_GZIP("REFUND", WxConst.GZIP),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每月账单
     */
    RECHARGE_REFUND("RECHARGE_REFUND"),
    /**
     * 基于商户支付宝余额收入及支出等资金变动的帐务账单；每月账单
     */
    RECHARGE_REFUND_GZIP("RECHARGE_REFUND", WxConst.GZIP);

    /**
     * 账单类型
     */
    private String type;
    /**
     * 日期格式化表达式
     */
    private String tarType;

    WxPayBillType(String type) {
        this.type = type;
    }


    WxPayBillType(String type, String tarType) {
        this.type = type;
        this.tarType = tarType;
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
        return null;
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    @Override
    public String getFileType() {
        return tarType;
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

    public static WxPayBillType forType(String type){
        for (WxPayBillType wxPayBillType : WxPayBillType.values()){
            if (wxPayBillType.getType().equals(type) && StringUtils.isEmpty(wxPayBillType.getFileType())){
                return wxPayBillType;
            }
        }
        return WxPayBillType.ALL;
    }

}
