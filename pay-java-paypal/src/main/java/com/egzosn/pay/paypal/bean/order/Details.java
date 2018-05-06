package com.egzosn.pay.paypal.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

public class Details {
	/**
	 * Amount of the subtotal of the items. **Required** if line items are specified. 10 characters max, with support for 2 decimal places.
	 */
	private String subtotal;
	/**
	 * Amount charged for shipping. 10 characters max with support for 2 decimal places.
	 */
	private String shipping;
	/**
	 * Amount charged for tax. 10 characters max with support for 2 decimal places.
	 */
	private String tax;
	/**
	 * Amount being charged for the handling fee. Only supported when the `payment_method` is set to `paypal`.
	 */
	@JSONField(name = "handling_fee")
	private String handlingFee;
	/**
	 * Amount being discounted for the shipping fee. Only supported when the `payment_method` is set to `paypal`.
	 */
	@JSONField(name = "shipping_discount")
	private String shippingDiscount;
	/**
	 * Amount being charged for the insurance fee. Only supported when the `payment_method` is set to `paypal`.
	 */
	private String insurance;
	/**
	 * Amount being charged as gift wrap fee.
	 */
	@JSONField(name = "gift_wrap")
	private String giftWrap;
	/**
	 * Fee charged by PayPal. In case of a refund, this is the fee amount refunded to the original receipient of the payment.
	 */
	private String fee;

	/**
	 * Default Constructor
	 */
	public Details() {
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getHandlingFee() {
		return handlingFee;
	}

	public void setHandlingFee(String handlingFee) {
		this.handlingFee = handlingFee;
	}

	public String getShippingDiscount() {
		return shippingDiscount;
	}

	public void setShippingDiscount(String shippingDiscount) {
		this.shippingDiscount = shippingDiscount;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getGiftWrap() {
		return giftWrap;
	}

	public void setGiftWrap(String giftWrap) {
		this.giftWrap = giftWrap;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
}