package com.kipa.core;

/**
 * 接口的执行器
 * @param <C> 执行的类型
 */
public interface Executor<C> {

    /**
     * 获取执行的客户端
     * @return
     */
    C getClient();

    /**
     * 执行
     * @param invokeRequest
     * @return
     * @throws Exception
     */
    InvokeResponse execute(InvokeRequest invokeRequest) throws Exception;
}
