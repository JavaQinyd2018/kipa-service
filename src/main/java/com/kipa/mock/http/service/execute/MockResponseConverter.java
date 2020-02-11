package com.kipa.mock.http.service.execute;

import com.kipa.core.InvokeRequest;
import com.kipa.core.RequestConverter;
import com.kipa.mock.http.entity.BaseMockResponse;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.mockserver.model.HttpResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 16:47
 */
@Slf4j
public class MockResponseConverter extends RequestConverter<HttpResponse> {

    @Override
    public HttpResponse getRequest() {
        return reference.get();
    }

    @Override
    public HttpResponse convert(InvokeRequest invokeRequest) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("需要mock的结果是：{}",invokeRequest);
        }
        HttpResponse httpResponse = new HttpResponse();
        PreCheckUtils.checkEmpty(invokeRequest, "mock的结果不能为空");
        BaseMockResponse response = (BaseMockResponse) invokeRequest;
        httpResponse.withBody(response.getBody());
        Map<String, String> cookies = response.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            cookies.forEach(httpResponse::withCookie);
        }

        Map<String, String> headers = response.getHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(httpResponse::withHeader);
        }
        httpResponse.withReasonPhrase(response.getReasonPhrase());
        httpResponse.withStatusCode(response.getStatusCode());
        TimeUnit timeUnit = response.getDelayTimeUnit();
        Long delay = response.getDelay();
        if (delay != null) {
            httpResponse.withDelay(timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit, delay);
        }
        reference.set(httpResponse);
        return httpResponse;
    }
}
