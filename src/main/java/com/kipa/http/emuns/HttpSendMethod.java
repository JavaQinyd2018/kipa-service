package com.kipa.http.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: Qinyadong
 * @date: 2019/2/14 18:17
 */
@AllArgsConstructor
@Getter
@ToString
public enum HttpSendMethod {

    /**
     * 求获取Request-URI所标识的资源
     */
    GET(0, "GET"),

    /**
     * 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。
     * POST请求可能会导致新的资源的建立和/或已有资源的修改
     */
    POST(1, "POST"),

    /**
     * 向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。
     * 这一方法可以在不必传输整个响应内容的情况下，就可以获取包含在响应消息头中的元信息
     * 只获取响应信息报头
     */
    HEAD(2, "HEAD"),

    /**
     * 向指定资源位置上传其最新内容（全部更新，操作幂等）
     */
    PUT	(3, "PUT"),

    /**
     * 请求服务器删除Request-URI所标识的资源
     */
    DELETE	(4, "DELETE"),

    /**
     * 请求服务器回送收到的请求信息，主要用于测试或诊断
     */
    TRACE(5, "TRACE"),

    /**
     * 向指定资源位置上传其最新内容（部分更新，非幂等）
     */
    PATCH	(6, "PATCH"),

    /**
     * 返回服务器针对特定资源所支持的HTTP请求方法。
     * 也可以利用向Web服务器发送'*'的请求来测试服务器的功能性
     */
    OPTIONS	(7, "OPTIONS"),

    /**
     * 上传
     */
    UPLOAD(8, "UPLOAD"),

    /**
     * 下载
     */
    DOWNLOAD(9, "DOWNLOAD");

    private int code;
    private String name;

    public static HttpSendMethod getMethod(String methodName) {
        if (StringUtils.isBlank(methodName)) {
            return null;
        }
        for (HttpSendMethod value : HttpSendMethod.values()) {
            if (StringUtils.equalsIgnoreCase(methodName, value.getName())) {
                return value;
            }
        }
        throw new IllegalArgumentException("不存在名称为："+methodName+"http请求");
    }
}
