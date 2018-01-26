package com.egzosn.pay.payoneer.bean;

import com.egzosn.pay.common.bean.TransactionType;

/**
 * 支付类型
 * @author Actinia
 * @email hayesfu@qq.com
 *  <pre>
 * create 2017 2017/1/16 0016
 * </pre>
 */
public enum PayoneerTransactionType implements TransactionType {

    /**
     * 收款
     */
    registration("payees/registration-link"),
    /**
     * 收款
     */
    charge("charges"),
    /**
     * 取消收款(取消订单与退款)
     */
    chargeCancel("charges/{client_reference_id}/cancel"),
    /**
     * 查询收款订单与订单状态
     */
    chargeStatus("charges/{client_reference_id}/status")
    ;

    private String method;

    PayoneerTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }
}
