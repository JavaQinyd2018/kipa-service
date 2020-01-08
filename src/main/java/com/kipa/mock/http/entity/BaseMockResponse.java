package com.kipa.mock.http.entity;

import com.kipa.core.InvokeRequest;
import com.kipa.core.InvokeResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:25
 * mock接口响应的结果
 */
@Data
@NoArgsConstructor
public class BaseMockResponse implements InvokeRequest {
    /**
     * 响应的cookie
     */
    private Map<String, String> cookies;
    /**
     * 相应的header
     */
    private Map<String, String> headers;
    /**
     * 相应的结果体
     */
    private String body;
    /**
     * 结果原因
     */
    private String reasonPhrase;
    /**
     * 结果状态码
     */
    private Integer statusCode;
    /**
     * 延迟时长单位
     */
    private TimeUnit delayTimeUnit;
    /**
     * 延迟时长
     */
    private Long delay;
}
