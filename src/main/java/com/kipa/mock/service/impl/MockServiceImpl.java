package com.kipa.mock.service.impl;

import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.mock.entity.BaseMockError;
import com.kipa.mock.entity.BaseMockForward;
import com.kipa.mock.entity.BaseMockRequest;
import com.kipa.mock.entity.BaseMockResponse;
import com.kipa.mock.service.MockInvokeService;
import com.kipa.mock.service.MockService;
import com.kipa.mock.service.bo.*;
import com.kipa.utils.PreCheckUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.nio.charset.Charset;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 19:05
 * mock服务类
 */
@Service("mockService")
public class MockServiceImpl implements MockService {

    @Autowired
    private MockInvokeService mockInvokeService;

    @Override
    public void mockResponse(MockParamRequest request, MockResponse response) {
       check(request);
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        BaseMockResponse baseMockResponse = new BaseMockResponse();
        BeanUtils.copyProperties(response, baseMockResponse);
        mockInvokeService.mockResponse(baseMockRequest, request.getRequestParams(), baseMockResponse);
    }

    @Override
    public void mockResponse(MockBodyRequest request, MockResponse response) {
        check(request);
        PreCheckUtils.checkEmpty(request.getBody(), "http请求的消息体不能为空");
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        Charset charset = request.getCharset() == null ? Charset.forName("utf8") : request.getCharset();
        BaseMockResponse baseMockResponse = new BaseMockResponse();
        BeanUtils.copyProperties(response, baseMockResponse);
        mockInvokeService.mockResponse(baseMockRequest, request.getBody(), charset, baseMockResponse);
    }

    @Override
    public void mockForward(MockParamRequest request, MockForward forward) {
        check(request);
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        BaseMockForward baseMockForward = new BaseMockForward();
        BeanUtils.copyProperties(forward, baseMockForward);
        mockInvokeService.mockForward(baseMockRequest, request.getRequestParams(), baseMockForward);
    }

    @Override
    public void mockForward(MockBodyRequest request, MockForward forward) {
        check(request);
        PreCheckUtils.checkEmpty(request.getBody(), "http请求的消息体不能为空");
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        BaseMockForward baseMockForward = new BaseMockForward();
        BeanUtils.copyProperties(forward, baseMockForward);
        Charset charset = request.getCharset() == null ? Charset.forName("utf8") : request.getCharset();
        mockInvokeService.mockForward(baseMockRequest, request.getBody(), charset, baseMockForward);
    }

    @Override
    public void mockError(MockParamRequest request, MockError mockError) {
        check(request);
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        BaseMockError baseMockError = new BaseMockError();
        BeanUtils.copyProperties(mockError, baseMockError);
        mockInvokeService.mockError(baseMockRequest, request.getRequestParams(), baseMockError);
    }

    @Override
    public void mockError(MockBodyRequest request, MockError mockError) {
        check(request);
        PreCheckUtils.checkEmpty(request.getBody(), "http请求的消息体不能为空");
        BaseMockRequest baseMockRequest = new BaseMockRequest();
        BeanUtils.copyProperties(request, baseMockRequest);
        baseMockRequest.setHttpSendMethod(HttpSendMethod.getMethod(request.getMethod()));
        BaseMockError baseMockError = new BaseMockError();
        BeanUtils.copyProperties(mockError, baseMockError);
        Charset charset = request.getCharset() == null ? Charset.forName("utf8") : request.getCharset();
        mockInvokeService.mockError(baseMockRequest, request.getBody(), charset, baseMockError);
    }

    private void check(MockParamRequest request) {
        PreCheckUtils.checkEmpty(request, "mock请求的参数不能为空");
        PreCheckUtils.checkEmpty(request.getPath(), "http请求的路径不能为空");
        PreCheckUtils.checkEmpty(request.getMethod(), "http请求的方法不能为空");
    }

    private void check(MockBodyRequest request) {
        PreCheckUtils.checkEmpty(request, "mock请求的参数不能为空");
        PreCheckUtils.checkEmpty(request.getPath(), "http请求的路径不能为空");
        PreCheckUtils.checkEmpty(request.getMethod(), "http请求的方法不能为空");
    }
}
