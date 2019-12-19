package com.kipa.http.service.execute;

import com.kipa.common.core.Executor;
import okhttp3.*;

public abstract class AbstractHttpExecutor implements Executor<OkHttpClient, Request, Response> {
    @Override
    public Response execute(OkHttpClient client, Request request) throws Exception {
        Call call = client.newCall(request);
        return invoke(call);
    }

    abstract Response invoke(Call call) throws Exception;
}
