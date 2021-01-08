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
	 * @param amount 金额
	 */
	public CartBase(Amount amount) {
		this.amount = amount;
	}

	/**
	 * Merchant identifier to the purchase unit. Optional parameter
	 * @return  identifier
	 */
	public String getReferenceId() {
		return this.referenceId;
	}

	/**
	 * Amount being collected.
	 * @return amount 金额
	 */
	public Amount getAmount() {
		return this.amount;
	}

	/**
	 * Recipient of the funds in this transaction.
	 * @return Recipient
	 */
	public Payee getPayee() {
		return this.payee;
	}

	/**
	 * Description of what is being paid for.
	 * @return Description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Note to the recipient of the funds in this transaction.
	 * @return Note
	 */
	public String getNoteToPayee() {
		return this.noteToPayee;
	}

	/**
	 * free-form field for the use of clients
	 * @return custom
	 */
	public String getCustom() {
		return this.custom;
	}

	/**
	 * invoice number to track this payment
	 * @return invoice number
	 */
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	/**
	 * Soft descriptor used when charging this funding source. If length exceeds max length, the value will be truncated
	 * @return SoftDescriptor
	 */
	public String getSoftDescriptor() {
		return this.softDescriptor;
	}

	/**
	 * Soft descriptor city used when charging this funding source. If length exceeds max length, the value will be truncated. Only supported when the `payment_method` is set to `credit_card`
	 * @return Soft descriptor
	 */
	public String getSoftDescriptorCity() {
		return this.softDescriptorCity;
	}


	/**
	 * URL to send payment notifications
	 * @return URL
	 */
	public String getNotifyUrl() {
		return this.notifyUrl;
	}

	/**
	 * Url on merchant site pertaining to this payment.
	 * @return URL
	 */
	public String getOrderUrl() {
		return this.orderUrl;
	}

	/**
	 * Merchant identifier to the purchase unit. Optional parameter
	 * @param referenceId identifier
	 * @return this
	 */
	public CartBase setReferenceId(final String referenceId) {
		this.referenceId = referenceId;
		return this;
	}

	/**
	 * Amount being collected.
	 * @param amount 金额
	 * @return this
	 */
	public CartBase setAmount(final Amount amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Recipient of the funds in this transaction.
	 * @param payee Recipient
	 * @return this
	 */
	public CartBase setPayee(final Payee payee) {
		this.payee = payee;
		return this;
	}

	/**
	 * Description of what is being paid for.
	 * @param description description
	 * @return this
	 */
	public CartBase setDescription(final String description) {
		this.description = description;
		return this;
	}

	/**
	 * Note to the recipient of the funds in this transaction.
	 * @param noteToPayee noteToPayee
	 * @return this
	 */
	public CartBase setNoteToPayee(final String noteToPayee) {
		this.noteToPayee = noteToPayee;
		return this;
	}

	/**
	 * free-form field for the use of clients
	 * @param custom custom
	 * @return this
	 */
	public CartBase setCustom(final String custom) {
		this.custom = custom;
		return this;
	}

	/**
	 * invoice number to track this payment
	 * @param invoiceNumber invoiceNumber
	 * @return this
	 */
	public CartBase setInvoiceNumber(final String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	/**
	 * Soft descriptor used when charging this funding source. If length exceeds max length, the value will be truncated
	 * @param softDescriptor softDescriptor
	 * @return this
	 */
	public CartBase setSoftDescriptor(final String softDescriptor) {
		this.softDescriptor = softDescriptor;
		return this;
	}

	/**
	 * Soft descriptor city used when charging this funding source. If length exceeds max length, the value will be truncated. Only supported when the `payment_method` is set to `credit_card`
	 * @param softDescriptorCity softDescriptorCity
	 * @return this
	 */
	public CartBase setSoftDescriptorCity(final String softDescriptorCity) {
		this.softDescriptorCity = softDescriptorCity;
		return this;
	}



	/**
	 * URL to send payment notifications
	 * @param notifyUrl notifyUrl
	 * @return this
	 */
	public CartBase setNotifyUrl(final String notifyUrl) {
		this.notifyUrl = notifyUrl;
		return this;
	}

	/**
	 * Url on merchant site pertaining to this payment.
	 * @param orderUrl orderUrl
	 * @return this
	 */
	public CartBase setOrderUrl(final String orderUrl) {
		this.orderUrl = orderUrl;
		return this;
	}

}
