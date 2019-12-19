package com.kipa.mock.http.service.base;

import com.kipa.common.KipaProcessException;
import com.kipa.mock.http.annotation.InvokeType;
import com.kipa.mock.http.annotation.MockMethod;
import com.kipa.mock.http.annotation.MockType;
import com.kipa.mock.http.entity.*;
import com.kipa.mock.http.service.execute.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpForward;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:45
 * 动态代理
 */
@Service("baseMockService")
public class MockServiceFactoryBean implements FactoryBean<BaseMockService>, InitializingBean {

    private ClientAndServer clientAndServer;

    @Autowired
    private MockServerProperties mockServerProperties;

    private MockRequestConvert mockRequestConvert;
    private MockForwardConvert mockForwardConvert;
    private MockErrorConvert mockErrorConvert;
    private MockResponseConvert mockResponseConvert;
    private MockResponseExecutor mockResponseExecutor;
    private MockForwardExecutor mockForwardExecutor;
    private MockErrorExecutor mockErrorExecutor;

    @Override
    public BaseMockService getObject() throws Exception {
        return (BaseMockService) Proxy.newProxyInstance(BaseMockService.class.getClassLoader(), new Class[]{BaseMockService.class}, new MockServiceHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return BaseMockService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mockRequestConvert = new MockRequestConvert();
        mockErrorConvert = new MockErrorConvert();
        mockErrorExecutor = new MockErrorExecutor();
        mockForwardConvert = new MockForwardConvert();
        mockResponseConvert = new MockResponseConvert();
        mockResponseExecutor = new MockResponseExecutor();
        mockForwardExecutor = new MockForwardExecutor();
        MockServiceClientFactory clientFactory = new MockServiceClientFactory();
        clientAndServer = clientFactory.create(mockServerProperties);
    }

    class MockServiceHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            InvokeType annotation = method.getAnnotation(InvokeType.class);
            MockMethod mockMethod = (MockMethod) args[0];
            BaseMockRequest baseMockRequest = (BaseMockRequest) args[1];

            baseMockRequest.setMockMethod(mockMethod);
            HttpRequest httpRequest = mockRequestConvert.convert(baseMockRequest);
            MockType type = annotation.type();
            switch (type) {
                case RESPONSE:
                    BaseMockResponse baseMockResponse = (BaseMockResponse) args[2];
                    HttpResponse httpResponse = mockResponseConvert.convert(baseMockResponse);
                    mockResponseExecutor.execute(clientAndServer, httpRequest, httpResponse);
                    break;
                case FORWARD:
                    BaseMockForward baseMockForward = (BaseMockForward) args[2];
                    HttpForward httpForward = mockForwardConvert.convert(baseMockForward);
                    mockForwardExecutor.execute(clientAndServer, httpRequest, httpForward);
                    break;
                case ERROR:
                    BaseMockError baseMockError = (BaseMockError) args[2];
                    HttpError httpError = mockErrorConvert.convert(baseMockError);
                    mockErrorExecutor.execute(clientAndServer, httpRequest, httpError);
                    break;
                default:
                    break;
            }
            return null;
        }
    }
}
