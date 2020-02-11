package com.kipa.core;

/**
 * @author qinyadong
 * 同步调用器
 * @param <C> client 调用的客户端对象
 * @param <R> request 调用的请求参数
 * @param <V> response 响应的处理结果
 */
public interface SyncInvoker<C, R, V> extends Invoker {

    /**
     * 调用的统一方法
     * @param client 调用的客户端对象
     * @param request 调用的请求参数
     * @return 响应的处理结果
     * @throws Exception 异常
     */
    V invoke(C client, R request) throws Exception;

}
