package com.kipa.mock.http.service;

import com.kipa.mock.http.entity.BaseMockError;
import com.kipa.mock.http.entity.BaseMockForward;
import com.kipa.mock.http.entity.BaseMockRequest;
import com.kipa.mock.http.entity.BaseMockResponse;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 19:16
 */
public interface MockInvokeService {
    /**
     * mock 正常的响应
     * @param baseMockRequest 请求对象
     * @param requestParamMap 通过请求参数的方式
     * @param baseMockResponse mock的相应结果
     */
    void mockResponse(BaseMockRequest baseMockRequest, Map<String, String> requestParamMap, BaseMockResponse baseMockResponse);

    /**
     *
     * @param baseMockRequest 请求对象
     * @param body json的方式
     * @param baseMockResponse mock的相应结果
     */
    void mockResponse(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockResponse baseMockResponse);

    /**
     * forward的方式
     * @param baseMockRequest
     * @param requestParamMap
     * @param baseMockForward
     */
    void mockForward(BaseMockRequest baseMockRequest, Map<String, String> requestParamMap, BaseMockForward baseMockForward);

    /**
     * forward的方式
     * @param baseMockRequest
     * @param body
     * @param baseMockForward
     */
    void mockForward(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockForward baseMockForward);

    /**
     * 错误的方式
     * @param baseMockRequest
     * @param requestParamMap
     * @param baseMockError
     */
    void mockError(BaseMockRequest baseMockRequest, Map<String, String> requestParamMap, BaseMockError baseMockError);

    /**
     * 错误的方式
     * @param baseMockRequest
     * @param body
     * @param baseMockError
     */
    void mockError(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockError baseMockError);
}
