package com.egzosn.pay.paypal.bean.order;

public class Amount {
	/**
	 * 3-letter [currency code](https://developer.paypal.com/docs/integration/direct/rest_api_payment_country_currency_support/). PayPal does not support all currencies.
	 */
	private String currency;
	/**
	 * Total amount charged from the payer to the payee. In case of a refund, this is the refunded amount to the original payer from the payee. 10 characters max with support for 2 decimal places.
	 */
	private String total;
	/**
	 * Additional details of the payment amount.
	 */
	private Details details;

	/**
	 * Default Constructor
	 */
	public Amount() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Amount(String currency, String total) {
		this.currency = currency;
		this.total = total;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}
}