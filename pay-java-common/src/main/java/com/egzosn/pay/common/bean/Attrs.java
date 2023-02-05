package com.egzosn.pay.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 属性信息
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/10/8
 * </pre>
 */
public interface Attrs extends Serializable {

    /**
     * 获取属性 这里可用做覆盖已设置的信息属性，订单信息在签名前进行覆盖。
     *
     * @return 属性
     */
    Map<String, Object> getAttrs();

    /**
     * 获取属性 这里可用做覆盖已设置的订单信息属性，订单信息在签名前进行覆盖。
     *
     * @param key 属性名
     * @return 属性
     */
    default Object getAttr(String key) {
        return getAttrs().get(key);
    }


    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @return 属性
     */
    default Number getAttrForNumber(String key) {
        final Object attr = getAttr(key);
        if (null == attr || "".equals(attr)) {
            return null;
        }
        if (attr instanceof Number) {
            return (Number) attr;
        }

        return new BigDecimal(attr.toString());
    }

    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @return 属性
     */
    default Integer getAttrForInt(String key) {
        Number attr = getAttrForNumber(key);
        if (null == attr) {
            return null;
        }
        if (attr instanceof Integer) {
            return (Integer) attr;
        }
        return attr.intValue();
    }

    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @param defaultValue 默认值
     * @return 属性
     */
    default Integer getAttrForInt(String key, Integer defaultValue) {
        Integer value = getAttrForInt(key);
        return null == value ? defaultValue : value;
    }

    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @return 属性
     */
    default Long getAttrForLong(String key) {
        Number attr = getAttrForNumber(key);
        if (null == attr) {
            return null;
        }
        if (attr instanceof Long) {
            return (Long) attr;
        }

        return attr.longValue();
    }

    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @return 属性
     */
    default String getAttrForString(String key) {
        return (String) getAttr(key);
    }

    /**
     * 获取属性 这里可用做覆盖已设置的属性信息属性。
     *
     * @param key 属性名
     * @return 属性
     */
    default Date getAttrForDate(String key) {
        return (Date) getAttr(key);
    }
}
