package com.kipa.mock.http.service.execute;

import com.kipa.core.AbstractMockInvoker;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpRequest;

public class MockErrorInvoker extends AbstractMockInvoker<ClientAndServer, HttpRequest, HttpError> {
    @Override
    public void mock(ClientAndServer client, HttpRequest mockRequest, HttpError mockResult) {
        client.when(mockRequest).error(mockResult);
    }
}
