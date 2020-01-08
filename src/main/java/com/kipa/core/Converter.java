package com.kipa.core;

import java.io.IOException;

/**
 * 转化器统一接口
 * @param <R> 转化的请求泛型
 * @param <V> 转化的结果泛型
 */
public interface Converter<R, V> {

    /**
     * 转化统一的方法
     * @param r 转化的请求泛型
     * @return 转化的结果泛型
     * @throws IOException 异常
     */
    V convert(R r) throws Exception;
}
