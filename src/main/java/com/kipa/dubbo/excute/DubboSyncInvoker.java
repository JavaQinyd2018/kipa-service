package com.kipa.dubbo.excute;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.core.SyncInvoker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DubboSyncInvoker implements SyncInvoker<GenericService,WrappedDubboParameter, Object > {

    @Override
    public Object invoke(GenericService client, WrappedDubboParameter request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("dubbo接口请求的参数信息为：{}",request);
        }
        long start = System.currentTimeMillis();
        Object invoke = client.$invoke(request.getMethodName(), request.getParamTypeArray(), request.getValueArray());
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("当前dubbo调用的时长为：{}",end - start);
        }
        return invoke;
    }
}
