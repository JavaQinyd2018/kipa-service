package com.kipa.mock.dubbo;

/**
 * @author Qinyadong
 * mock dubbo的回调接口
 */
public interface MockCallback {

    /**
     * 处理请求之后的结果
     * @param request
     * @return
     */
    Object process(MockDubboRequest request);
}
