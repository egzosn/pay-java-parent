package com.egzosn.pay.paypal.v2.bean;

import java.math.BigDecimal;
import java.util.Map;

import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;

/**
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/1/16
 * </pre>
 */
public class PayPalRefundResult extends BaseRefundResult {

    /**
     * 支付平台订单号,交易号
     */
    private String tradeNo;

    public PayPalRefundResult(Map<String, Object> attrs, String tradeNo) {
        super(attrs);
        this.tradeNo = tradeNo;
    }

    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return getAttrString("state");
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return null;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return getAttrString("state");
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return null;
    }

    /**
     * 退款金额
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {
        return null;
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
        return null;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return getAttrString("id");
    }
}
