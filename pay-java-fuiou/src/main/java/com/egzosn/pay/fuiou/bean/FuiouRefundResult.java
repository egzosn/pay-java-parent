package com.egzosn.pay.fuiou.bean;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;

/**
 * 富友退款结果
 * @author Egan、
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 21:17
 * </pre>
 */
public class FuiouRefundResult extends BaseRefundResult {

    private String originOrderId;
    /**
     * 退款状态
     * ‘0’ – 已受理
     * ‘1’ – 成功
     * ‘2’ – 失败
     */
    @JSONField(name = "order_st")
    private String orderSt;
    /**
     * 错误代码
     * 5341表示退款成功
     */
    @JSONField(name = "order_pay_code")
    private String orderPayCode;
    /**
     * 错误中文描述
     * 5341表示退款成功
     */
    @JSONField(name = "order_pay_error")
    private String orderPayError;
    /**
     * 富友流水号
     * 供商户查询支付交易状态及对账用
     */
    @JSONField(name = "fy_ssn")
    private String fySsn;
    /**
     * 保留字段
     */
    private String resv1;
    /**
     * MD5摘要数据
     * mchnt_cd + "|" + order_st + "|" +
     * order_pay_code + "|" +
     * order_pay_error + "|" + fy_ssn + "|" +
     * resv1 + "|" + mchnt_key
     * 做MD5摘要
     * 其中mchnt_key 为32位的商户密钥，系统分配
     */
    private String md5;

    /**
     * 退款金额
     */
    private BigDecimal refundAmt;

    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return orderSt;
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return orderPayError;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return orderPayCode;
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return orderPayError;
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
        return fySsn;
    }

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    @Override
    public String getOutTradeNo() {
        return originOrderId;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return null;
    }

    public static final FuiouRefundResult create(Map<String, Object> result){
        FuiouRefundResult refundResult = new JSONObject(result).toJavaObject(FuiouRefundResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }

}
