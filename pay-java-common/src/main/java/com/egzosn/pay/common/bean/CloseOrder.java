package com.egzosn.pay.common.bean;

import java.util.Map;

/**
 * 关闭订单
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class CloseOrder implements Order {

    /**
     * 支付平台订单号,交易号
     */
    private String tradeNo;
    /**
     * 商户单号
     */
    private String outTradeNo;


    /**
     * 订单附加信息，可用于预设未提供的参数，这里会覆盖以上所有的订单信息，
     */
    private Map<String, Object> attr;

    public CloseOrder() {
    }

    public CloseOrder(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }



    /**
     * 添加订单信息
     *
     * @param key   key
     * @param value 值
     */
    @Override
    public void addAttr(String key, Object value) {
        attr.put(key, value);
    }

    /**
     * 获取属性 这里可用做覆盖已设置的信息属性，订单信息在签名前进行覆盖。
     *
     * @return 属性
     */
    @Override
    public Map<String, Object> getAttrs() {
        return attr;
    }

    /**
     * 获取属性 这里可用做覆盖已设置的订单信息属性，订单信息在签名前进行覆盖。
     *
     * @param key 属性名
     * @return 属性
     */
    @Override
    public Object getAttr(String key) {
        return attr.get(key);
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
}
