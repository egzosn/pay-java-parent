package com.egzosn.pay.wx.v3.bean.response;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.wx.v3.bean.order.RefundAmount;

/**
 * 微信退款结果
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 21:29
 * </pre>
 */
public class WxRefundResult extends BaseRefundResult {

    /**
     * 500 	SYSTEM_ERROR 	系统超时 	请不要更换商户退款单号，请使用相同参数再次调用API。
     * 403 	USER_ACCOUNT_ABNORMAL 	退款请求失败 	此状态代表退款申请失败，商户可自行处理退款。
     * 403 	NOT_ENOUGH 	余额不足 	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
     * 400 	PARAM_ERROR 	参数错误 	请求参数错误，请重新检查再调用申请退款接口
     * 404 	MCH_NOT_EXISTS 	MCHID不存在 	请检查MCHID是否正确
     * 404 	RESOURCE_NOT_EXISTS 	订单号不存在 	请检查你的订单号是否正确且是否已支付，未支付的订单不能发起退款
     * 401 	SIGN_ERROR 	签名错误 	请检查签名参数和方法是否都符合签名算法要求
     * 429 	FREQUENCY_LIMITED 	频率限制 	该笔退款未受理，请降低频率后重试
     * 400 	INVALID_REQUEST 	请求参数符合参数格式，但不符合业务规则 	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
     * 403 	NO_AUTH 	没有退款权限 	此状态代表退款申请失败，请检查是否有该笔订单的退款权限
     */
    private String code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 微信订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;


    /**
     * 退款渠道
     * 枚举值：
     * ORIGINAL：原路退款
     * BALANCE：退回到余额
     * OTHER_BALANCE：原账户异常退到其他余额账户
     * OTHER_BANKCARD：原银行卡异常退到其他银行卡
     * 示例值：ORIGINAL
     */
    private String channel;

    /**
     * 退款入账账户
     * 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     * 示例值：招商银行信用卡0403
     */
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;
    /**
     * 退款成功时间
     * 退款成功时间，当退款状态为退款成功时有返回。
     * 示例值：2020-12-01T16:18:12+08:00
     */
    @JSONField(name = "success_time")
    private String successTime;
    /**
     * 退款创建时间
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 退款状态
     * 退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往服务商平台-交易中心，手动处理此笔退款。
     * 枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * PROCESSING：退款处理中
     * ABNORMAL：退款异常
     * 示例值：SUCCESS
     */
    private String status;

    /**
     * 资金账户
     * 退款所使用资金对应的资金账户类型
     * 枚举值：
     * UNSETTLED : 未结算资金
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     * OPERATION : 运营户
     * BASIC : 基本账户（含可用余额和不可用余额）
     */
    @JSONField(name = "funds_account")
    private String fundsAccount;

    /**
     * 金额详细信息
     */
    private RefundAmount amount;

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
        return message;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return status;
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return message;
    }

    /**
     * 退款金额, 金额元
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {

        return new BigDecimal(amount.getRefund()).divide(Util.HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    @Override
    public CurType getRefundCurrency() {
        return DefaultCurType.valueOf(amount.getCurrency());
    }

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    @Override
    public String getTradeNo() {
        return transactionId;
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
        return outRefundNo;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserReceivedAccount() {
        return userReceivedAccount;
    }

    public void setUserReceivedAccount(String userReceivedAccount) {
        this.userReceivedAccount = userReceivedAccount;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFundsAccount() {
        return fundsAccount;
    }

    public void setFundsAccount(String fundsAccount) {
        this.fundsAccount = fundsAccount;
    }

    public RefundAmount getAmount() {
        return amount;
    }

    public void setAmount(RefundAmount amount) {
        this.amount = amount;
    }

    public static final WxRefundResult create(Map<String, Object> result) {
        WxRefundResult refundResult = new JSONObject(result).toJavaObject(WxRefundResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }
}
