package com.kipa.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMockInvoker<C, MR, MT> implements MockInvoker<C, MR, MT> {

    @Override
    public void invoke(C client, MR mockRequest, MT mockResult) {
        if (log.isDebugEnabled()) {
            log.debug("mock请求的参数为：{}，mock响应的请求为：{}",mockRequest, mockResult);
        }
        long start = System.currentTimeMillis();
        mock(client, mockRequest, mockResult);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("执行的时长为：{}ms",end - start);
        }
    }


    public abstract void mock(C client, MR mockRequest, MT mockResult);
}
