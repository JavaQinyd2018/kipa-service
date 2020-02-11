package com.kipa.http.service.impl;

import com.kipa.common.KipaProcessException;
import com.kipa.http.excute.HttpResponse;
import com.kipa.http.excute.HttpResponseConvert;
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
        throw new KipaProcessException("http异步回调异常，错误原因是", e);
    }

    @Override
    default void onResponse(Call call, Response response) throws IOException {
        HttpResponseConvert httpResponseConvert = new HttpResponseConvert();
        HttpResponse httpResponse = null;
        try {
            httpResponse = (HttpResponse) httpResponseConvert.convert(response);
        } catch (Exception e) {
            throw new KipaProcessException("http转化异常，错误原因是", e);
        }
        callResponse(call, httpResponse);
    }

    void callResponse(Call call, HttpResponse httpResponse);
}
