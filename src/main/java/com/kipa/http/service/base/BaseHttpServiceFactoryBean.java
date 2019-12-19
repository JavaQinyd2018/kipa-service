package com.kipa.http.service.base;

import com.kipa.http.annotation.ServiceType;
import com.kipa.http.core.HttpRequest;
import com.kipa.http.emuns.InvokeType;
import com.kipa.http.emuns.RequestType;
import com.kipa.http.service.convert.RequestConvert;
import com.kipa.http.service.convert.ResponseConvert;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.service.execute.HttpAsyncExecutor;
import com.kipa.http.service.execute.HttpSyncExecutor;
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

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@SuppressWarnings("all")
@Service("baseHttpService")
public class BaseHttpServiceFactoryBean implements FactoryBean<BaseHttpService>, InitializingBean {

    private HttpSyncExecutor httpSyncExecutor;
    private HttpAsyncExecutor httpAsyncExecutor;
    private RequestConvert requestConvert;
    private ResponseConvert responseConvert;

    @Override
    public void afterPropertiesSet() throws Exception {
        httpSyncExecutor = new HttpSyncExecutor();
        requestConvert = new RequestConvert();
        responseConvert = new ResponseConvert();
        httpAsyncExecutor = new HttpAsyncExecutor();
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
        private volatile String localDir = null;

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
                        localDir = fileMap.get("localTargetDir");
                    }
                    break;
            }

            Request request = requestConvert.convert(httpRequest);
            if (invokeType == InvokeType.SYNC) {
                Response response = httpSyncExecutor.execute(okHttpClient, request);
                //如果是下载文件的话，转化为下载文件的结果，其他的get、post、put、delete等操作返回对应的结果
                return flag.get() ? responseConvert.convertDownloadFile(response, localDir) : responseConvert.convert(response);
            }else if (invokeType == InvokeType.ASYNC) {
                httpAsyncExecutor.setCallback(httpRequest.getCallback());
                httpAsyncExecutor.execute(okHttpClient, request);
            }
            return null;
        }
    }
}
