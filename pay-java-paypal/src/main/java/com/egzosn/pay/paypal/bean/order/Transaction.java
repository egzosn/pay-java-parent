package com.egzosn.pay.paypal.bean.order;


import java.util.List;

public class Transaction  extends  CartBase{
	/**
	 * List of financial transactions (Sale, Authorization, Capture, Refund) related to the payment.
	 */
	private List<RelatedResources> relatedResources;

	public List<RelatedResources> getRelatedResources() {
		return relatedResources;
	}

	public void setRelatedResources(List<RelatedResources> relatedResources) {
		this.relatedResources = relatedResources;
	}

	/**
	 * Default Constructor
	 */
	public Transaction() {
	}

}