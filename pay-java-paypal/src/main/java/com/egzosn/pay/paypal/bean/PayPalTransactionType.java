package com.egzosn.pay.paypal.bean;

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
 *
 * email egzosn@gmail.com
 * date 2018/04/28 11:10
 */
public enum PayPalTransactionType implements TransactionType {
    /**
     * 获取token
     */
    AUTHORIZE("oauth2/token"),
    /**
     * 付款 网页支付
     */
    sale("payments/payment"),
    /**
     *  sale 支付退款
     */
    REFUND("payments/sale/{saleId}/refund"),
    REFUND_QUERY("payments/refund/{refundId}"),
    PAYOUT("payments/payouts/{payoutBatchId}"),
    ORDERS("payments/orders/{orderId}"),
    /**
     * 回调订单状态查询
     */
    EXECUTE("payments/payment/{paymentId}/execute"),

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
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }

}
