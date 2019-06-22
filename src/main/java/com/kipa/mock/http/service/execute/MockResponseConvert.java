package com.kipa.mock.http.service.execute;

import com.kipa.mock.http.entity.BaseMockResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 16:47
 */
@Service("mockResponseConvert")
@Slf4j
public class MockResponseConvert implements MockConvert<BaseMockResponse, HttpResponse> {

    @Override
    public HttpResponse convert(BaseMockResponse response) {
        HttpResponse httpResponse = new HttpResponse();
        log.debug("需要mock的结果是：{}",response);
        if (response == null) {
            throw new RuntimeException("mock的结果不能为空");
        }
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
        return httpResponse;
    }
}
