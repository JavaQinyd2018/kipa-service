package com.kipa.core;

import lombok.Setter;

/**
 * @author Qinyadong
 * 基础的一个执行器，主要的作用是：
 * 1. 通过转化器Convert 转化请求对象invokeRequest，同时将 第三方的api处理完成之后转化成
 * InvokeResponse
 * 2. 同步不同的请求去调用不同的调用器： 同步调用器和异步调用器，拿到具体的第三方api处理的
 * 结果
 * @param <C> client 客户端
 * @param <R>  request 请求对象
 * @param <V>  response 或者 callback 对象
 */
public class BaseExecutor<C, R, V> implements Executor<C>{

    @Setter
    private C client;

    private Invoker invoker;
    private RequestConverter<R> requestConverter;
    private ResponseConverter<V> responseConverter;

    public BaseExecutor(C client,
                        Invoker invoker,
                        RequestConverter<R> requestConverter,
                        ResponseConverter<V> responseConverter) {
        this.client = client;
        this.invoker = invoker;
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    public BaseExecutor(Invoker invoker,
                        RequestConverter<R> requestConverter,
                        ResponseConverter<V> responseConverter) {
        this.invoker = invoker;
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    @Override
    public C getClient() {
        return client;
    }

    /**
     * 核心的调用逻辑
     * @param invokeRequest
     * @return
     * @throws Exception
     */
    @Override
    public InvokeResponse execute(InvokeRequest invokeRequest) throws Exception {
        if (invoker instanceof SyncInvoker) {
            R request = requestConverter.convert(invokeRequest);
            SyncInvoker<C, R, V> syncInvoker = (SyncInvoker<C, R, V>) invoker;
            V response = syncInvoker.invoke(client, request);
            return responseConverter.convert(response);
        }else if (invoker instanceof AsyncInvoker){
            AsyncInvoker<C, R, V> asyncInvoker = (AsyncInvoker<C, R, V>) invoker;
            AsyncInvokeRequest<V> asyncInvokeRequest = (AsyncInvokeRequest<V>) invokeRequest;
            R request = requestConverter.convert(asyncInvokeRequest);
            V callback = asyncInvokeRequest.getCallback();
            asyncInvoker.invoke(client, request, callback);
            //1. 无返回值
        }else if (invoker instanceof MockInvoker){
            MockInvoker<C, R, V> mockInvoker = (MockInvoker<C, R, V>) invoker;
            MockInvokeRequest<R, V> mockInvokeRequest = (MockInvokeRequest<R, V>) invokeRequest;
            R mockRequest = mockInvokeRequest.getMockRequest();
            V mockResult = mockInvokeRequest.getMockResult();
            mockInvoker.invoke(client, mockRequest, mockResult);
            //2. 无返回值
        }
        return null;
    }
}
