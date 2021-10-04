package com.egzosn.pay.common.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map生成工具
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class MapGen<K, V> {

    /**
     * 属性
     */
    private Map<K, V> attr;

    public MapGen(K key, V value) {
        keyValue(key, value);
    }

    public MapGen<K, V> keyValue(K key, V value) {
        if (null == attr){
            attr = new LinkedHashMap<>();
        }
        attr.put(key, value);
        return this;
    }


    public Map<K, V> getAttr() {
        return attr;
    }

    private void setAttr(Map<K, V> attr) {
        this.attr = attr;
    }
}
