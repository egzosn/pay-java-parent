package com.egzosn.pay.common.bean;

/**
 * 转账类型
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/9/28.19:45
 */
public interface TransferType extends TransactionType{

    /**
     * 获取转账类型
     * @return 转账类型
     */
    String getType();

    /**
     * 获取接口
     * @return 接口
     */
    String getMethod();
}
