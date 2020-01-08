package com.kipa.core;

/**
 * @author Qinyadong
 * 异步调用的调用器
 * @param <C> client 客户端对象
 * @param <R> request 第三方api的请求对象
 * @param <A> 异步回调的接口
 */
public interface AsyncInvoker<C, R, A> extends Invoker {

    /**
     * 异步调用的方法
     * @param client 客户端对象
     * @param request 第三方api的请求对象
     * @param callBack 异步回调的接口
     * @throws Exception 出现的异常
     */
    void invoke(C client, R request, A callBack) throws Exception;

}
