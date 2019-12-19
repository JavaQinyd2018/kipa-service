package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.common.core.Executor;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.exception.DubboInvokeException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:40
 */
@Slf4j
public abstract class AbstractDubboExecutor implements Executor<GenericService, WrappedDubboParameter, Object> {

    @Override
    public Object execute(GenericService genericService, WrappedDubboParameter request) throws Exception {
        if (genericService == null) {
            throw new DubboInvokeException("泛化服务生成为空");
        }
        long startTime = System.currentTimeMillis();
        Object result = invoke(genericService, request.getMethodName(), request.getParamTypeArray(), request.getValueArray());
        long endTime = System.currentTimeMillis();
        log.info("接口调用执行的总时长为：{}ms",endTime - startTime);
        return result;
    }

    /**
     * 同步或者异步实际的调用逻辑
     * @param genericService
     * @param methodName
     * @param paramTypes
     * @param values
     * @return
     */
    abstract Object invoke(GenericService genericService, String methodName, String[] paramTypes, Object[] values) throws Exception;
}
