package com.kipa.core;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求转化器抽象类
 * @param <R> 将InvokeRequest 转化成结果的的泛型
 */
public abstract class RequestConverter<R> implements Converter<InvokeRequest, R> {
    protected AtomicReference<R> reference;

    public RequestConverter() {
        reference = new AtomicReference<>();
    }
    /**
     * 获取请求
     * @return
     */
    public abstract R getRequest();
}
