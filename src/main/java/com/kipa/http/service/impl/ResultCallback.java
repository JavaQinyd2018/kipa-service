package com.kipa.http.service.impl;

import com.kipa.http.core.HttpResponse;
import com.kipa.http.exception.HttpProcessException;
import com.kipa.http.service.convert.ResponseConvert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author: Qinyadong
 * @date: 2019/5/16 10:36
 */
public interface ResultCallback extends Callback {
    @Override
    default void onFailure(Call call, IOException e) {
        throw new HttpProcessException("http异步回调异常，错误原因是", e);
    }

    @Override
    default void onResponse(Call call, Response response) throws IOException {
        ResponseConvert responseConvert = new ResponseConvert();
        HttpResponse httpResponse = responseConvert.convert(response);
        callResponse(call, httpResponse);
    }

    void callResponse(Call call, HttpResponse httpResponse);
}
