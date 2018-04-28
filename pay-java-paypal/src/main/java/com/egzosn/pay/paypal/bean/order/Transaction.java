package com.egzosn.pay.paypal.bean.order;


import java.util.List;

public class Transaction  extends  CartBase{
	/**
	 * List of financial transactions (Sale, Authorization, Capture, Refund) related to the payment.
	 */
	private List<RelatedResources> relatedResources;


	/**
	 * Default Constructor
	 */
	public Transaction() {
	}

}