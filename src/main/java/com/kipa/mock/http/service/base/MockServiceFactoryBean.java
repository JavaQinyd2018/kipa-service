package com.kipa.mock.http.service.base;

import com.kipa.core.BaseExecutor;
import com.kipa.core.InvokeRequest;
import com.kipa.core.MockInvokeRequest;
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

    @Autowired
    private MockServerProperties mockServerProperties;

    private MockRequestConverter mockRequestConvert;
    private MockForwardConverter mockForwardConvert;
    private MockErrorConverter mockErrorConvert;
    private MockResponseConverter mockResponseConvert;

    private BaseExecutor<ClientAndServer, HttpRequest, HttpResponse> responseBaseExecutor;
    private BaseExecutor<ClientAndServer, HttpRequest, HttpError> errorBaseExecutor;
    private BaseExecutor<ClientAndServer, HttpRequest, HttpForward> forwardBaseExecutor;

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
        final MockServiceClientFactory clientFactory = new MockServiceClientFactory();
        final ClientAndServer clientAndServer = clientFactory.create(mockServerProperties);
        mockRequestConvert = new MockRequestConverter();
        mockForwardConvert = new MockForwardConverter();
        mockErrorConvert = new MockErrorConverter();
        mockResponseConvert = new MockResponseConverter();
        final MockResponseInvoker responseInvoker = new MockResponseInvoker();
        final MockErrorInvoker errorInvoker = new MockErrorInvoker();
        final MockForwardInvoker forwardInvoker = new MockForwardInvoker();
        responseBaseExecutor = new BaseExecutor<>(clientAndServer, responseInvoker, mockRequestConvert, null);
        errorBaseExecutor = new BaseExecutor<>(clientAndServer, errorInvoker, mockRequestConvert, null);
        forwardBaseExecutor = new BaseExecutor<>(clientAndServer, forwardInvoker, mockRequestConvert, null);
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
                    MockInvokeRequest<HttpRequest, HttpResponse> mockInvokeRequest = new MockInvokeRequest<>();
                    mockInvokeRequest.setMockRequest(httpRequest);
                    mockInvokeRequest.setMockResult(httpResponse);
                    responseBaseExecutor.execute(mockInvokeRequest);
                    break;
                case FORWARD:
                    BaseMockForward baseMockForward = (BaseMockForward) args[2];
                    HttpForward httpForward = mockForwardConvert.convert(baseMockForward);
                    MockInvokeRequest<HttpRequest, HttpForward> errorMockInvokeRequest = new MockInvokeRequest<>();
                    errorMockInvokeRequest.setMockRequest(httpRequest);
                    errorMockInvokeRequest.setMockResult(httpForward);
                    forwardBaseExecutor.execute(errorMockInvokeRequest);
                    break;
                case ERROR:
                    BaseMockError baseMockError = (BaseMockError) args[2];
                    MockInvokeRequest<HttpRequest, HttpError> forwardMockInvokeRequest = new MockInvokeRequest<>();
                    forwardMockInvokeRequest.setMockRequest(httpRequest);
                    HttpError httpError = mockErrorConvert.convert(baseMockError);
                    forwardMockInvokeRequest.setMockResult(httpError);
                    errorBaseExecutor.execute(forwardMockInvokeRequest);
                    break;
                default:
                    break;
            }
            return null;
        }
    }
}
