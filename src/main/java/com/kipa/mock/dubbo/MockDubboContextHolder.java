package com.kipa.mock.dubbo;

import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/6/6 16:07
 * 传递 mock的相关信息
 */
public class MockDubboContextHolder {

    private static ThreadLocal<Map<MockDubboRequest, MockDubboResponse>> mockInfoThread
            = new ThreadLocal<>();

    public static Map<MockDubboRequest,MockDubboResponse> get() {
        return mockInfoThread.get();
    }

    public static void set(Map<MockDubboRequest, MockDubboResponse> map) {
        mockInfoThread.set(map);
    }

    public static void remove() {
        mockInfoThread.remove();
    }
}
