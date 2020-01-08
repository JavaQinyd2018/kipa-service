package com.kipa.mock.http.service.execute;

import com.kipa.core.InvokeRequest;
import com.kipa.core.RequestConverter;
import com.kipa.mock.http.entity.BaseMockForward;
import com.kipa.utils.PreCheckUtils;
import org.mockserver.model.HttpForward;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:43
 */
public class MockForwardConverter extends RequestConverter<HttpForward> {

    @Override
    public HttpForward getRequest() {
        return reference.get();
    }

    @Override
    public HttpForward convert(InvokeRequest invokeRequest) throws Exception {
        PreCheckUtils.checkEmpty(invokeRequest, "mock的forward响应不能为空");
        BaseMockForward baseMockForward = (BaseMockForward) invokeRequest;
        HttpForward httpForward = HttpForward.forward();
        httpForward.withHost(baseMockForward.getHost());
        httpForward.withPort(baseMockForward.getPort());
        httpForward.withScheme(HttpForward.Scheme.valueOf(baseMockForward.getScheme()));
        TimeUnit timeUnit = baseMockForward.getDelayTimeUnit();
        Long delay = baseMockForward.getDelay();
        if (delay != null) {
            httpForward.withDelay(timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit, delay);
        }
        reference.set(httpForward);
        return httpForward;
    }
}
