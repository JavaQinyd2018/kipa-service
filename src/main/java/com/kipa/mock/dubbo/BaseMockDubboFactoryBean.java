package com.kipa.mock.dubbo;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:46
 * mock dubbo接口的代理类
 */
@Service("mockDubboService")
public class BaseMockDubboFactoryBean implements FactoryBean<MockDubboService>{

    private static Map<MockDubboRequest, MockDubboResponse> mockInfoMap = new ConcurrentHashMap<>();

    @Override
    public MockDubboService getObject() throws Exception {
        return (MockDubboService) Proxy.newProxyInstance(MockDubboService.class.getClassLoader(),
                new Class[]{MockDubboService.class},
                new MockDubboInvocationHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return MockDubboService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }


    
    class MockDubboInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MockDubboRequest request = (MockDubboRequest) args[0];
            MockDubboResponse response = (MockDubboResponse) args[1];
            mockInfoMap.put(request, response);
            MockDubboContextHolder.set(mockInfoMap);
            return null;
        }
    }
}
