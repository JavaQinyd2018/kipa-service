package com.kipa.http.excute;

import com.kipa.core.AsyncInvokeRequest;
import com.kipa.http.emuns.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.Callback;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest extends AsyncInvokeRequest<Callback> {

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

}
