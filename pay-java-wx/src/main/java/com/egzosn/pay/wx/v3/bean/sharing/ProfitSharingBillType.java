package com.egzosn.pay.wx.v3.bean.sharing;

import com.egzosn.pay.common.bean.BillType;

/**
 * 分账账单类型
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public enum ProfitSharingBillType implements BillType {

    /**
     * 数据流
     */
    STREAM,
    /**
     * 返回格式为.gzip的压缩包账单
     */
    GZIP("GZIP");

    ProfitSharingBillType() {
    }

    ProfitSharingBillType(String type) {
        this.type = type;
    }

    private String type;

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
