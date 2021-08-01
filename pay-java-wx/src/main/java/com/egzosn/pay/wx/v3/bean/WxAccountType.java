package com.egzosn.pay.wx.v3.bean;

import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.api.WxConst;

/**
 * 资金账户类型
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/2/22
 * </pre>
 */
public enum WxAccountType implements BillType {
    /**
     * 基本账户， 不填则默认是数据流
     */
    BASIC("BASIC"),
    /**
     * 基本账户
     * 返回格式为.gzip的压缩包账单
     */
    BASIC_GZIP("BASIC", WxConst.GZIP),
    /**
     * 运营账户
     */
    OPERATION("OPERATION"),
    /**
     * 运营账户
     * 返回格式为.gzip的压缩包账单
     */
    OPERATION_GZIP("OPERATION", WxConst.GZIP),
    /**
     * 手续费账户
     */
    FEES("FEES"),
    /**
     * 手续费账户
     * 返回格式为.gzip的压缩包账单
     */
    FEES_GZIP("FEES", WxConst.GZIP);

    /**
     * 账单类型
     */
    private String type;
    /**
     * 日期格式化表达式
     */
    private String tarType;



    WxAccountType(String type) {
        this.type = type;
    }


    WxAccountType(String type, String tarType) {
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
     * 返回交易类型
     *
     * @return 交易类型
     */
    @Override
    public String getCustom() {
        return WxTransactionType.FUND_FLOW_BILL.name();
    }

    public static WxAccountType forType(String type) {
        for (WxAccountType wxPayBillType : WxAccountType.values()) {
            if (wxPayBillType.getType().equals(type) && StringUtils.isEmpty(wxPayBillType.getFileType())) {
                return wxPayBillType;
            }
        }
        return null;
    }

}
