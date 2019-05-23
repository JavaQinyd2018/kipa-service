package com.kipa.dubbo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 17:19
 */
@AllArgsConstructor
@Getter
public enum  InvokeType {

    /**
     * 同步调用
     */
    SYNCHRONOUS("sync"),

    /**
     * 异步调用
     */
    ASYNCHRONOUS("async"),

    /**
     * 不用配置中心直连
     */
    DIRECTED_LINK("direct");

    private String message;
}
