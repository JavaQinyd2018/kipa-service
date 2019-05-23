package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.exception.DubboInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/5/13 20:36
 * dubbo异步调用
 */
@Service("dubboAsyncExecutor")
@Slf4j
public class DubboAsyncExecutor implements DubboExecutor {

    @Override
    public Object execute(GenericService genericService, WrappedDubboParameter wrappedDubboParameter) {
        if (genericService == null) {
            throw new DubboInvokeException("泛化服务生成为空");
        }

        try {
            genericService.$invoke(wrappedDubboParameter.getMethodName(),wrappedDubboParameter.getParamTypeArray(), wrappedDubboParameter.getValueArray());
            FutureAdapter futureAdapter = (FutureAdapter) RpcContext.getContext().getFuture();
            ResponseFuture responseFuture = futureAdapter.getFuture();
            responseFuture.setCallback(wrappedDubboParameter.getResponseCallback());
            return responseFuture.get();
        } catch (Exception exception) {
            throw new DubboInvokeException("调用接口失败",exception);
        }
    }
}
