package com.kipa.http.service.impl;

import com.kipa.http.core.HttpResponse;
import com.kipa.http.service.HttpMetaService;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
@Service("httpService")
@Slf4j
public class HttpService extends AbstractHttpServiceImpl implements HttpMetaService {

    private static final String MESSAGE = "回调函数接口不能为空";
    @Autowired
    private HttpInvokeServiceImpl httpInvokeService;

    @Override
    public String get(String url, boolean receiveAllInfo) {
        return get(url, null, receiveAllInfo);
    }

    @Override
    public String get(String url, Map<String, String> headerMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.get(url, null, headerMap);
       return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    /**
     * 策略：1.如果响应的信息成功 ： 是否需要获取全部信息->是：转化为map， 否，获取json 转化为map
     * 2. 如果响应失败，直接转化结果信息为map
     * @param url 请求url
     * @param headerMap http header 集合
     * @param paramMap 请求的参数集合
     * @param receiveAllInfo 是否要获取全部信息：是-全部信息（响应消息头， 响应消息体），否仅仅获得服务端返回的业务数据
     * @return
     */
    @Override
    public List<Map<String, Object>> get(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.get(url, paramMap, headerMap);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> get(String url, Map<String, String> headerMap, Map<String, String> paramMap, Class<T> clazz) {
        HttpResponse httpResponse = httpInvokeService.get(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public String post(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.post(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public List<Map<String, Object>> post(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.post(url, paramMap, headerMap);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> post(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpInvokeService.post(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public List<Map<String, Object>> post(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.post(url, headerMap, json);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public String post(String url, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.post(url, null, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String put(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.put(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public List<Map<String, Object>> put(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.put(url, paramMap, headerMap);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> put(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpInvokeService.put(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public List<Map<String, Object>> put(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.put(url, headerMap, json);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public String put(String url, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.put(url, null, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String delete(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.delete(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public List<Map<String, Object>> delete(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpInvokeService.delete(url, paramMap, headerMap);
        return parseHttpResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> delete(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpInvokeService.delete(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public Map<String, String> upload(String url, Map<String, String> headerMap, Map<String, String> fileMap, boolean receiveAllInfo) {
        PreCheckUtils.checkEmpty(fileMap, "文件信息集合为空");
        fileMap.put("method", "upload");
        String filePath = fileMap.get("filePath");
        String mediaType = fileMap.get("mediaType");
        PreCheckUtils.checkEmpty(filePath, "上传的文件路径不能为空");
        PreCheckUtils.checkEmpty(mediaType, "上传的文件mediaType不能为空");
        HttpResponse httpResponse = httpInvokeService.file(url, headerMap, fileMap);
        return parseFileResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public Map<String, String> upload(String url, Map<String, String> fileMap, boolean receiveAllInfo) {
        return upload(url, null, fileMap, receiveAllInfo);
    }

    @Override
    public Map<String, String> download(String url, Map<String, String> headerMap, String localTargetDir, boolean receiveAllInfo) {
        PreCheckUtils.checkEmpty(localTargetDir, "下载保存的本地目录不存在");
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("localTargetDir",localTargetDir);
        HttpResponse httpResponse = httpInvokeService.file(url, headerMap, fileMap);
        return parseFileResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public Map<String, String> download(String url, String localTargetDir, boolean receiveAllInfo) {
        return download(url, null, localTargetDir, receiveAllInfo);
    }

    @Override
    public String get(String url) {
        return get(url, false);
    }

    @Override
    public String post(String url, Map<String, String> paramMap) {
        return post(url, paramMap, false);
    }

    @Override
    public String post(String url, String json) {
        return post(url, json, false);
    }

    @Override
    public String put(String url, Map<String, String> paramMap) {
        return put(url, paramMap, false);
    }

    @Override
    public String put(String url, String json) {
        return put(url, json, false);
    }

    @Override
    public String delete(String url, Map<String, String> paramMap) {
        return delete(url, paramMap, false);
    }

    @Override
    public Map<String, String> upload(String url, Map<String, String> fileMap) {
        return upload(url, fileMap, false);
    }

    @Override
    public Map<String, String> download(String url, String localTargetDir) {
        return download(url, localTargetDir, false);
    }

    @Override
    public void asyncGet(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncGet(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncPost(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncPost(url, headerMap, json, callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncPut(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncPut(url, headerMap, json, callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncDelete(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncDelete(url, headerMap, json, callback);
    }

    @Override
    public void asyncUpload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncFile(url, headerMap, fileMap, callback);
    }

    @Override
    public void asyncDownload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, MESSAGE);
        httpInvokeService.asyncFile(url, headerMap, fileMap, callback);
    }
}
