package com.egzosn.pay.common.bean;

/**
 * 基础货币类型
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/16.20:55
 */
public interface CurType {
    /**
     * 获取货币类型
     * @return 货币类型
     */
    String getType();

    /**
     * 货币名称
     * @return 货币名称
     */
    String getName();

}
