package com.egzosn.pay.common.bean;

import java.util.Map;

/**
 * 支付订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2020/01/05 13:34
 *  </pre>
 */
public interface Order {

    /**
     * 获取订单属性 这里可用做覆盖已设置的订单信息属性，订单信息在签名前进行覆盖。
     * @return 属性
     */
    Map<String, Object> getAttr();

}
