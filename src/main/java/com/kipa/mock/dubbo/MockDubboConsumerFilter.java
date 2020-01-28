package com.kipa.mock.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 15:00
 * mock 服务的过滤器
 * ====接口是：interface com.alibaba.dubbo.rpc.service.GenericService
 * =====url是：dubbo://10.18.10.125:20889/com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService?anyhost=true&application=kipa-dubbo-comsumer&async=false&check=false&connections=10&dubbo=2.6.2&generic=true&interface=com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService&methods=queryUserInfoByPhoneNo,getInfo,getUserByUsername&organization=kipa-org&owner=kipa&pid=1420&protocol=dubbo&reference.filter=mockDubboConsumerFilter&register.ip=10.18.10.125&remote.timestamp=1578554407055&retries=3&side=consumer&timeout=12000&timestamp=1578558966840
 * =====调用信息是：RpcInvocation [methodName=$invoke, parameterTypes=[class java.lang.String, class [Ljava.lang.String;, class [Ljava.lang.Object;], arguments=[queryUserInfoByPhoneNo, [Ljava.lang.String;@24e31f06, [Ljava.lang.Object;@5d260494], attachments={generic=true}]
 */
@Activate(group = Constants.CONSUMER,order = -33000)
public class MockDubboConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        System.out.println("=============信息===============");
/*        System.out.println(url.getServiceInterface());
        Object[] arguments1 = invocation.getArguments();
        System.out.println(JSON.toJSONString(arguments1));*/
        System.out.println(url.getServiceInterface());


        String methodName = RpcUtils.getMethodName(invocation);
        System.out.println("方法："+methodName);
        Class<?>[] parameterTypes = RpcUtils.getParameterTypes(invocation);
        System.out.println("参数类型" + Arrays.toString(parameterTypes));

        Object[] arguments = RpcUtils.getArguments(invocation);
        System.out.println("参数值："+Arrays.toString(arguments));

        Class<?> returnType = RpcUtils.getReturnType(invocation);
        System.out.println("返回值类型：" + returnType);
        System.out.println("==============信息==============");

        return invoker.invoke(invocation);
    }

    private boolean checkIfMock(String url) {
        return false;
    }
}
