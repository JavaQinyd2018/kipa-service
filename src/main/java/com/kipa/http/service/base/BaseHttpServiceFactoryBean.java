package com.kipa.http.service.base;

import com.kipa.core.BaseExecutor;
import com.kipa.core.InvokeResponse;
import com.kipa.http.annotation.ServiceType;
import com.kipa.http.excute.HttpRequest;
import com.kipa.http.excute.OkHttpClientFactory;
import com.kipa.http.excute.OkHttpClientProperties;
import com.kipa.http.emuns.InvokeType;
import com.kipa.http.emuns.RequestType;
import com.kipa.http.excute.HttpRequestConvert;
import com.kipa.http.excute.HttpResponseConvert;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.excute.HttpAsyncInvoker;
import com.kipa.http.excute.HttpSyncInvoker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * BaseHttpService的代理对象
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@SuppressWarnings("all")
@Service("baseHttpService")
public class BaseHttpServiceFactoryBean implements FactoryBean<BaseHttpService>, InitializingBean {

    @Autowired
    private OkHttpClientProperties okHttpClientProperties;

    /**
     * 同步执行器
     */
    private BaseExecutor<OkHttpClient, Request, Response> syncExecutor;

    /**
     * 异步执行器
     */
    private BaseExecutor<OkHttpClient, Request, Response> asyncExecutor;

    private HttpResponseConvert httpResponseConvert;
    @Override
    public void afterPropertiesSet() throws Exception {
        //所有初始化的操作
        final HttpRequestConvert httpRequestConvert = new HttpRequestConvert();
        httpResponseConvert = new HttpResponseConvert();
        final OkHttpClientFactory okHttpClientFactory = new OkHttpClientFactory();
        final OkHttpClient okHttpClient = okHttpClientFactory.create(okHttpClientProperties);
        final HttpSyncInvoker httpSyncInvoker = new HttpSyncInvoker();
        final HttpAsyncInvoker httpAsyncInvoker = new HttpAsyncInvoker();
        syncExecutor = new BaseExecutor<>(okHttpClient, httpSyncInvoker, httpRequestConvert, httpResponseConvert);
        asyncExecutor = new BaseExecutor<>(okHttpClient, httpAsyncInvoker, httpRequestConvert, httpResponseConvert);
    }

    @Override
    public BaseHttpService getObject() throws Exception {
        return (BaseHttpService) Proxy.newProxyInstance(BaseHttpService.class.getClassLoader(), new Class[]{BaseHttpService.class}, new HttpServiceHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return BaseHttpService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    class HttpServiceHandler implements InvocationHandler{

        private AtomicBoolean flag = new AtomicBoolean(false);
        private AtomicReference<String> localDir = new AtomicReference<>();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            ServiceType annotation = method.getAnnotation(ServiceType.class);
            OkHttpClient okHttpClient = (OkHttpClient) args[0];
            HttpSendMethod httpSendMethod = (HttpSendMethod) args[1];
            HttpRequest httpRequest = (HttpRequest) args[2];
            InvokeType invokeType = null;
            RequestType requestType = annotation.requestType();
            switch (requestType) {
                case PARAMETER:
                    httpRequest.setType(requestType);
                    invokeType = (InvokeType) args[3];
                    break;
                case JSON:
                    String json = (String) args[3];
                    invokeType = (InvokeType) args[4];
                    httpRequest.setJson(json);
                    httpRequest.setType(requestType);
                    break;
                case FILE:
                    Map<String, String> fileMap = (Map<String, String>) args[3];
                    invokeType = (InvokeType) args[4];
                    httpRequest.setFileMap(fileMap);
                    httpRequest.setType(requestType);
                    String way = fileMap.get("method");
                    if (StringUtils.equalsIgnoreCase(way, HttpSendMethod.DOWNLOAD.getName())) {
                        flag.set(true);
                        localDir.set(fileMap.get("localTargetDir"));
                    }
                    break;
            }

            if (invokeType == InvokeType.SYNC) {
                InvokeResponse response = syncExecutor.execute(httpRequest);
                //如果是下载文件的话，转化为下载文件的结果，其他的get、post、put、delete等操作返回对应的结果
                return flag.get() ? httpResponseConvert.convertDownloadFile(httpResponseConvert.getResponse(), localDir.get())
                        : response;
            }else if (invokeType == InvokeType.ASYNC) {
                return asyncExecutor.execute(httpRequest);
            }
            return null;
        }
    }
}
