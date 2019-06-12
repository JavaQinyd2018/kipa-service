package com.kipa.mock.dubbo;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/6/6 15:56
 * mock dubbo 接口运行容器
 */
public class MockDubboServiceContainer implements SmartLifecycle, InitializingBean {

    private MockDubboGenericService mockDubboGenericService;
    private volatile boolean initialized = false;

    @Autowired
    private MockDubboServiceRegister mockDubboServiceRegister;

    @Autowired
    private MockDubboConfig mockDubboConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<MockDubboRequest, MockDubboResponse> map = MockDubboContextHolder.get();
        if (MapUtils.isNotEmpty(map)) {
            Map.Entry<MockDubboRequest, MockDubboResponse> entry = map.entrySet().iterator().next();
            MockDubboRequest request = entry.getKey();
            MockDubboResponse response = entry.getValue();
            mockDubboGenericService = new MockDubboGenericService(request,response);
            initialized = true;
        }
    }

    @Override
    public void start() {
        if (initialized) {

        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(Runnable callback) {

    }

    @Override
    public int getPhase() {
        return 0;
    }


}
