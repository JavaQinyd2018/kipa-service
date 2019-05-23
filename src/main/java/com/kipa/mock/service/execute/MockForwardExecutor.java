package com.kipa.mock.service.execute;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpForward;
import org.mockserver.model.HttpRequest;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:22
 */
@Service("mockForwardExecutor")
public class MockForwardExecutor implements MockExecutor<HttpForward> {
    @Override
    public void execute(ClientAndServer clientAndServer,
                                           HttpRequest httpRequest,
                                           HttpForward httpForward) {
        clientAndServer.when(httpRequest).forward(httpForward);
    }
}
