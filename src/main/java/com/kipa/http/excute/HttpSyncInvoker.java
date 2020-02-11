package com.kipa.http.excute;

import com.kipa.core.SyncInvoker;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class HttpSyncInvoker implements SyncInvoker<OkHttpClient, Request, Response> {

    @Override
    public Response invoke(OkHttpClient client, Request request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("当前调用第三方的api的请求为：{}",request);
        }
        long start = System.currentTimeMillis();
        Call call = client.newCall(request);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("当前http执行的时长为：{}ms",end - start);
        }
        return call.execute();
    }
}
