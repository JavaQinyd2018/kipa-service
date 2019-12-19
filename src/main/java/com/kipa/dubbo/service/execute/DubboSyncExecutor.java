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
public class DubboSyncExecutor extends AbstractDubboExecutor {
    @Override
    Object invoke(GenericService genericService, String methodName, String[] paramTypes, Object[] values) throws Exception{
        return genericService.$invoke(methodName, paramTypes, values);
    }
}
