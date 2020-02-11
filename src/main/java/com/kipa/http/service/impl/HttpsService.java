package com.kipa.http.service.impl;

import com.google.common.collect.Maps;
import com.kipa.http.excute.HttpResponse;
import com.kipa.http.service.HttpMetaService;
import com.kipa.utils.PreCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
@Service("httpsService")
public class HttpsService extends AbstractHttpServiceImpl implements HttpMetaService {

    private static final String VALID_MESSAGE = "回调函数接口不能为空";

    @Autowired
    private HttpsInvokeServiceImpl httpsInvokeService;

    @Override
    public String get(String url, boolean receiveAllInfo) {
        return get(url, null, receiveAllInfo);
    }

    @Override
    public String get(String url, Map<String, String> headerMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.get(url, null, headerMap);
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
    public String get(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.get(url, paramMap, headerMap);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> get(String url, Map<String, String> headerMap, Map<String, String> paramMap, Class<T> clazz) {
        HttpResponse httpResponse = httpsInvokeService.get(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public String post(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.post(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String post(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.post(url, paramMap, headerMap);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> post(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpsInvokeService.post(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public String post(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.post(url, headerMap, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String post(String url, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.post(url, null, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String put(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.put(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String put(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.put(url, paramMap, headerMap);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> put(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpsInvokeService.put(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    @Override
    public String put(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.put(url, headerMap, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String put(String url, String json, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.put(url, null, json);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String delete(String url, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.delete(url, paramMap, new HashMap<>());
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public String delete(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo) {
        HttpResponse httpResponse = httpsInvokeService.delete(url, paramMap, headerMap);
        return parseHttpResponseToString(httpResponse, receiveAllInfo);
    }

    @Override
    public <T> List<T> delete(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz) {
        HttpResponse httpResponse = httpsInvokeService.delete(url, paramMap, headerMap);
        return parseHttpResponseToInstance(httpResponse, clazz);
    }

    /**
     * 上传
     * @param url 请求的url地址
     * @param headerMap 消息头
     * @param fileMap 必须上传filePath：本地文件路径， mediaType文件类型， fileParamJson 带参数的文件上传信息
     * @param receiveAllInfo
     * @return
     */
    @Override
    public Map<String, String> upload(String url, Map<String, String> headerMap, Map<String, String> fileMap, boolean receiveAllInfo) {
        String filePath = fileMap.get("filePath");
        String mediaType = fileMap.get("mediaType");
        PreCheckUtils.checkEmpty(filePath, "上传的文件路径不能为空");
        PreCheckUtils.checkEmpty(mediaType, "上传的文件mediaType不能为空");
        HttpResponse httpResponse = httpsInvokeService.file(url, headerMap, fileMap);
        return parseFileResponseToMap(httpResponse, receiveAllInfo);
    }

    @Override
    public Map<String, String> upload(String url, Map<String, String> fileMap, boolean receiveAllInfo) {
        return upload(url, null, fileMap, receiveAllInfo);
    }

    @Override
    public Map<String, String> download(String url, Map<String, String> headerMap, String localDir, boolean receiveAllInfo) {
        PreCheckUtils.checkEmpty(localDir, "下载保存的本地目录不存在");
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("localTargetDir",localDir);
        HttpResponse httpResponse = httpsInvokeService.file(url, headerMap, fileMap);
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
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncGet(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncPost(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPost(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncPost(url, headerMap, json, callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncPut(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncPut(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncPut(url, headerMap, json, callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncDelete(url, paramMap, headerMap, callback);
    }

    @Override
    public void asyncDelete(String url, Map<String, String> headerMap, String json, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncDelete(url, headerMap, json, callback);
    }

    @Override
    public void asyncUpload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncFile(url, headerMap, fileMap, callback);
    }

    @Override
    public void asyncDownload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback) {
        PreCheckUtils.checkEmpty(callback, VALID_MESSAGE);
        httpsInvokeService.asyncFile(url, headerMap, fileMap, callback);
    }
}
