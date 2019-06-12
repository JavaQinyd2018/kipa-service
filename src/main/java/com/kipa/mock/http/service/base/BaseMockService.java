package com.kipa.mock.http.service.base;

import com.kipa.mock.http.annotation.InvokeType;
import com.kipa.mock.http.annotation.MockMethod;
import com.kipa.mock.http.annotation.MockType;
import com.kipa.mock.http.entity.BaseMockError;
import com.kipa.mock.http.entity.BaseMockForward;
import com.kipa.mock.http.entity.BaseMockRequest;
import com.kipa.mock.http.entity.BaseMockResponse;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:35
 * mockservice基础服务类
 */
public interface BaseMockService {

    /**
     * mock正常
     * @param baseMockRequest
     * @return
     */
    @InvokeType(type = MockType.RESPONSE)
    void mock(MockMethod mockMethod, BaseMockRequest baseMockRequest, BaseMockResponse baseMockResponse);

    /**
     * 跳转
     * @param baseMockRequest
     * @return
     */
    @InvokeType(type = MockType.FORWARD)
    void mock(MockMethod mockMethod, BaseMockRequest baseMockRequest, BaseMockForward baseMockForward);

    /**
     * 错误
     * @param baseMockRequest
     * @return
     */
    @InvokeType(type = MockType.ERROR)
    void mock(MockMethod mockMethod, BaseMockRequest baseMockRequest, BaseMockError baseMockError);
}
