package com.kipa.http.core;

import com.kipa.http.emuns.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Callback;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {

    /**
     * http url地址
     */
    private String url;
    /**
     * http调用方法
     */
    private String method;
    /**
     * http消息头
     */
    private Map<String, String> headerMap;
    /**
     * http消息体
     */
    private Map<String, String> bodyMap;
    /**
     * http消息体，json格式的
     */
    private String json;
    /**
     * 文件上传
     */
    private Map<String, String> fileMap;
    /**
     * http请求类型：PARAMETER、 JSON、FILE
     */
    private RequestType type;

    /**
     * 异步回调接口
     */
    private Callback callback;
}
