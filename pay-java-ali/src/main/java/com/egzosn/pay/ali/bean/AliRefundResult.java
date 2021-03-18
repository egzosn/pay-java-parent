package com.egzosn.pay.ali.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.RefundOrder;

/**
 * 支付宝退款结果返回
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 17:53
 * </pre>
 */
public class AliRefundResult extends BaseRefundResult {

    /**
     * 网关返回码,详见文档 	40004
     */
    private String code;
    /**
     * 网关返回码描述,详见文档 	Business Failed
     */
    private String msg;
    /**
     * 业务返回码，参见具体的API接口文档 	ACQ.TRADE_HAS_SUCCESS
     */
    @JSONField(name = "sub_code")
    private String subCode;

    /**
     * 业务返回码描述，参见具体的API接口文档 	交易已被支付
     */
    @JSONField(name = "sub_msg")
    private String subMsg;
    /**
     * 签名,详见文档
     */
    private String sign;
    /**
     * 支付宝交易号
     */
    @JSONField(name = "trade_no")
    private String tradeNo;
    /**
     * 商户订单号 	6823789339978248
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 标识一次退款请求，同一笔交易多次退款需要保证唯一
     * <p>
     * 因支付宝退款结果中没有返回此参数，该参数从{@link RefundOrder#getRefundNo()}获取
     */
    private String outRequestNo;
    /**
     * 用户的登录id 	159****5620
     */
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    /**
     * 本次退款是否发生了资金变化 	Y
     */
    @JSONField(name = "fund_change")
    private String fundChange;
    /**
     * 退款总金额 	88.88
     */
    @JSONField(name = "refund_fee")
    private BigDecimal refundFee;
    /**
     * 退款币种信息 	USD
     */
    @JSONField(name = "refund_currency")
    private DefaultCurType refundCurrency;
    /**
     * 退款支付时间 	2014-11-27 15:45:57
     */
    @JSONField(name = "gmt_refund_pay")
    private Date gmtRefundPay;

    /**
     * 退款使用的资金渠道。
     * 只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。
     */
    @JSONField(name = "refund_detail_item_list")
    private List<TradeFundBill> refundDetailItemList;
    /**
     * 交易在支付时候的门店名称
     */
    @JSONField(name = "store_name")
    private String storeName;
    /**
     * 买家在支付宝的用户id 	2088101117955611
     */
    @JSONField(name = "buyer_user_id")
    private String buyerUserId;
    /**
     * 退回的前置资产列表
     */
    @JSONField(name = "refund_preset_paytool_list")
    private PresetPayToolInfo refundPresetPaytoolList;
    /**
     * 退款清算编号，用于清算对账使用；
     * 只在银行间联交易场景下返回该信息； 	2018101610032004620239146945
     */
    @JSONField(name = "refund_settlement_id")
    private String refundSettlementId;
    /**
     * 本次退款金额中买家退款金额 	88.88
     */
    @JSONField(name = "present_refund_buyer_amount")
    private String presentRefundBuyerAmount;
    /**
     * 本次退款金额中平台优惠退款金额 	88.88
     */
    @JSONField(name = "present_refund_discount_amount")
    private String presentRefundDiscountAmount;
    /**
     * 本次退款金额中商家优惠退款金额 	88.88
     */
    @JSONField(name = "present_refund_mdiscount_amount")
    private String presentRefundMdiscountAmount;



    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return subCode;
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return subMsg;
    }

    /**
     * 退款金额
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {
        return refundFee;
    }

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    @Override
    public CurType getRefundCurrency() {
        return refundCurrency;
    }

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    @Override
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    @Override
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return outRequestNo;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getFundChange() {
        return fundChange;
    }

    public void setFundChange(String fundChange) {
        this.fundChange = fundChange;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public void setRefundCurrency(DefaultCurType refundCurrency) {
        this.refundCurrency = refundCurrency;
    }

    public Date getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(Date gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }

    public List<TradeFundBill> getRefundDetailItemList() {
        return refundDetailItemList;
    }

    public void setRefundDetailItemList(List<TradeFundBill> refundDetailItemList) {
        this.refundDetailItemList = refundDetailItemList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public PresetPayToolInfo getRefundPresetPaytoolList() {
        return refundPresetPaytoolList;
    }

    public void setRefundPresetPaytoolList(PresetPayToolInfo refundPresetPaytoolList) {
        this.refundPresetPaytoolList = refundPresetPaytoolList;
    }

    public String getRefundSettlementId() {
        return refundSettlementId;
    }

    public void setRefundSettlementId(String refundSettlementId) {
        this.refundSettlementId = refundSettlementId;
    }

    public String getPresentRefundBuyerAmount() {
        return presentRefundBuyerAmount;
    }

    public void setPresentRefundBuyerAmount(String presentRefundBuyerAmount) {
        this.presentRefundBuyerAmount = presentRefundBuyerAmount;
    }

    public String getPresentRefundDiscountAmount() {
        return presentRefundDiscountAmount;
    }

    public void setPresentRefundDiscountAmount(String presentRefundDiscountAmount) {
        this.presentRefundDiscountAmount = presentRefundDiscountAmount;
    }

    public String getPresentRefundMdiscountAmount() {
        return presentRefundMdiscountAmount;
    }

    public void setPresentRefundMdiscountAmount(String presentRefundMdiscountAmount) {
        this.presentRefundMdiscountAmount = presentRefundMdiscountAmount;
    }
    public static final AliRefundResult create(Map<String, Object> result){
        AliRefundResult refundResult = new JSONObject(result).toJavaObject(AliRefundResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }
}
