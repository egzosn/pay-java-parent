package com.egzosn.pay.common.bean;

/**
 * 账单类型
 * @author Egan
 * @email egzosn@gmail.com
 * @date 2021/2/22
 */
public interface BillType {
    /**
     * 获取类型名称
     * @return 类型
     */
    String getType();

    /**
     * 获取类型对应的日期格式化表达式
     * @return 日期格式化表达式
     */
    String getDatePattern();

    /**
     * 获取压缩类型
     * @return 压缩类型
     */
    String getTarType();

    /**
     * 自定义属性
     * @return 自定义属性
     */
    String getCustom();
}
