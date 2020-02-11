package com.kipa.mock.http.service.execute;

import com.kipa.core.AbstractMockInvoker;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public class MockResponseInvoker extends AbstractMockInvoker<ClientAndServer, HttpRequest, HttpResponse> {
    @Override
    public void mock(ClientAndServer client, HttpRequest mockRequest, HttpResponse mockResult) {
        client.when(mockRequest).respond(mockResult);
    }
}
