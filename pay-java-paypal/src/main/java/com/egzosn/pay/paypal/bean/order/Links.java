package com.egzosn.pay.paypal.bean.order;


public class Links {
	/**
	 */
	private String href;
	/**
	 */
	private String rel;

	/**
	 */
	private String method;
	/**
	 */
	private String enctype;


	/**
	 * Default Constructor
	 */
	public Links() {
	}


	public Links(String href, String rel) {
		this.href = href;
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEnctype() {
		return enctype;
	}

	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}
}
