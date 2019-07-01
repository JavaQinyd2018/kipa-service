package com.kipa.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Qinyadong
 * @date 2019/7/1 14:35
 * @desciption 参数map集合
 */
public final class ParamMap<K, V> {

    private Map<K, V> map;

    private ParamMap(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> ParamMap<K, V> newMap() {
        return new ParamMap<>(new ConcurrentHashMap<K, V>());
    }

    public static <K, V> ParamMap<K, V> newMap(Map<K, V> map) {
        return new ParamMap<>(map);
    }

    public ParamMap<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return this.map;
    }
}
