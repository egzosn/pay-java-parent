package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.TransferOrder;

/**
 * 支付转账(红包)订单
 * @author egan
 * date 2020/5/18 21:08
 * email egzosn@gmail.com
 */
public class AliTransferOrder extends TransferOrder {

    private String orderTitle;
    private String identity;
    private String identityType;
    private String name;
    private String businessParams;

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessParams() {
        return businessParams;
    }

    public void setBusinessParams(String businessParams) {
        this.businessParams = businessParams;
    }
}
