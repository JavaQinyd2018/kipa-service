package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 14:33
 * 消息发送的接口，定义消息发送的方式
 *  1. 正常同步消息
 *  2.
 */
public interface MQProducerManager {
    /**
     * 同步发送消息
     * @param mqMessage 消息包装类
     * @return 发送消息后结果信息，包括消息ID、消息存储的一些信息
     */
    SendResult send(MQMessage mqMessage, MessageType messageType);

    /**
     * 异步消息发送
     * @param mqMessage 消息
     */
    void asyncSend(MQMessage mqMessage, MessageType messageType);


    /**
     * 单向发送
     * @param mqMessage 消息
     * @param messageType 消息类型
     */
    void sendOneWay(MQMessage mqMessage, MessageType messageType);
    /**
     * 发送事务消息
     * @param mqMessage 消息包装类
     * @return 发送消息后结果信息
     */
    TransactionSendResult sendInTransaction(MQMessage mqMessage, MessageType messageType);
}
