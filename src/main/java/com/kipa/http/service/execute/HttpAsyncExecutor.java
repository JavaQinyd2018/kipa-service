package com.kipa.http.service.execute;

import okhttp3.*;
import org.springframework.stereotype.Component;


/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http异步执行请求
 */
@Component
public class HttpAsyncExecutor implements HttpExecutor {
    @Override
    public Response execute(OkHttpClient client, Request request, Callback callback) {

        Call call = client.newCall(request);
        call.enqueue(callback);
        return null;
    }
}
