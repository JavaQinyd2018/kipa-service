package com.kipa.mock.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 16:23
 * mock的方式
 */
@AllArgsConstructor
@Getter
public enum MockMethod {

    /**
     * 请求方式：参数
     */
    PARAMETER("param"),

    /**
     * 请求体：json，xml等等
     */
    BODY("body");

    private String message;
}
