package com.egzosn.pay.wx.v3.bean;

import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.TransactionType;

/**
 * 微信V3分账交易类型
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public enum WxProfitSharingTransactionType implements TransactionType {
    /**
     * 请求分账
     */
    ORDERS("/v3/profitsharing/orders", MethodType.POST),
    /**
     * 查询分账结果
     */
    ORDERS_RESULT("/v3/profitsharing/orders/{out_order_no}", MethodType.GET),
    /**
     * 请求分账回退
     */
    RETURN_ORDERS("/v3/profitsharing/return-orders", MethodType.POST),
    /**
     * 查询分账回退结果
     */
    RETURN_ORDERS_RESULT("/v3/profitsharing/return-orders/{out_return_no}", MethodType.GET),
    /**
     * 解冻剩余资金
     */
    ORDERS_UNFREEZE("/v3/profitsharing/orders/unfreeze", MethodType.POST),
    /**
     * 查询剩余待分金额
     */
    AMOUNTS("/v3/profitsharing/transactions/{transaction_id}/amounts", MethodType.GET),
    /**
     * 服务商专用-查询最大分账比例
     */
    MCH_CONFIG("/v3/profitsharing/merchant-configs/{sub_mchid}", MethodType.GET),
    /**
     * 添加分账接收方
     */
    RECEIVERS_ADD("/v3/profitsharing/receivers/add", MethodType.POST),
    /**
     * 删除分账接收方
     */
    RECEIVERS_DELETE("/v3/profitsharing/receivers/add", MethodType.POST),

    BILLS("/v3/profitsharing/bills", MethodType.GET),

    ;



    WxProfitSharingTransactionType(String type, MethodType method) {
        this.type = type;
        this.method = method;
    }

    private String type;
    private MethodType method;



    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMethod() {
        return this.method.name();
    }

}
