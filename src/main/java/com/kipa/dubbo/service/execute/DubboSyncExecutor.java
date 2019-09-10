package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.exception.DubboInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:44
 * 同步执行
 */
@Slf4j
@Service("dubboSyncExecutor")
public class DubboSyncExecutor implements DubboExecutor {

    @Override
    public Object execute(GenericService genericService, WrappedDubboParameter wrappedDubboParameter) {
        if (genericService == null) {
            throw new DubboInvokeException("泛化服务生成为空");
        }

        try {
            long startTime = System.currentTimeMillis();
            Object result = genericService.$invoke(wrappedDubboParameter.getMethodName(), wrappedDubboParameter.getParamTypeArray(), wrappedDubboParameter.getValueArray());
            long endTime = System.currentTimeMillis();
            log.info("接口调用执行的总时长为：{}ms",endTime - startTime);
            return result;
        } catch (Exception exception) {
            throw new DubboInvokeException("调用接口失败",exception);
        }
    }
}
