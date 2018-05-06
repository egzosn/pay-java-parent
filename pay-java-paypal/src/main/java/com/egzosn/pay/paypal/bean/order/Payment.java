package com.egzosn.pay.paypal.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 贝宝付款订单
 * <pre>
 * 说明付款订单信息
 *
 *
 *
 * </pre>
 *
 * @author egan
 *
 * email egzosn@gmail.com
 * date 2018/04/28 11:09
 */
public class Payment {

    /**
     * Identifier of the payment resource created.
     */
    private String id;
    /**
     * Payment intent.
     */
    private String intent;
    /**
     * Source of the funds for this payment represented by a PayPal account or a direct credit card.
     */
    private Payer payer;

    /**
     * Receiver of funds for this payment. **Readonly for PayPal external REST payments.**
     */
    private Payee payee;
    /**
     * ID of the cart to execute the payment.
     */
    private String cart;
    /**
     * Transactional details including the amount and item details.
     */
    private List<Transaction> transactions;
    /**
     * Applicable for advanced payments like multi seller payment (MSP) to support partial failures
     */
    @JSONField(name = "failed_transactions")
    private List<Error> failedTransactions;

    /**
     * The state of the payment, authorization, or order transaction. The value is:<ul><li><code>created</code>. The transaction was successfully created.</li><li><code>approved</code>. The buyer approved the transaction.</li><li><code>failed</code>. The transaction request failed.</li></ul>
     */
    private String state;
    /**
     * PayPal generated identifier for the merchant's payment experience profile. Refer to [this](https://developer.paypal.com/docs/api/#payment-experience) link to create experience profile ID.
     */
    @JSONField(name = "experience_profile_id")
    private String experienceProfileId;
    /**
     * free-form field for the use of clients to pass in a message to the payer
     */
    @JSONField(name = "note_to_payer")
    private String noteToPayer;
    /**
     * Set of redirect URLs you provide only for PayPal-based payments.
     */
    @JSONField(name = "redirect_urls")
    private RedirectUrls redirectUrls;
    /**
     * Failure reason code returned when the payment failed for some valid reasons.
     */
    @JSONField(name = "failure_reason")
    private String failureReason;
    /**
     * Payment creation time as defined in [RFC 3339 Section 5.6](http://tools.ietf.org/html/rfc3339#section-5.6).
     */
    @JSONField(name = "create_time")
    private String createTime;
    /**
     * Payment update time as defined in [RFC 3339 Section 5.6](http://tools.ietf.org/html/rfc3339#section-5.6).
     */
    @JSONField(name = "update_time")
    private String updateTime;
    /**
     */
    private List<Links> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Error> getFailedTransactions() {
        return failedTransactions;
    }

    public void setFailedTransactions(List<Error> failedTransactions) {
        this.failedTransactions = failedTransactions;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExperienceProfileId() {
        return experienceProfileId;
    }

    public void setExperienceProfileId(String experienceProfileId) {
        this.experienceProfileId = experienceProfileId;
    }

    public String getNoteToPayer() {
        return noteToPayer;
    }

    public void setNoteToPayer(String noteToPayer) {
        this.noteToPayer = noteToPayer;
    }

    public RedirectUrls getRedirectUrls() {
        return redirectUrls;
    }

    public void setRedirectUrls(RedirectUrls redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }
}
