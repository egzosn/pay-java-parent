package com.egzosn.pay.paypal.v2.bean.order;


import com.alibaba.fastjson.annotation.JSONField;

public class ApplicationContext {

    /**
     * 该标签将覆盖PayPal网站上PayPal帐户中的公司名称
     */
    @JSONField(name = "brand_name")
    private String brandName;

    @JSONField(name = "cancel_url")
    private String cancelUrl;
    /**
     * LOGIN。当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * BILLING。当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * NO_PREFERENCE。当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    @JSONField(name = "landing_page")
    private String landingPage = "NO_PREFERENCE";


    @JSONField(name = "return_url")
    private String returnUrl;
    /**
     * GET_FROM_FILE。使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING。从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS。使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    @JSONField(name = "shipping_preference")
    private String shippingPreference = "NO_SHIPPING";
    /**
     * CONTINUE。将客户重定向到PayPal付款页面后，将出现“ 继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW。将客户重定向到PayPal付款页面后，出现“ 立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    @JSONField(name = "user_action")
    private String userAction = "CONTINUE";

    public ApplicationContext() {
    }


    public String brandName() {
        return brandName;
    }

    public ApplicationContext brandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public String cancelUrl() {
        return this.cancelUrl;
    }

    public ApplicationContext cancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
        return this;
    }

    public String landingPage() {
        return this.landingPage;
    }

    public ApplicationContext landingPage(String landingPage) {
        this.landingPage = landingPage;
        return this;
    }

    public String returnUrl() {
        return this.returnUrl;
    }

    public ApplicationContext returnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public String shippingPreference() {
        return this.shippingPreference;
    }

    public ApplicationContext shippingPreference(String shippingPreference) {
        this.shippingPreference = shippingPreference;
        return this;
    }

    public String userAction() {
        return this.userAction;
    }

    public ApplicationContext userAction(String userAction) {
        this.userAction = userAction;
        return this;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getShippingPreference() {
        return shippingPreference;
    }

    public void setShippingPreference(String shippingPreference) {
        this.shippingPreference = shippingPreference;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }
}
