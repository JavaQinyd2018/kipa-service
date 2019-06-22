package com.kipa.mock.http.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 17:40
 * mock 错误信息
 */
@Data
@NoArgsConstructor
public class BaseMockError {

    private boolean dropConnection;
    private String responseMessage;
    /**
     * 延迟时长单位
     */
    private TimeUnit delayTimeUnit;
    /**
     * 延迟时长
     */
    private Long delay;
}
