package com.egzosn.pay.common.bean;

/**
 * 支付订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2020/01/05 13:34
 *  </pre>
 */
public interface Order extends Attrs {


    /**
     * 添加订单信息
     *
     * @param key   key
     * @param value 值
     */
    void addAttr(String key, Object value);

}
