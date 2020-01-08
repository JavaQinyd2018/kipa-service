package com.kipa.mock.http.entity;

import com.kipa.core.InvokeRequest;
import com.kipa.core.InvokeResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:40
 * mock 跳转
 */
@Data
@NoArgsConstructor
public class BaseMockForward implements InvokeRequest {

    private String host;
    private Integer port;
    private String scheme;
    /**
     * 延迟时长单位
     */
    private TimeUnit delayTimeUnit;
    /**
     * 延迟时长
     */
    private Long delay;
}
