package com.egzosn.pay.common.bean;

import java.util.Map;

/**
 * 转账类型
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/9/28.19:45
 */
public interface TransferType extends TransactionType{
    /**
     * 设置属性
     *
     * @param attr 已有属性对象
     * @param order 转账订单
     * @return 属性对象
     */
    Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order);
}
