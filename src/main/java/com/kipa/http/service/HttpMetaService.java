package com.kipa.http.service;

import com.kipa.http.service.impl.ResultCallback;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/31
 */
public interface HttpMetaService {

    /**
     * get请求
     * @param url 请求的url
     * @return 结果信息json。默认只获取部分接口响应的字符串
     */
    String get(String url);

    /**
     * get请求
     * @param url 请求url
     * @param receiveAllInfo 是否要获取全部信息：是-全部信息（响应消息头， 响应消息体），否仅仅获得服务端返回的业务数据
     * @return 结果信息
     */
    String get(String url, boolean receiveAllInfo);

    /**
     * get请求
     * @param url 请求url
     * @param headerMap http header 集合
     * @param receiveAllInfo 是否要获取全部信息：是-全部信息（响应消息头， 响应消息体），否仅仅获得服务端返回的业务数据
     * @return 结果信息
     */
    String get(String url, Map<String, String> headerMap, boolean receiveAllInfo);

    /**
     * get请求
     * @param url 请求url
     * @param headerMap http header 集合
     * @param paramMap 请求的参数集合
     * @param receiveAllInfo 是否要获取全部信息：是-全部信息（响应消息头， 响应消息体），否仅仅获得服务端返回的业务数据
     * @return 响应的map集合
     */
    String get(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     * get请求
     * @param url 请求url
     * @param headerMap http header 集合
     * @param paramMap 请求的参数集合
     * @param clazz 将服务的业务结果json转化成clazz类型对象
     * @param <T> 泛型
     * @return 结果对象
     */
    <T> List<T> get(String url, Map<String, String> headerMap, Map<String, String> paramMap, Class<T> clazz);

    /**
     * post请求
     * @param url 请求url
     * @param paramMap 请求的参数集合
     * @return json结果
     */
    String post(String url, Map<String, String> paramMap);
    /**
     * post请求
     * @param url 请求url
     * @param paramMap 请求的参数集合
     * @param receiveAllInfo 是否要获取全部信息：是-全部信息（响应消息头， 响应消息体），否仅仅获得服务端返回的业务数据
     * @return 结果
     */
    String post(String url, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     * post请求
     * @param url
     * @param headerMap
     * @param paramMap
     * @param receiveAllInfo
     * @return
     */
    String post(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     * post请求
     * @param url
     * @param headerMap
     * @param paramMap
     * @param <T>
     * @return
     */
    <T> List<T> post(String url, Map<String, String> headerMap, Map<String, String> paramMap, Class<T> clazz);

    /**
     *
     * @param url
     * @param headerMap
     * @param json
     * @param receiveAllInfo
     * @return
     */
    String post(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo);

    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    String post(String url, String json);
    /**
     *post请求
     * @param url
     * @param json 请求体是json格式
     * @param receiveAllInfo
     * @return
     */
    String post(String url, String json, boolean receiveAllInfo);

    /**
     * put请求
     * @param url
     * @param paramMap
     * @return
     */
    String put(String url, Map<String, String> paramMap);
    /**
     *
     * @param url
     * @param paramMap
     * @param receiveAllInfo
     * @return
     */
    String put(String url, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     *
     * @param url
     * @param headerMap
     * @param paramMap
     * @param receiveAllInfo
     * @return
     */
    String put(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     *
     * @param url
     * @param headerMap
     * @param paramMap
     * @param <T>
     * @return
     */
    <T> List<T> put(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz);

    /**
     *
     * @param url
     * @param headerMap
     * @param json
     * @param receiveAllInfo
     * @return
     */
    String put(String url, Map<String, String> headerMap, String json, boolean receiveAllInfo);

    /**
     * put
     * @param url
     * @param json
     * @return
     */
    String put(String url, String json);
    /**
     *put请求
     * @param url
     * @param json
     * @param receiveAllInfo
     * @return
     */
    String put(String url, String json, boolean receiveAllInfo);

    /**
     * 删除
     * @param url
     * @param paramMap
     * @return
     */
    String delete(String url, Map<String, String> paramMap);

    /**
     * delete请求
     * @param url
     * @param paramMap
     * @param receiveAllInfo
     * @return
     */
    String delete(String url, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     * delete请求
     * @param url
     * @param headerMap
     * @param paramMap
     * @param receiveAllInfo
     * @return
     */
    String delete(String url, Map<String, String> headerMap, Map<String, String> paramMap, boolean receiveAllInfo);

    /**
     *
     * @param url
     * @param headerMap
     * @param paramMap
     * @param <T>
     * @return
     */
    <T> List<T> delete(String url, Map<String, String> headerMap, Map<String, String> paramMap,Class<T> clazz);

    /**
     * 上传文件
     * @param url 请求的url地址
     * @param headerMap 消息头
     * @param fileMap 必须上传filePath：本地文件路径， mediaType文件类型， fileParamJson 带参数的文件上传信息
     * @param receiveAllInfo
     * @return
     */
    Map<String, String> upload(String url, Map<String, String> headerMap, Map<String, String> fileMap, boolean receiveAllInfo);

    /**
     * 上传文件
     * @param url
     * @param fileMap
     * @return
     */
    Map<String, String> upload(String url,  Map<String, String> fileMap);
    /**
     * 上传文件
     * @param url
     * @param fileMap
     * @param receiveAllInfo
     * @return
     */
    Map<String, String> upload(String url,  Map<String, String> fileMap, boolean receiveAllInfo);

    /**
     * 下载文件
     * @param url
     * @param headerMap
     * @param localTargetDir
     * @param receiveAllInfo
     * @return
     */
    Map<String, String> download(String url, Map<String, String> headerMap, String localTargetDir, boolean receiveAllInfo);

    /**
     * 下载文件
     * @param url
     * @param localTargetDir
     * @param receiveAllInfo
     * @return
     */
    Map<String, String> download(String url, String localTargetDir, boolean receiveAllInfo);

    /**
     * 下载文件
     * @param url
     * @param localTargetDir
     * @return
     */
    Map<String, String> download(String url, String localTargetDir);

    /**
     * 异步get
     * @param url
     * @param paramMap
     * @param headerMap
     * @param callback
     */
    void asyncGet(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback);

    /**
     * 异步post
     * @param url
     * @param paramMap
     * @param headerMap
     * @param callback
     */
    void asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback);

    void asyncPost(String url,  Map<String, String> headerMap,String json, ResultCallback callback);

    void asyncPut(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback);

    /**
     * 异步put
     * @param url
     * @param headerMap
     * @param json
     * @param callback
     */
    void asyncPut(String url,  Map<String, String> headerMap,String json, ResultCallback callback);

    void asyncDelete(String url, Map<String, String> paramMap, Map<String, String> headerMap, ResultCallback callback);

    void asyncDelete(String url,  Map<String, String> headerMap,String json, ResultCallback callback);

    /**
     * 异步文件上传
     * @param url
     * @param headerMap
     * @param fileMap
     * @param callback 回调函数
     */
    void asyncUpload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback);

    /**
     * 异步文件下载
     * @param url
     * @param headerMap
     * @param fileMap
     * @param callback 回调函数
     */
    void asyncDownload(String url, Map<String, String> headerMap, Map<String, String> fileMap, ResultCallback callback);
}
