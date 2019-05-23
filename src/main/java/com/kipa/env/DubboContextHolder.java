package com.kipa.env;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/6
 * 传递参数
 */
public class DubboContextHolder {

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
