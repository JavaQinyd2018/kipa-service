package com.kipa.http.service.base;

import com.kipa.http.annotation.ServiceType;
import com.kipa.http.excute.HttpRequest;
import com.kipa.http.excute.HttpResponse;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.emuns.InvokeType;
import com.kipa.http.emuns.RequestType;
import com.kipa.log.SystemLog;
import okhttp3.OkHttpClient;


import java.util.Map;


/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * http基础服务接口
 */
public interface BaseHttpService {

    /**
     * 通过Json的方式请求
     * @param method 发送请求的方式：get，post，。。。
     * @param request 发送请求的详情：header，body
     * @param json json报文
     * @return 响应的http结果：data，状态
     */
    @SystemLog("Json的方式请求")
    @ServiceType(requestType = RequestType.JSON)
    HttpResponse send(OkHttpClient okHttpClient, HttpSendMethod method, HttpRequest request, String json, InvokeType invokeType);

    /**
     * 通过参数的方式
     * @param method 发送请求的方式：get，post，。。。
     * @param request 发送请求的详情：header，body
     * @return 响应的http结果：data，状态
     */
    @SystemLog("参数的请求方式")
    @ServiceType(requestType = RequestType.PARAMETER)
    HttpResponse send(OkHttpClient okHttpClient, HttpSendMethod method,  HttpRequest request, InvokeType invokeType);

    /**
     * 通过文件的方式：上传文件/下载文件
     * @param method
     * @param request
     * @param fileMap
     * @return
     */
    @SystemLog("文件的请求方式")
    @ServiceType(requestType = RequestType.FILE)
    HttpResponse send(OkHttpClient okHttpClient, HttpSendMethod method, HttpRequest request, Map<String, String> fileMap, InvokeType invokeType);
}
