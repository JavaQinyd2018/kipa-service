package com.kipa.http.excute;

import com.kipa.core.InvokeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse implements InvokeResponse {

    /**
     * http响应消息头
     */
    private Map<String, List<String>> headerMap;
    /**
     * http响应消息体
     */
    private Map<String, Object> bodyMap;
    /**
     * http 消息体是否为json格式
     */
    private boolean jsonFormat;
    /**
     * 响应结果是否成功
     */
    private boolean success;
    /**
     * 响应失败的失败信息
     */
    private String message;
    /**
     * http错误码
     */
    private int code;

    /**
     * 响应的请求信息
     */
    private Map<String, Object> requestInfo;
}

