package com.egzosn.pay.yiji.bean;

import com.egzosn.pay.common.bean.TransactionType;

/**
 * 易极付交易类型
 * <pre>
 * 说明交易类型主要用于支付接口调用参数所需
 *
 * </pre>
 *
 * @author egan
 *
 * email egzosn@gmail.com
 * date 2019/04/15 22:58
 */
public enum  YiJiTransactionType implements TransactionType {
    /**
     * 跳转微支付
     */
    commonWchatTradeRedirect("commonWchatTradeRedirect"),
    /**
     * 跳转收银台支付
     */
    commonTradePay("commonTradePay"),
    /**
     * 跨境订单同步
     */
    corderRemittanceSynOrder("corderRemittanceSynOrder"),
    /**
     * 国际转账
     */
    applyRemittranceWithSynOrder("applyRemittranceWithSynOrder")

;

    private String method;

    YiJiTransactionType(String method) {
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
