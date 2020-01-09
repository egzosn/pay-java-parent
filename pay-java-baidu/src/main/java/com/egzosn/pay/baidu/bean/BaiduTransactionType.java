package com.egzosn.pay.baidu.bean;

import com.egzosn.pay.common.bean.TransactionType;

public enum BaiduTransactionType implements TransactionType {
    /**
     * 查询支付状态
     */
    PAY_QUERY("https://dianshang.baidu.com/platform/entity/openapi/queryorderdetail", null),
    /**
     * 取消核销
     */
    REFUND_QUERY("https://nop.nuomi.com/nop/server/rest", "nuomi.cashier.syncorderstatus"),
    /**
     * 下载资金账单
     */
    DOWNLOAD_BILL("https://openapi.baidu.com/rest/2.0/smartapp/pay/paymentservice/capitaBill", null),
    /**
     * 下载订单对账单
     */
    DOWNLOAD_ORDER_BILL("https://openapi.baidu.com/rest/2.0/smartapp/pay/paymentservice/orderBill", null),
    /**
     * 申请退款
     */
    APPLY_REFUND("https://nop.nuomi.com/nop/server/rest", "nuomi.cashier.applyorderrefund");
    private final String method;
    private final String url;
    
    BaiduTransactionType( String url, String method) {
        this.url = url;
        this.method = method;
    }
    
    @Override
    public String getType() {
        return this.name();
    }
    
    @Override
    public String getMethod() {
        return this.method;
    }
    
    public String getUrl() {
        return url;
    }
}
