package com.kipa.mock.service.execute;

import com.kipa.http.service.convert.Convert;
import com.kipa.mock.entity.BaseMockForward;
import org.mockserver.model.HttpForward;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:43
 */
@Service("mockForwardConvert")
public class MockForwardConvert implements Convert<BaseMockForward, HttpForward> {
    @Override
    public HttpForward convert(BaseMockForward baseMockForward) {
        if (baseMockForward == null) {
            throw new RuntimeException("mock的forward响应不能为空");
        }
        HttpForward httpForward = HttpForward.forward();
        httpForward.withHost(baseMockForward.getHost());
        httpForward.withPort(baseMockForward.getPort());
        httpForward.withScheme(HttpForward.Scheme.valueOf(baseMockForward.getScheme()));
        TimeUnit timeUnit = baseMockForward.getDelayTimeUnit();
        Long delay = baseMockForward.getDelay();
        if (delay != null) {
            httpForward.withDelay(timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit, delay);
        }
        return httpForward;
    }
}
