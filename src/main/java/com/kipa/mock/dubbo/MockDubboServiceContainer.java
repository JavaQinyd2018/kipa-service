package com.kipa.mock.dubbo;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: Qinyadong
 * @date: 2019/6/6 15:56
 * mock dubbo 接口运行容器
 */
@Service
public class MockDubboServiceContainer implements SmartLifecycle, InitializingBean {

    private MockDubboGenericService mockDubboGenericService;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private AtomicBoolean running = new AtomicBoolean(false);
    @Autowired
    private MockDubboServiceRegister mockDubboServiceRegister;

    @Autowired
    private MockDubboProperties mockDubboProperties;

    private String interfaceName;

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
    public void start() {
        if (initialized.get() && !isRunning()) {
            running.set(true);
            mockDubboServiceRegister.exportService(mockDubboProperties, interfaceName, mockDubboGenericService);
        }
    }

    @Override
    public void stop() {
        if (running.get()) {
            running.set(false);
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        stop();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


}
