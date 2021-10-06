package com.egzosn.pay.common.bean;

import java.util.Date;
import java.util.Map;

import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 订单参数构造器
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/8/16
 * </pre>
 */
public final class OrderParaStructure {
    private OrderParaStructure() {
    }

    public static Map<String, Object> loadParameters(Map<String, Object> parameters, String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            parameters.put(key, value);
        }
        return parameters;
    }

    public static Map<String, Object> loadParameters(Map<String, Object> parameters, String key, Order order) {
        Object attr = order.getAttr(key);
        if (null != attr && !"".equals(attr)) {
            order.getAttrs().remove(key);
            parameters.put(key, attr);
        }
        return parameters;
    }

    public static Map<String, Object> loadDateParameters(Map<String, Object> parameters, String key, Order order, String datePattern) {
        return OrderParaStructure.loadParameters(parameters, key, DateUtils.formatDate((Date) order.getAttr(key), datePattern));
    }


}
