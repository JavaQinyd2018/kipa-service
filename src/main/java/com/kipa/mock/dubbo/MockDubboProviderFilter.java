package com.kipa.mock.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 15:00
 * mock 服务的过滤器
 */
@Activate(group = Constants.PROVIDER,order = -30000)
public class MockDubboProviderFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        String interfaceName = invocation.getAttachment("interface");
        String methodName = invocation.getMethodName();
        Object[] typeNameArray = Arrays.stream(invocation.getParameterTypes()).map(Class::getName).toArray();
        Object[] arguments = invocation.getArguments();
        rpcInvocation.setArguments(new Object[]{interfaceName + "." + methodName, ArrayUtils.toStringArray(typeNameArray), arguments});
        rpcInvocation.setMethodName(Constants.$INVOKE);
        rpcInvocation.setParameterTypes(new Class[]{String.class, String[].class, Object[].class});
        return invoker.invoke(rpcInvocation);
    }
}
