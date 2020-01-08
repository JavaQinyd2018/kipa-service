package com.kipa.core;

/**
 * mock执行器统一接口
 * @param <C>
 * @param <MR>
 * @param <MT>
 */
public interface MockInvoker<C, MR, MT> extends Invoker{
    /**
     * mock执行器
     * @param client
     * @param mockRequest
     * @param mockResult
     */
    void invoke(C client, MR mockRequest, MT mockResult);

}
