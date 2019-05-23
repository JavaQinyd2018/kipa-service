package com.kipa.env;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/7
 * http标识传递线程副本
 */
public class HttpContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getFlag() {
        return contextHolder.get();
    }

    public static void setFlag(String flag) {
        contextHolder.set(flag);
    }

    public static void removeFlag() {
        contextHolder.remove();
    }
}
