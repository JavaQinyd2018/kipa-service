package com.kipa.mock.http.service.execute;

import com.kipa.common.KipaProcessException;
import com.kipa.common.core.Convertor;
import com.kipa.mock.http.entity.BaseMockForward;
import org.mockserver.model.HttpForward;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:43
 */
public class MockForwardConvert implements Convertor<BaseMockForward, HttpForward> {
    @Override
    public HttpForward convert(BaseMockForward baseMockForward) {
        if (baseMockForward == null) {
            throw new KipaProcessException("mock的forward响应不能为空");
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
