package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.common.api.PayMessageHandler;

/**
 *
 * Created by ZaoSheng on 2016/6/1.
 */
public abstract class BasePayMessageHandler implements PayMessageHandler {
    //支付账户id
    private Integer payId;

    public BasePayMessageHandler(Integer payId) {
        this.payId = payId;
    }

    public Integer getPayId() {
        return payId;
    }
}
