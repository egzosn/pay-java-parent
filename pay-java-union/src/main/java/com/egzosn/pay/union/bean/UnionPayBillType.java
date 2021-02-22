package com.egzosn.pay.union.bean;

import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 银联账单类型
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/2/23
 * </pre>
 */
public class UnionPayBillType implements BillType {

    private String fileType = "00";

    /**
     * 获取类型名称
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return null;
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
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public UnionPayBillType() {
    }

    public UnionPayBillType(String fileType) {
        if (StringUtils.isNotEmpty(fileType)) {
            this.fileType = fileType;
        }
    }
}
