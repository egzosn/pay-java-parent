package com.egzosn.pay.wx.v3.bean.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.bean.response.order.Amount;
import com.egzosn.pay.wx.v3.bean.response.order.Payer;
import com.egzosn.pay.wx.v3.bean.response.order.PromotionDetail;
import com.egzosn.pay.wx.v3.bean.response.order.TradeState;

/**
 * 支付回调消息,兼容退款回调
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class WxPayMessage extends PayMessage {




    /**
     * 直连模式应用ID，服务商模式请解析spAppid
     */
    private String appid;
    /**
     * 直连模式商户号，服务商模式请解析spMchid
     */
    private String mchid;
    /**
     * 服务商模式服务商APPID
     */
    @JSONField(name = "sp_appid")
    private String spAppid;
    /**
     * 服务商模式服务商户号
     */
    @JSONField(name = "sp_mchid")
    private String spMchid;
    /**
     * 服务商模式-子商户appid
     */
    @JSONField(name = "sub_appid")
    private String subAppid;
    /**
     * 服务商模式-子商户商户id
     */
    @JSONField(name = "sub_mchid")
    private String subMchid;

    /**
     * 商户订单号
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 微信支付订单号
     * 微信支付系统生成的订单号。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 交易类型，枚举值：
     * JSAPI：公众号支付
     * NATIVE：扫码支付
     * APP：APP支付
     * MICROPAY：付款码支付
     * MWEB：H5支付
     * FACEPAY：刷脸支付
     * 示例值：MICROPAY
     */
    @JSONField(name = "trade_type")
    private WxTransactionType tradeType;


    /**
     * 交易状态，枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（付款码支付）
     * USERPAYING：用户支付中（付款码支付）
     * PAYERROR：支付失败(其他原因，如银行返回失败)
     */
    @JSONField(name = "trade_state")
    private TradeState tradeState;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    /**
     * 退款状态，枚举值：
     * SUCCESS：退款成功
     * CLOSE：退款关闭
     * ABNORMAL: 退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往【商户平台—交易中心】，手动处理此笔退款
     * 示例值：SUCCESS
     */
    @JSONField(name = "refund_status")
    private TradeState refundStatus;
    /**
     * 退款入账账户
     * 取当前退款单的退款入账方。
     * 1、退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2、退回支付用户零钱: 支付用户零钱
     * 3、退还商户: 商户基本账户、商户结算银行账户
     * 4、退回支付用户零钱通：支付用户零钱通
     * 示例值：招商银行信用卡0403
     */
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;
    /**
     * 交易状态描述
     * 示例值：支付成功
     */
    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;
    /**
     * 银行类型，采用字符串类型的银行标识。
     * 银行标识请参考 <a target= "_blank" href= "https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6">《银行类型对照表》</a>
     * 示例值：CMC
     */
    @JSONField(name = "bank_type")
    private String bankType;

    /**
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     */
    private String attach;

    /**
     * 支付完成时间|| 退款完成时间，遵循rfc3339标准格式，
     * 格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，
     * TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。
     * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "success_time", format = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date successTime;
    /**
     * 支付者信息
     */
    private Payer payer;


    /**
     * 订单金额
     */
    private Amount amount;
    /**
     * 支付场景信息描述
     */
    @JSONField(name = "scene_info")
    private SceneInfo sceneInfo;

    /**
     * 优惠功能，享受优惠时返回该字段。
     */
    @JSONField(name = "promotion_detail")
    private List<PromotionDetail> promotionDetail;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getSpAppid() {
        return spAppid;
    }

    public void setSpAppid(String spAppid) {
        this.spAppid = spAppid;
    }

    public String getSpMchid() {
        return spMchid;
    }

    public void setSpMchid(String spMchid) {
        this.spMchid = spMchid;
    }

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public WxTransactionType getTradeType() {
        return tradeType;
    }

    public void setTradeType(WxTransactionType tradeType) {
        this.tradeType = tradeType;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public List<PromotionDetail> getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(List<PromotionDetail> promotionDetail) {
        this.promotionDetail = promotionDetail;
    }

    @Override
    public BigDecimal getTotalFee() {
        return BigDecimal.valueOf(getAmount().getTotal());
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public TradeState getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(TradeState refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getUserReceivedAccount() {
        return userReceivedAccount;
    }

    public void setUserReceivedAccount(String userReceivedAccount) {
        this.userReceivedAccount = userReceivedAccount;
    }

    public static WxPayMessage create(Map<String, Object> message) {
        WxPayMessage payMessage = new JSONObject(message).toJavaObject(WxPayMessage.class);
//        payMessage.setPayType("");
        payMessage.setPayMessage(message);
        return payMessage;
    }
}
