package com.kipa.http.service;
import com.kipa.http.excute.HttpResponse;
import okhttp3.Callback;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
public interface HttpInvokeService {


    HttpResponse get(String url, Map<String, String> paramMap, Map<String, String> headerMap);

    HttpResponse post(String url, Map<String, String> paramMap, Map<String, String> headerMap);

    HttpResponse post(String url,  Map<String, String> headerMap,String json);

    HttpResponse put(String url, Map<String, String> paramMap, Map<String, String> headerMap);

    HttpResponse put(String url,  Map<String, String> headerMap,String json);

    HttpResponse delete(String url, Map<String, String> paramMap, Map<String, String> headerMap);

    HttpResponse delete(String url,  Map<String, String> headerMap,String json);

    HttpResponse file(String url, Map<String, String> headerMap, Map<String, String> fileMap);

    void asyncGet(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback);

    void asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback);

    void asyncPost(String url,  Map<String, String> headerMap,String json, Callback callback);

    void asyncPut(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback);

    void asyncPut(String url,  Map<String, String> headerMap,String json, Callback callback);

    void asyncDelete(String url, Map<String, String> paramMap, Map<String, String> headerMap, Callback callback);

    void asyncDelete(String url,  Map<String, String> headerMap,String json, Callback callback);

    void asyncFile(String url, Map<String, String> headerMap, Map<String, String> fileMap, Callback callback);
}
