package com.egzosn.pay.wx.v3.bean;

import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.api.WxConst;

/**
 * 微信账单类型
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/08/01
 * </pre>
 */
public enum WxBillType implements BillType {
    /**
     * 返回当日所有订单信息（不含充值退款订单）
     */
    ALL("ALL"),
    /**
     * 返回当日所有订单信息（不含充值退款订单） 返回格式为.gzip的压缩包账单
     */
    ALL_GZIP("ALL", WxConst.GZIP),
    /**
     * 返回当日成功支付的订单（不含充值退款订单）
     */
    SUCCESS(WxConst.SUCCESS),
    /**
     * 返回当日成功支付的订单（不含充值退款订单）
     * 返回格式为.gzip的压缩包账单
     */
    SUCCESS_GZIP(WxConst.SUCCESS, WxConst.GZIP),
    /**
     * 返回当日退款订单（不含充值退款订单）
     */
    REFUND("REFUND"),
    /**
     * 返回当日退款订单（不含充值退款订单）
     * 返回格式为.gzip的压缩包账单
     */
    REFUND_GZIP("REFUND", WxConst.GZIP);

    /**
     * 账单类型
     */
    private String type;
    /**
     * 日期格式化表达式
     */
    private String tarType;

    WxBillType(String type) {
        this.type = type;
    }


    WxBillType(String type, String tarType) {
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
        return WxTransactionType.TRADE_BILL.name();
    }

    public static WxBillType forType(String type) {
        for (WxBillType wxPayBillType : WxBillType.values()) {
            if (wxPayBillType.getType().equals(type) && StringUtils.isEmpty(wxPayBillType.getFileType())) {
                return wxPayBillType;
            }
        }
        return null;
    }

}
