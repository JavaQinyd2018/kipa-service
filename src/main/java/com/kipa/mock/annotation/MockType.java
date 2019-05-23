package com.kipa.mock.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:37
 * mock类型
 */
@AllArgsConstructor
@Getter
public enum  MockType {
    /**
     * 正常响应
     */
    RESPONSE("response"),
    /**
     * 跳转
     */
    FORWARD("forward"),
    /**
     * 响应错误
     */
    ERROR("error");

    private String message;
}
