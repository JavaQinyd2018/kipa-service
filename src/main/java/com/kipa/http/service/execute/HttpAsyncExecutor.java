package com.kipa.http.service.execute;
import lombok.Setter;
import okhttp3.*;
import org.springframework.util.Assert;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http异步执行请求
 */
public class HttpAsyncExecutor extends AbstractHttpExecutor {
    @Setter
    private Callback callback;

    @Override
    Response invoke(Call call) throws Exception {
        Assert.notNull(callback, "callback接口不能为空");
        call.enqueue(callback);
        return null;
    }
}
