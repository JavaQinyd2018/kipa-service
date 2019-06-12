package com.kipa.mock.dubbo;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:40
 * mock dubbo请求的服务接口
 */
public interface MockDubboService {


    /**
     * dubbo接口的mock
     * @param request mock请求
     * @param response mock响应
     */
    void mock(MockDubboRequest request, MockDubboResponse response);
}
