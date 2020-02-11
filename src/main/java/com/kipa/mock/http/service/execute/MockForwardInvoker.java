package com.kipa.mock.http.service.execute;

import com.kipa.core.AbstractMockInvoker;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpForward;
import org.mockserver.model.HttpRequest;

public class MockForwardInvoker extends AbstractMockInvoker<ClientAndServer, HttpRequest, HttpForward> {
    @Override
    public void mock(ClientAndServer client, HttpRequest mockRequest, HttpForward mockResult) {
        client.when(mockRequest).forward(mockResult);
    }
}
