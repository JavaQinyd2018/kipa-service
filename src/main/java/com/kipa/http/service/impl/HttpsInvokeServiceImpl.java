package com.kipa.http.service.impl;

import com.kipa.http.core.HttpRequest;
import com.kipa.http.core.HttpResponse;
import com.kipa.http.core.OkHttpClientProperties;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.emuns.InvokeType;
import com.kipa.http.service.HttpInvokeService;
import com.kipa.http.service.base.BaseHttpService;
import com.kipa.http.service.base.OkHttpClientGenerator;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
@Service("httpsInvokeService")
public class HttpsInvokeServiceImpl extends AbstractInvokeServiceImpl implements HttpInvokeService, InitializingBean {
    private OkHttpClient okHttpClient;

    @Autowired
    private BaseHttpService baseHttpService;

    @Autowired
    private OkHttpClientGenerator okHttpClientGenerator;

    @Autowired
    private OkHttpClientProperties okHttpClientProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        okHttpClient = okHttpClientGenerator.build(okHttpClientProperties);
    }

    @Override
    public HttpResponse invokeParameter(HttpRequest httpRequest) {
        return baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, InvokeType.SYNC);
    }

    @Override
    public HttpResponse invokeJson(HttpRequest httpRequest, String json) {
        return baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, json, InvokeType.SYNC);
    }

    @Override
    public HttpResponse invokeFile(HttpRequest httpRequest, Map<String, String> fileMap) {
        return baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, fileMap, InvokeType.SYNC);
    }

    @Override
    public void invokeAsyncParameter(HttpRequest httpRequest, Callback callback) {
        httpRequest.setCallback(callback);
        baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, InvokeType.ASYNC);
    }

    @Override
    public void invokeAsyncJson(HttpRequest httpRequest, String json, Callback callback) {
        httpRequest.setCallback(callback);
        baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, json, InvokeType.ASYNC);
    }

    @Override
    public void invokeAsyncFile(HttpRequest httpRequest, Map<String, String> fileMap, Callback callback) {
        httpRequest.setCallback(callback);
        baseHttpService.send(okHttpClient, HttpSendMethod.valueOf(httpRequest.getMethod()), httpRequest, fileMap, InvokeType.ASYNC);
    }
}
