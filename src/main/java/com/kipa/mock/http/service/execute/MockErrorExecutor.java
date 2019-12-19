package com.kipa.mock.http.service.execute;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpRequest;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:31
 *
 */
public class MockErrorExecutor implements MockExecutor<HttpError> {
    @Override
    public void execute(ClientAndServer clientAndServer, HttpRequest httpRequest, HttpError action) {
        clientAndServer.when(httpRequest).error(action);
    }
}
