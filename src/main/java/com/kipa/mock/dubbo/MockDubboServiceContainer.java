package com.kipa.mock.dubbo;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: Qinyadong
 * @date: 2019/6/6 15:56
 * mock dubbo 接口运行容器
 */
@Service
public class MockDubboServiceContainer implements ApplicationListener<ContextRefreshedEvent>, DisposableBean, InitializingBean {

    private MockDubboGenericService mockDubboGenericService;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private AtomicBoolean running = new AtomicBoolean(false);
    @Autowired
    private MockDubboServiceRegister mockDubboServiceRegister;

    @Autowired
    private MockDubboProperties mockDubboProperties;

    private String interfaceName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (initialized.get() && !running.get()) {
            running.set(true);
            mockDubboServiceRegister.exportService(mockDubboProperties, interfaceName, mockDubboGenericService);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<MockDubboRequest, MockDubboResponse> map = MockDubboContextHolder.get();
        if (MapUtils.isNotEmpty(map)) {
            Map.Entry<MockDubboRequest, MockDubboResponse> entry = map.entrySet().iterator().next();
            MockDubboRequest request = entry.getKey();
            MockDubboResponse response = entry.getValue();
            mockDubboGenericService = new MockDubboGenericService(request,response);
            interfaceName = request.getInterfaceName();
            initialized.set(true);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (running.get()) {
            running.set(false);
        }
    }
}
