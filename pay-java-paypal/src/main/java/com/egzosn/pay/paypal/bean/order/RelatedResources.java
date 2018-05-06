package com.egzosn.pay.paypal.bean.order;

public class RelatedResources{

	/**
	 * Order transaction
	 */
	private Order order;

	/**
	 * Refund transaction
	 */
	private Refund refund;

	/**
	 * Default Constructor
	 */
	public RelatedResources() {
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
	}
}