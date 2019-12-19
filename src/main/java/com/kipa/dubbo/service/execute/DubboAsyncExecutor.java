package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.exception.DubboInvokeException;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author: Qinyadong
 * @date: 2019/5/13 20:36
 * dubbo异步调用
 */
@Service("dubboAsyncExecutor")
public class DubboAsyncExecutor extends AbstractDubboExecutor {
    @Setter
    private ResponseCallback responseCallback;

    @Override
    Object invoke(GenericService genericService, String methodName, String[] paramTypes, Object[] values) throws Exception {
        Assert.notNull(responseCallback, "异步回调的接口实现不能为空");
        genericService.$invoke(methodName, paramTypes,values);
        FutureAdapter futureAdapter = (FutureAdapter) RpcContext.getContext().getFuture();
        ResponseFuture responseFuture = futureAdapter.getFuture();
        responseFuture.setCallback(responseCallback);
        return responseFuture.get();
    }
}
