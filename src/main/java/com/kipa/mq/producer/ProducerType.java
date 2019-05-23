package com.kipa.mq.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:13
 *
 */
@AllArgsConstructor
@Getter
public enum ProducerType {

    /**
     * 默认生产者
     */
    DEFAULT_PRODUCER("default"),

    /**
     * 发送事物消息的生产者
     */
    TRANSACTION_PRODUCER("transaction");

    private String message;
}
