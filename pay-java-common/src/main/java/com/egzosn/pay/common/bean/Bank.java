package com.egzosn.pay.common.bean;

/**
 * 银行
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2018/1/31
 * </pre>
 */
public interface Bank {

    /**
     * 获取银行的代码
     * @return 银行的代码
     */
    String getCode();

    /**
     * 获取银行的名称
     * @return 银行的名称
     */
    String getName();
}
