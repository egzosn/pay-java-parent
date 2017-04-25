package com.egzosn.pay.common.bean;

/**
 * 交易类型
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016/10/19 22:30
 * </pre>
 */
public interface TransactionType {
    /**
     * 获取交易类型
     * @return 交易类型
     */
     String getType();

    /**
     * 获取接口
     * @return 接口
     */
     String getMethod();
}

