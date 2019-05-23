package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.WrappedDubboParameter;
import com.kipa.dubbo.exception.DubboInvokeException;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:44
 * 同步执行
 */
@Service("dubboSyncExecutor")
public class DubboSyncExecutor implements DubboExecutor {

    @Override
    public Object execute(GenericService genericService, WrappedDubboParameter wrappedDubboParameter) {
        if (genericService == null) {
            throw new DubboInvokeException("泛化服务生成为空");
        }

        try {
            return genericService.$invoke(wrappedDubboParameter.getMethodName(),wrappedDubboParameter.getParamTypeArray(), wrappedDubboParameter.getValueArray());
        } catch (Exception exception) {
            throw new DubboInvokeException("调用接口失败",exception);
        }
    }
}
