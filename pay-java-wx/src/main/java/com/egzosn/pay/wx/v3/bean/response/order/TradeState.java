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
    /**
     * 如果请求返回为处理中，则商户可以通过调用回退结果查询接口获取请求的最终处理结果。如果查询到回退结果在处理中，请勿变更商户回退单号，使用相同的参数再次发起分账回退，否则会出现资金风险。在处理中状态的回退单如果5天没有成功，会因为超时被设置为已失败。
     * 处理中
     */
    PROCESSING,
    /**
     * 失败
     */
    FAILED;
}