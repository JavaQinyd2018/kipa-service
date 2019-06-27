package com.kipa.http.service.execute;

import com.kipa.http.exception.HttpProcessException;
import okhttp3.*;
import org.springframework.stereotype.Service;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http同步执行请求
 */
@Service
public class HttpSyncExecutor implements HttpExecutor {

    @Override
    public Response execute(OkHttpClient client, Request request, Callback callback) {
        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (Exception e) {
            throw new HttpProcessException("http调用失败",e);
        }
    }
}
