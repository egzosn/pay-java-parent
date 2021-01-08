package com.egzosn.pay.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 退款结果
 * <p>
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 9:55
 * </pre>
 */
public interface RefundResult extends Serializable {
    /**
     * 获取退款结果原信息集
     *
     * @return 属性
     */
    Map<String, Object> getAttrs();

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    Object getAttr(String key);

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    String getAttrString(String key);

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    BigDecimal getAttrDecimal(String key);


    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    String getCode();

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    String getMsg();

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    String getResultCode();

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    String getResultMsg();

    /**
     * 退款金额
     *
     * @return 退款金额
     */
    BigDecimal getRefundFee();

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    CurType getRefundCurrency();

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    String getTradeNo();

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    String getOutTradeNo();

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    String getRefundNo();
}
