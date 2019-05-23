package com.kipa.http.service.impl;

import com.kipa.http.core.HttpRequest;
import com.kipa.http.core.HttpResponse;
import com.kipa.http.emuns.HttpSendMethod;
import com.kipa.http.service.HttpInvokeService;
import com.kipa.utils.HttpUtils;
import com.kipa.utils.PreCheckUtils;
import okhttp3.Callback;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
public abstract class AbstractInvokeServiceImpl implements HttpInvokeService {

    @Override
    public HttpResponse get(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        String getUrl = HttpUtils.buildGetUrl(url, paramMap);
        HttpRequest httpRequest = assembleRequest(getUrl, null,headerMap,HttpSendMethod.GET);
        return invokeParameter(httpRequest);
    }

    @Override
    public HttpResponse post(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        PreCheckUtils.checkEmpty(paramMap, "POST请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap,HttpSendMethod.POST);
        return invokeParameter(httpRequest);
    }

    @Override
    public HttpResponse post(String url, Map<String, String> headerMap, String json) {
        PreCheckUtils.checkEmpty(json, "POST请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.POST);
        return invokeJson(httpRequest,json);
    }

    @Override
    public HttpResponse file(String url, Map<String, String> headerMap, Map<String, String> fileMap) {
        PreCheckUtils.checkEmpty(fileMap, "请求的文件信息不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.POST);
        return invokeFile(httpRequest, fileMap);
    }

    @Override
    public HttpResponse put(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        PreCheckUtils.checkEmpty(paramMap, "put请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap, HttpSendMethod.PUT);
        return invokeParameter(httpRequest);
    }

    @Override
    public HttpResponse put(String url, Map<String, String> headerMap, String json) {
        PreCheckUtils.checkEmpty(json, "put请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.PUT);
        return invokeJson(httpRequest,json);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        PreCheckUtils.checkEmpty(paramMap, "put请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap, HttpSendMethod.DELETE);
        return invokeParameter(httpRequest);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> headerMap, String json) {
        PreCheckUtils.checkEmpty(json, "delete请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.DELETE);
        return invokeJson(httpRequest,json);
    }

    public abstract HttpResponse invokeParameter(HttpRequest httpRequest);

    /**
     * 实际调用方Post格式json
     * @param httpRequest
     * @param json
     * @return
     */
    public abstract HttpResponse invokeJson(HttpRequest httpRequest, String json);

    /**
     * 文件的相关功能
     * @param httpRequest
     * @return
     */
    public abstract  HttpResponse invokeFile(HttpRequest httpRequest, Map<String, String> fileMap);

    /**
     * 组装http请求
     * @param url 请求的url地址
     * @param paramMap 请求的参数集合
     * @param headerMap 请求的消息头
     * @return http请求
     */
    private HttpRequest assembleRequest(String url, Map<String, String> paramMap, Map<String, String> headerMap, HttpSendMethod method) {
        PreCheckUtils.checkEmpty(url, "请求的URL不能为空");
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.setBodyMap(paramMap);
        httpRequest.setHeaderMap(headerMap);
        httpRequest.setMethod(method.getName());
        return httpRequest;
    }

    @Override
    public void asyncGet(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback) {
        String getUrl = HttpUtils.buildGetUrl(url, paramMap);
        HttpRequest httpRequest = assembleRequest(getUrl, null,headerMap,HttpSendMethod.GET);
        invokeAsyncParameter(httpRequest, callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback) {
        PreCheckUtils.checkEmpty(paramMap, "POST请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap,HttpSendMethod.POST);
        invokeAsyncParameter(httpRequest,callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> headerMap, String json, Callback callback) {
        PreCheckUtils.checkEmpty(json, "POST请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.POST);
        invokeAsyncJson(httpRequest,json, callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback) {
        PreCheckUtils.checkEmpty(paramMap, "POST请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap,HttpSendMethod.PUT);
        invokeAsyncParameter(httpRequest,callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> headerMap, String json, Callback callback) {
        PreCheckUtils.checkEmpty(json, "POST请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.PUT);
        invokeAsyncJson(httpRequest,json, callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback) {
        PreCheckUtils.checkEmpty(paramMap, "POST请求参数集合不能空");
        HttpRequest httpRequest = assembleRequest(url, paramMap, headerMap,HttpSendMethod.DELETE);
        invokeAsyncParameter(httpRequest,callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> headerMap, String json, Callback callback) {
        PreCheckUtils.checkEmpty(json, "POST请求的json报文不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.DELETE);
        invokeAsyncJson(httpRequest,json, callback);
    }

    @Override
    public void asyncFile(String url, Map<String, String> headerMap, Map<String, String> fileMap, Callback callback) {
        PreCheckUtils.checkEmpty(fileMap, "请求的文件信息不能为空");
        HttpRequest httpRequest = assembleRequest(url, null, headerMap, HttpSendMethod.POST);
        invokeAsyncFile(httpRequest, fileMap, callback);
    }

    /**
     * 异步参数请求
     * @param httpRequest
     * @param callback
     */
    public abstract void invokeAsyncParameter(HttpRequest httpRequest, Callback callback);

    /**
     * 异步json请求
     * @param httpRequest
     * @param json
     * @param callback
     */
    public abstract void invokeAsyncJson(HttpRequest httpRequest, String json, Callback callback);

    /**
     * 异步文件请求
     * @param httpRequest
     * @param fileMap
     * @param callback
     */
    public abstract void invokeAsyncFile(HttpRequest httpRequest, Map<String, String> fileMap, Callback callback);
}
