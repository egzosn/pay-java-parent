package com.egzosn.pay.paypal.v2.bean;

import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.paypal.v2.bean.order.ShippingDetail;

/**
 * PayPal付款订单
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/1/12
 * </pre>
 */
public class PayPalOrder extends PayOrder {

    /**
     * 该标签将覆盖PayPal网站上PayPal帐户中的公司名称
     */
    private String brandName;
    /**
     * 支付成功之后回调的页面
     */
    private String returnUrl;

    /**
     * 取消支付的页面
     * <pre>
     * 注意：这里不是异步回调的通知
     * IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * </pre>
     */
    private String cancelUrl;
    /**
     * LOGIN。当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * BILLING。当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * NO_PREFERENCE。当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    private String landingPage = "NO_PREFERENCE";

    /**
     * GET_FROM_FILE。使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING。从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS。使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    private String shippingPreference = "NO_SHIPPING";
    /**
     * CONTINUE。将客户重定向到PayPal付款页面后，将出现“ 继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW。将客户重定向到PayPal付款页面后，出现“ 立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    private String userAction = "CONTINUE";

    private ShippingDetail shippingDetail;
    /**
     * API调用者为购买单元提供的外部ID。当您必须通过“补丁”更新订单时，需要多个购买单位。如果忽略该值，且订单只包含一个购买单元，PayPal将该值设置为' default '。
     */
    private String referenceId;
    /**
     * API调用者为该订单提供的外部发票号码。出现在付款人的交易历史记录和付款人收到的电子邮件中
     */
    private String invoiceId;


    /**
     * API调用者提供的外部ID。用于协调客户端交易与PayPal交易。出现在交易和结算报告中，但付款人不可见。
     *
     * @return 外部ID
     */
    public String getCustomId() {
        return super.getOutTradeNo();
    }

    /**
     * /**
     * API调用者提供的外部ID。用于协调客户端交易与PayPal交易。出现在交易和结算报告中，但付款人不可见。
     *
     * @param customId 外部ID
     */
    public void setCustomId(String customId) {
        super.setOutTradeNo(customId);
    }


    public String getDescription() {
        return super.getSubject();
    }

    public void setDescription(String description) {
        super.setSubject(description);
    }


    public String getCurrencyCode() {
        CurType curType = super.getCurType();
        if (null == curType) {
            curType = DefaultCurType.USD;
        }
        return curType.getType();
    }

    public void setCurrencyCode(CurType currencyCode) {
        super.setCurType(currencyCode);
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        super.addAttr("brandName", brandName);
        this.brandName = brandName;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        super.addAttr("returnUrl", returnUrl);
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        super.addAttr("cancelUrl", cancelUrl);
        this.cancelUrl = cancelUrl;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        super.addAttr("landingPage", landingPage);
        this.landingPage = landingPage;
    }

    public String getShippingPreference() {
        return shippingPreference;
    }

    public void setShippingPreference(String shippingPreference) {
        super.addAttr("shippingPreference", shippingPreference);
        this.shippingPreference = shippingPreference;
    }

    public String getUserAction() {

        return userAction;
    }

    public void setUserAction(String userAction) {
        super.addAttr("userAction", userAction);
        this.userAction = userAction;
    }

    public ShippingDetail getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(ShippingDetail shippingDetail) {
        super.addAttr("shippingDetail", shippingDetail);
        this.shippingDetail = shippingDetail;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        super.addAttr("referenceId", referenceId);
        this.referenceId = referenceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        super.addAttr("invoiceId", referenceId);
        this.invoiceId = invoiceId;
    }
}
