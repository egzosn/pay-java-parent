package com.egzosn.pay.paypal.v2.bean.order;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderRequest {
    @JSONField(name = "application_context")
    private ApplicationContext applicationContext;
    @JSONField(name = "intent")
    private String checkoutPaymentIntent;

    @JSONField(name =
            "purchase_units"
    )
    private List<PurchaseUnitRequest> purchaseUnits;


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getCheckoutPaymentIntent() {
        return checkoutPaymentIntent;
    }

    public void setCheckoutPaymentIntent(String checkoutPaymentIntent) {
        this.checkoutPaymentIntent = checkoutPaymentIntent;
    }


    public List<PurchaseUnitRequest> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(List<PurchaseUnitRequest> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }
}