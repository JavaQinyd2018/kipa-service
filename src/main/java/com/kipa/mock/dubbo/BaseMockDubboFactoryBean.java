package com.kipa.mock.dubbo;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;
import org.testng.collections.Maps;

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

    
    static class MockDubboInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MockDubboRequest request = (MockDubboRequest) args[0];
            MockDubboResponse response = (MockDubboResponse) args[1];

            return null;
        }

        private Map<String, Object> parse(MockDubboRequest request, MockDubboResponse response) {
            Map<String, Object> mockInfo = Maps.newHashMap();
            Map<String, Object> classValuePair = request.getClassValuePair();
            if (MapUtils.isNotEmpty(classValuePair)) {
                String[] paramTypeArray = new String[classValuePair.size()];
                Object[] valueArray = new Object[classValuePair.size()];
                for (int i = 0; i < classValuePair.entrySet().size(); i++) {

                }
            }
            mockInfo.put("interface",request.getInterfaceName());
            mockInfo.put("methodName", request.getMethodName());
            mockInfo.put("paramTypeArray", "");
            return null;
        }
    }
}
