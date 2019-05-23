package com.kipa.mock.service.execute;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:20
 * 正常响应执行器
 */
@Service("mockResponseExecutor")
public class MockResponseExecutor implements MockExecutor<HttpResponse> {
    @Override
    public void execute(ClientAndServer clientAndServer,
                                           HttpRequest httpRequest,
                                           HttpResponse httpResponse) {
        clientAndServer.when(httpRequest).respond(httpResponse);
    }
}
