package com.egzosn.pay.wx.v3.bean.sharing;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.wx.v3.bean.response.order.TradeState;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信退款结果
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class WxProfitSharingReturnResult extends BaseRefundResult {

    /**
     * 分账回退的接收商户，对应原分账出资的分账方商户，填写微信支付分配的商户号
     * 直连商户不用传二级商户号。
     */
    @JSONField(name = "sub_mchid")
    private String subMchid;

    /**
     * 微信分账单号，微信系统返回的唯一标识。
     * 示例值：3008450740201411110007820472
     */
    @JSONField(name = "order_id")
    private String orderId;
    /**
     * 商户分账单号
     * 商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。 取值范围：[0-9a-zA-Z_*@-]
     * 示例值：P20150806125346
     */
    @JSONField(name = WxConst.OUT_ORDER_NO)
    private String outOrderNo;
    /**
     * 此回退单号是商户在自己后台生成的一个新的回退单号，在商户后台唯一
     * 示例值：R20190516001
     */
    @JSONField(name = "out_return_no")
    private String outReturnNo;
    /**
     * 微信分账回退单号，微信系统返回的唯一标识
     * 示例值：3008450740201411110007820472
     */
    @JSONField(name = "return_id")
    private String returnId;
    /**
     * 回退商户号
     * 只能对原分账请求中成功分给商户接收方进行回退
     * 示例值：86693852
     */
    @JSONField(name = "return_mchid")
    private String returnMchid;


    /**
     * 回退金额
     * 需要从分账接收方回退的金额，单位为分，只能为整数
     * 示例值：10
     */
    private Integer amount;

    /**
     * 分账回退的原因描述
     * 示例值：用户退款
     */
    private String description;
    /**
     如果请求返回为处理中，则商户可以通过调用回退结果查询接口获取请求的最终处理结果。如果查询到回退结果在处理中，请勿变更商户回退单号，使用相同的参数再次发起分账回退，否则会出现资金风险。在处理中状态的回退单如果5天没有成功，会因为超时被设置为已失败。
     枚举值：
     PROCESSING：处理中
     SUCCESS：已成功
     FAILED：已失败
     示例值：SUCCESS
     */
    private TradeState result;
    /**
     * 失败原因。包含以下枚举值：
     * ACCOUNT_ABNORMAL : 分账接收方账户异常
     * TIME_OUT_CLOSED : 超时关单
     * 示例值：TIME_OUT_CLOSED
     */
    @JSONField(name = "fail_reason")
    private String failReason;

    /**
     *  	分账回退创建时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35.120+08:00
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 分账回退完成时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35.120+08:00
     */
    @JSONField(name = "finish_time")
    private String finishTime;


    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return result.name();
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return failReason;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return result.name();
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return failReason;
    }

    /**
     * 退款金额, 金额元
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {

        return new BigDecimal(amount);
    }

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    @Override
    public CurType getRefundCurrency() {
        return null;
    }

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    @Override
    public String getTradeNo() {
        return orderId;
    }

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    @Override
    public String getOutTradeNo() {
        return outOrderNo;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return outReturnNo;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getOutReturnNo() {
        return outReturnNo;
    }

    public void setOutReturnNo(String outReturnNo) {
        this.outReturnNo = outReturnNo;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getReturnMchid() {
        return returnMchid;
    }

    public void setReturnMchid(String returnMchid) {
        this.returnMchid = returnMchid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TradeState getResult() {
        return result;
    }

    public void setResult(TradeState result) {
        this.result = result;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public static final WxProfitSharingReturnResult create(Map<String, Object> result) {
        WxProfitSharingReturnResult refundResult = new JSONObject(result).toJavaObject(WxProfitSharingReturnResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }
}
