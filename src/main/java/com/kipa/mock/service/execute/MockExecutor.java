package com.kipa.mock.service.execute;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:17
 * mock执行器
 */
public interface MockExecutor<T> {

    void execute(ClientAndServer clientAndServer,
                 HttpRequest httpRequest,
                 T action);

}
