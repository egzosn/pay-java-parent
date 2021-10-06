package com.egzosn.pay.wx.v3.bean.response.order;

/**
 * 微信侧返回交易状态,兼容退款状态
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public enum TradeState {
    /**
     * 支付成功
     * 退款成功
     */
    SUCCESS,
    /**
     * 转入退款
     */
    REFUND,
    /**
     * 未支付
     */
    NOTPAY,
    /**
     * 已关闭
     * 退款关闭
     */
    CLOSED,
    /**
     * 退款异常.
     */
    ABNORMAL,
    /**
     * 已撤销（付款码支付）
     */
    REVOKED,
    /**
     * 用户支付中（付款码支付）
     */
    USERPAYING,
    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    PAYERROR,
    /**
     * 已接收，等待扣款
     */
    ACCEPT,
}