package com.kipa.http.service.execute;

import com.kipa.common.core.Executor;
import okhttp3.*;
import org.springframework.stereotype.Service;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http同步执行请求
 */
public class HttpSyncExecutor implements Executor<OkHttpClient, Request, Response> {
    @Override
    public Response execute(OkHttpClient client, Request request) throws Exception {
        return client.newCall(request).execute();
    }
}
