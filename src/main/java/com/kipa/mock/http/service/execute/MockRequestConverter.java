package com.kipa.mock.http.service.execute;

import com.kipa.common.KipaProcessException;
import com.kipa.core.InvokeRequest;
import com.kipa.core.RequestConverter;
import com.kipa.mock.http.annotation.MockMethod;
import com.kipa.mock.http.entity.BaseMockRequest;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.mockserver.model.HttpRequest;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 16:13
 */
@Slf4j
public class MockRequestConverter extends RequestConverter<HttpRequest> {

    @Override
    public HttpRequest getRequest() {
        return reference.get();
    }

    @Override
    public HttpRequest convert(InvokeRequest invokeRequest) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("mock请求的信息：{}", invokeRequest);
        }

        HttpRequest httpRequest = new HttpRequest();
        PreCheckUtils.checkEmpty(invokeRequest, "mock的请求不能为空");
        BaseMockRequest baseMockRequest = (BaseMockRequest) invokeRequest;
        MockMethod mockMethod = baseMockRequest.getMockMethod();
        if (mockMethod == null) {
            throw new KipaProcessException("mock请求的方式不能为空");
        }
        if (mockMethod == MockMethod.PARAMETER) {
            Map<String, String> paramMap = baseMockRequest.getRequestParams();
            if (MapUtils.isNotEmpty(paramMap)) {
                paramMap.forEach(httpRequest::withQueryStringParameter);
            }

        }else if (mockMethod == MockMethod.BODY) {
            String body = baseMockRequest.getBody();
            Charset charset = baseMockRequest.getCharset();
            httpRequest.withBody(body, charset);
        }
        Map<String, String> cookies = baseMockRequest.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            cookies.forEach(httpRequest::withCookie);
        }

        Map<String, String> headers = baseMockRequest.getHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(httpRequest::withHeader);
        }
        String path = baseMockRequest.getPath();
        httpRequest.withPath(path);
        httpRequest.withSecure(baseMockRequest.isSecure());
        httpRequest.withMethod(baseMockRequest.getHttpSendMethod().getName());
        reference.set(httpRequest);
        return httpRequest;
    }

}
