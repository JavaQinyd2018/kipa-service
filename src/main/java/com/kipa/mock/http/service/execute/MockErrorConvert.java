package com.kipa.mock.http.service.execute;

import com.kipa.common.KipaProcessException;
import com.kipa.mock.http.entity.BaseMockError;
import org.mockserver.model.HttpError;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:44
 */
@Service("mockErrorConvert")
public class MockErrorConvert implements MockConvert<BaseMockError, HttpError> {
    @Override
    public HttpError convert(BaseMockError baseMockError) {
        if (baseMockError == null) {
            throw new KipaProcessException("mock的error响应不能为空");
        }
        HttpError httpError = HttpError.error();
        httpError.withDropConnection(baseMockError.isDropConnection());
        httpError.withResponseBytes(baseMockError.getResponseMessage().getBytes());
        TimeUnit timeUnit = baseMockError.getDelayTimeUnit();
        Long delay = baseMockError.getDelay();
        if (delay != null) {
            httpError.withDelay(timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit, delay);
        }
        return httpError;
    }
}
