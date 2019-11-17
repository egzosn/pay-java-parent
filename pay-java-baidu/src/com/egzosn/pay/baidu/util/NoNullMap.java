package com.egzosn.pay.baidu.util;

import java.util.HashMap;

public class NoNullMap<K, V> extends HashMap<K, V> {
    
    public NoNullMap<K, V> putIfNoNull(K key, V value) {
        if (value != null) {
            put(key, value);
        }
        return this;
    }
    
}
