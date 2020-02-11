package com.kipa.core;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 响应结果转化器
 * 将第三方的结果 V 转化成 统一的请求对象InvokeResponse
 * @param <V> 响应结果的泛型
 */
public abstract class ResponseConverter<V> implements Converter<V, InvokeResponse> {

    protected AtomicReference<V> reference;

    public ResponseConverter() {
        reference = new AtomicReference<>();
    }

    /**
     * 获取相应结果
     * @return
     */
    public abstract V getResponse();
}
