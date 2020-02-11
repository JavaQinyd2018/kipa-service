package com.kipa.dubbo.excute;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.core.AsyncInvoker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DubboAsyncInvoker implements AsyncInvoker<GenericService, WrappedDubboParameter, ResponseCallback> {

    @Override
    public void invoke(GenericService client, WrappedDubboParameter request, ResponseCallback callBack) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("dubbo接口请求的参数信息为：{}",request);
        }
        long start = System.currentTimeMillis();
        client.$invoke(request.getMethodName(), request.getParamTypeArray(), request.getValueArray());
        FutureAdapter futureAdapter = (FutureAdapter) RpcContext.getContext().getFuture();
        ResponseFuture responseFuture = futureAdapter.getFuture();
        responseFuture.setCallback(callBack);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("当前dubbo调用的时长为：{}， 处理的结果为：{}",end - start, responseFuture.get());
        }
    }
}
