package com.egzosn.pay.paypal.v2.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * The shipping details.
 */
public class ShippingDetail {

    // Required default constructor
    public ShippingDetail() {
    }

    /**
     * The portable international postal address. Maps to [AddressValidationMetadata](https://github.com/googlei18n/libaddressinput/wiki/AddressValidationMetadata) and HTML 5.1 [Autofilling form controls: the autocomplete attribute](https://www.w3.org/TR/html51/sec-forms.html#autofilling-form-controls-the-autocomplete-attribute).
     */
    @JSONField(name = "address")
    private AddressPortable addressPortable;

    public AddressPortable addressPortable() {
        return addressPortable;
    }

    public ShippingDetail addressPortable(AddressPortable addressPortable) {
        this.addressPortable = addressPortable;
        return this;
    }

    /**
     * The name of the party.
     */
    @JSONField(name = "name")
    private Name name;

    public Name name() {
        return name;
    }

    public ShippingDetail name(Name name) {
        this.name = name;
        return this;
    }

    public AddressPortable getAddressPortable() {
        return addressPortable;
    }

    public void setAddressPortable(AddressPortable addressPortable) {
        this.addressPortable = addressPortable;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
