package com.egzosn.pay.paypal.v2.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

public class PurchaseUnitRequest {
    @JSONField(name = "amount")
    private Money money;
    @JSONField(name = "custom_id")
    private String customId;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "invoice_id")
    private String invoiceId;
    @JSONField(name = "reference_id")
    private String referenceId;

    @JSONField(name = "soft_descriptor")
    private String softDescriptor;
    /**
     * The shipping details.
     */
    @JSONField(name = "shipping")
    private ShippingDetail shippingDetail;

    public Money money() {
        return this.money;
    }

    public PurchaseUnitRequest money(Money money) {
        this.money = money;
        return this;
    }

    public String customId() {
        return this.customId;
    }

    public PurchaseUnitRequest customId(String customId) {
        this.customId = customId;
        return this;
    }

    public String description() {
        return this.description;
    }

    public PurchaseUnitRequest description(String description) {
        this.description = description;
        return this;
    }

    public String invoiceId() {
        return this.invoiceId;
    }

    public PurchaseUnitRequest invoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }


    public String referenceId() {
        return this.referenceId;
    }

    public PurchaseUnitRequest referenceId(String referenceId) {
        this.referenceId = referenceId;
        return this;
    }


    public String softDescriptor() {
        return this.softDescriptor;
    }

    public PurchaseUnitRequest softDescriptor(String softDescriptor) {
        this.softDescriptor = softDescriptor;
        return this;
    }

    public PurchaseUnitRequest shippingDetail(ShippingDetail shippingDetail) {
        this.shippingDetail = shippingDetail;
        return this;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getSoftDescriptor() {
        return softDescriptor;
    }

    public void setSoftDescriptor(String softDescriptor) {
        this.softDescriptor = softDescriptor;
    }

    public ShippingDetail getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(ShippingDetail shippingDetail) {
        this.shippingDetail = shippingDetail;
    }
}