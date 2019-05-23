package com.kipa.http.service.execute;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
public interface HttpExecutor {

    Response execute(OkHttpClient client, Request request, Callback callback);
}
