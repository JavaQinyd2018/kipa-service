package com.kipa.http.excute;

import com.kipa.core.AsyncInvoker;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class HttpAsyncInvoker  implements AsyncInvoker<OkHttpClient, Request, Callback> {
    @Override
    public void invoke(OkHttpClient client, Request request, Callback callBack) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("当前调用第三方的api的请求为：{}",request);
        }
        long start = System.currentTimeMillis();
        Call call = client.newCall(request);
        call.enqueue(callBack);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("当前异步执行的时长为：{}ms",end - start);
        }
    }
}
