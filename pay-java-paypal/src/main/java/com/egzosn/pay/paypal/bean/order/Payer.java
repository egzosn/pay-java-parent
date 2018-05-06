package com.egzosn.pay.paypal.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Payer  {
	/**
	 * Payment method being used - PayPal Wallet payment, Bank Direct Debit  or Direct Credit card.
	 */
	@JSONField(name = "payment_method")
	private String paymentMethod;

	/**
	 * Transactional details including the amount and item details.
	 */
	private List<Transaction> transactions;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
}