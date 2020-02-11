package com.kipa.core;

import lombok.Data;

@Data
public class MockInvokeRequest<MR, MT> implements InvokeRequest{

    protected MR mockRequest;

    protected MT mockResult;
}
