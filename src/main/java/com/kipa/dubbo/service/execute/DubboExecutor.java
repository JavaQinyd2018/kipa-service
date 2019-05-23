package com.kipa.dubbo.service.execute;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.dubbo.entity.WrappedDubboParameter;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:40
 */
public interface DubboExecutor {

    /**
     * dubbo泛化的真实调用
     * @param genericService
     * @param wrappedDubboParameter
     * @return
     */
    Object execute(GenericService genericService, WrappedDubboParameter wrappedDubboParameter);
}
