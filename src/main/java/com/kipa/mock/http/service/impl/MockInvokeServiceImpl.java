package com.kipa.mock.http.service.impl;

import com.kipa.mock.http.annotation.MockMethod;
import com.kipa.mock.http.entity.BaseMockError;
import com.kipa.mock.http.entity.BaseMockForward;
import com.kipa.mock.http.entity.BaseMockRequest;
import com.kipa.mock.http.entity.BaseMockResponse;
import com.kipa.mock.http.service.MockInvokeService;
import com.kipa.mock.http.service.base.BaseMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 19:25
 * mock底层服务
 */
@Service("mockInvokeService")
public class MockInvokeServiceImpl implements MockInvokeService {

    @Autowired
    private BaseMockService baseMockService;

    @Override
    public void mockResponse(BaseMockRequest baseMockRequest, Map<String, String> requestMap, BaseMockResponse baseMockResponse) {
        BaseMockRequest request = assembleRequest(baseMockRequest, requestMap, null, null);
        baseMockService.mock(MockMethod.PARAMETER, request, baseMockResponse);
    }

    @Override
    public void mockResponse(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockResponse baseMockResponse) {
        BaseMockRequest request = assembleRequest(baseMockRequest, new HashMap<>(), body, charset);
        baseMockService.mock(MockMethod.BODY, request, baseMockResponse);
    }

    @Override
    public void mockForward(BaseMockRequest baseMockRequest, Map<String, String> requestMap, BaseMockForward baseMockForward) {
        BaseMockRequest request = assembleRequest(baseMockRequest, requestMap, null, null);
        baseMockService.mock(MockMethod.PARAMETER, request, baseMockForward);
    }

    @Override
    public void mockForward(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockForward baseMockForward) {
        BaseMockRequest request = assembleRequest(baseMockRequest, new HashMap<>(), body, charset);
        baseMockService.mock( MockMethod.BODY, request, baseMockForward);
    }

    @Override
    public void mockError(BaseMockRequest baseMockRequest, Map<String, String> requestMap, BaseMockError baseMockError) {
        BaseMockRequest request = assembleRequest(baseMockRequest, requestMap, null, null);
        baseMockService.mock(MockMethod.PARAMETER, request, baseMockError);
    }

    @Override
    public void mockError(BaseMockRequest baseMockRequest, String body, Charset charset, BaseMockError baseMockError) {
        BaseMockRequest request = assembleRequest(baseMockRequest, new HashMap<>(), body, charset);
        baseMockService.mock( MockMethod.BODY, request, baseMockError);
    }

    private BaseMockRequest assembleRequest(BaseMockRequest baseMockRequest, Map<String, String> requestMap, String body, Charset charset) {
        baseMockRequest.setRequestParams(requestMap);
        baseMockRequest.setBody(body);
        baseMockRequest.setCharset(charset);
        return baseMockRequest;
    }
}
