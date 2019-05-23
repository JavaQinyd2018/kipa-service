package com.kipa.mq.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:58
 */
@AllArgsConstructor
@Getter
public enum ProducerMethod {

    /**
     * 同步发送
     */
    SEND("send"),

    /**
     * 异步发送
     */
    ASNYC_SEND("asnycSend"),

    /**
     * 单向发送
     */
    SEND_ONE_WAY("sendOneWay"),

    /**
     * 顺序发送
     */
    SEND_ORDER("sendOrder"),
    /**
     * 单向顺序发送
     */
    SEND_ONE_WAY_ORDER("sendOneWayOrder"),

    /**
     * 发送事物消息
     */
    SEND_IN_TRANSACTION("sendInTransaction");

    private String message;

    public static ProducerMethod getMethod(String methodName) {
        ProducerMethod[] values = ProducerMethod.values();
        for (ProducerMethod value : values) {
            if (StringUtils.equalsIgnoreCase(methodName, value.message)) {
                return value;
            }
        }
        throw new IllegalArgumentException("没有对应的枚举");
    }
}
