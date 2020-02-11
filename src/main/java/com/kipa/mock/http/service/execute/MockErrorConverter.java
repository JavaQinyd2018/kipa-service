package com.kipa.mock.http.service.execute;

import com.kipa.core.InvokeRequest;
import com.kipa.core.RequestConverter;
import com.kipa.mock.http.entity.BaseMockError;
import com.kipa.utils.PreCheckUtils;
import org.mockserver.model.HttpError;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:44
 */
public class MockErrorConverter extends RequestConverter<HttpError> {
    @Override
    public HttpError getRequest() {
        return reference.get();
    }

    @Override
    public HttpError convert(InvokeRequest invokeRequest) throws Exception {
        PreCheckUtils.checkEmpty(invokeRequest,"mock的error响应不能为空");
        HttpError httpError = HttpError.error();
        BaseMockError baseMockError = (BaseMockError) invokeRequest;
        httpError.withDropConnection(baseMockError.isDropConnection());
        httpError.withResponseBytes(baseMockError.getResponseMessage().getBytes());
        TimeUnit timeUnit = baseMockError.getDelayTimeUnit();
        Long delay = baseMockError.getDelay();
        if (delay != null) {
            httpError.withDelay(timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit, delay);
        }
        reference.set(httpError);
        return httpError;
    }
}
