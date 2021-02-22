package com.egzosn.pay.paypal.v2.bean;

import com.egzosn.pay.common.bean.TransactionType;

/**
 * 贝宝交易类型
 * <pre>
 * 说明交易类型主要用于支付接口调用参数所需
 *
 *
 *
 * </pre>
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2018/04/28 11:10
 */
public enum PayPalTransactionType implements TransactionType {
    /**
     * 获取token
     */
    AUTHORIZE("v1/oauth2/token"),
    /**
     * 付款 网页支付
     */
    CHECKOUT("v2/checkout/orders"),
    /**
     * 获取订单信息
     */
    ORDERS_GET("/v2/checkout/orders/{order_id}"),
    /**
     * 确认订单并返回确认后订单信息
     */
    ORDERS_CAPTURE("/v2/checkout/orders/{order_id}/capture"),
    /**
     * 获取确认后订单信息
     */
    GET_CAPTURE("/v2/payments/captures/{capture_id}"),
    /**
     * 退款
     */
    REFUND("/v2/payments/captures/{capture_id}/refund"),

    /**
     * 退款查询
     */
    REFUND_GET("/v2/payments/refunds/{refund_id}"),

    ;


    private String method;

    private PayPalTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     *
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }

}
