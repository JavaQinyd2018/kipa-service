package com.kipa.env;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/6
 * dubbo 消费的线程副本
 */
public class DubboContextHolder {

    private static final ThreadLocal<Map<String, Object>> contextHolder = new ThreadLocal<>();

    public static Map<String, Object> getConfig() {
        return contextHolder.get();
    }

    public static void setConfig(Map<String, Object> configMap) {
        contextHolder.set(configMap);
    }

    public static void removeConfig() {
        contextHolder.remove();
    }
}
