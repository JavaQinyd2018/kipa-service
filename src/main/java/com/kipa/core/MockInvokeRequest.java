package com.kipa.core;

import lombok.Data;

/**
 * mock 调用请求
 * @param <MR>
 * @param <MT>
 */
@Data
public class MockInvokeRequest<MR, MT> implements InvokeRequest{

    protected MR mockRequest;

    protected MT mockResult;
}
