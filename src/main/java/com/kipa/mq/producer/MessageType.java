package com.kipa.mq.producer;

import lombok.Getter;

/**
 * 消息类型
 */
@Getter
public enum  MessageType {

    /**
     * 普通消息
     */
    NORMAL,

    /**
     * 顺序消息
     */
    ORDERLY,

    /**
     * 发送特定队列的消息：
     * 默认的队列是4个，有专门的Broker进行存储
     */
    SPECIAL;
}
