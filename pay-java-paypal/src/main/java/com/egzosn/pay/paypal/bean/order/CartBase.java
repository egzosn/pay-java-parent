package com.egzosn.pay.paypal.bean.order;


public class CartBase {
	/**
	 * Merchant identifier to the purchase unit. Optional parameter
	 */

	private String referenceId;
	/**
	 * Amount being collected.
	 */
	private Amount amount;
	/**
	 * Recipient of the funds in this transaction.
	 */
	private Payee payee;
	/**
	 * Description of what is being paid for.
	 */
	private String description;
	/**
	 * Note to the recipient of the funds in this transaction.
	 */
	private String noteToPayee;
	/**
	 * free-form field for the use of clients
	 */
	private String custom;
	/**
	 * invoice number to track this payment
	 */
	private String invoiceNumber;
	/**
	 * Soft descriptor used when charging this funding source. If length exceeds max length, the value will be truncated
	 */
	private String softDescriptor;
	/**
	 * Soft descriptor city used when charging this funding source. If length exceeds max length, the value will be truncated. Only supported when the `payment_method` is set to `credit_card`
	 */
	private String softDescriptorCity;
	/**
	 * URL to send payment notifications
	 */
	private String notifyUrl;
	/**
	 * Url on merchant site pertaining to this payment.
	 */
	private String orderUrl;

	/**
	 * Default Constructor
	 */
	public CartBase() {
	}

	/**
	 * Parameterized Constructor
	 */
	public CartBase(Amount amount) {
		this.amount = amount;
	}

	/**
	 * Merchant identifier to the purchase unit. Optional parameter
	 */
	@SuppressWarnings("all")
	public String getReferenceId() {
		return this.referenceId;
	}

	/**
	 * Amount being collected.
	 */
	@SuppressWarnings("all")
	public Amount getAmount() {
		return this.amount;
	}

	/**
	 * Recipient of the funds in this transaction.
	 */
	@SuppressWarnings("all")
	public Payee getPayee() {
		return this.payee;
	}

	/**
	 * Description of what is being paid for.
	 */
	@SuppressWarnings("all")
	public String getDescription() {
		return this.description;
	}

	/**
	 * Note to the recipient of the funds in this transaction.
	 */
	@SuppressWarnings("all")
	public String getNoteToPayee() {
		return this.noteToPayee;
	}

	/**
	 * free-form field for the use of clients
	 */
	@SuppressWarnings("all")
	public String getCustom() {
		return this.custom;
	}

	/**
	 * invoice number to track this payment
	 */
	@SuppressWarnings("all")
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	/**
	 * Soft descriptor used when charging this funding source. If length exceeds max length, the value will be truncated
	 */
	@SuppressWarnings("all")
	public String getSoftDescriptor() {
		return this.softDescriptor;
	}

	/**
	 * Soft descriptor city used when charging this funding source. If length exceeds max length, the value will be truncated. Only supported when the `payment_method` is set to `credit_card`
	 */
	@SuppressWarnings("all")
	public String getSoftDescriptorCity() {
		return this.softDescriptorCity;
	}


	/**
	 * URL to send payment notifications
	 */
	@SuppressWarnings("all")
	public String getNotifyUrl() {
		return this.notifyUrl;
	}

	/**
	 * Url on merchant site pertaining to this payment.
	 */
	@SuppressWarnings("all")
	public String getOrderUrl() {
		return this.orderUrl;
	}

	/**
	 * Merchant identifier to the purchase unit. Optional parameter
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setReferenceId(final String referenceId) {
		this.referenceId = referenceId;
		return this;
	}

	/**
	 * Amount being collected.
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setAmount(final Amount amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Recipient of the funds in this transaction.
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setPayee(final Payee payee) {
		this.payee = payee;
		return this;
	}

	/**
	 * Description of what is being paid for.
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setDescription(final String description) {
		this.description = description;
		return this;
	}

	/**
	 * Note to the recipient of the funds in this transaction.
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setNoteToPayee(final String noteToPayee) {
		this.noteToPayee = noteToPayee;
		return this;
	}

	/**
	 * free-form field for the use of clients
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setCustom(final String custom) {
		this.custom = custom;
		return this;
	}

	/**
	 * invoice number to track this payment
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setInvoiceNumber(final String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	/**
	 * Soft descriptor used when charging this funding source. If length exceeds max length, the value will be truncated
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setSoftDescriptor(final String softDescriptor) {
		this.softDescriptor = softDescriptor;
		return this;
	}

	/**
	 * Soft descriptor city used when charging this funding source. If length exceeds max length, the value will be truncated. Only supported when the `payment_method` is set to `credit_card`
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setSoftDescriptorCity(final String softDescriptorCity) {
		this.softDescriptorCity = softDescriptorCity;
		return this;
	}



	/**
	 * URL to send payment notifications
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setNotifyUrl(final String notifyUrl) {
		this.notifyUrl = notifyUrl;
		return this;
	}

	/**
	 * Url on merchant site pertaining to this payment.
	 * @return this
	 */
	@SuppressWarnings("all")
	public CartBase setOrderUrl(final String orderUrl) {
		this.orderUrl = orderUrl;
		return this;
	}

}
