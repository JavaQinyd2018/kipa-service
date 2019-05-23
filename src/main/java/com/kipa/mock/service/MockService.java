package com.kipa.mock.service;

import com.kipa.mock.service.bo.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:35
 * mock的服务类
 */
public interface  MockService {

    /**
     * mock 请求参数
     * @param request
     * @param response
     */
    void mockResponse(MockParamRequest request, MockResponse response);

    /**
     * mock请求body
     * @param request
     * @param response
     */
    void mockResponse(MockBodyRequest request, MockResponse response);

    /**
     * mock forward
     * @param request
     * @param forward
     */
    void mockForward(MockParamRequest request, MockForward forward);

    /**
     * mock forward
     * @param request
     * @param forward
     */
    void mockForward(MockBodyRequest request, MockForward forward);

    /**
     * mock Error
     * @param request
     * @param forward
     */
    void mockError(MockParamRequest request, MockError mockError);

    /**
     * mock Error
     * @param request
     * @param forward
     */
    void mockError(MockBodyRequest request, MockError mockError);
}
