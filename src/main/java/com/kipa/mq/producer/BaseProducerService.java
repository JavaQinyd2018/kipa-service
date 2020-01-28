package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:27
 */
public interface BaseProducerService {
    /**
     * 同步发送消息
     * @param mqProducer 消息生产者对象
     * @param mqMessage 消息包装类
     * @return 发送消息后结果信息，包括消息ID、消息存储的一些信息
     */
    SendResult send(DefaultMQProducer mqProducer, MQMessage mqMessage, MessageType messageType);

    /**
     * 异步消息发送
     * @param producer 消息生产者对象
     * @param mqMessage 消息
     */
    void asyncSend(DefaultMQProducer producer, MQMessage mqMessage, MessageType messageType);


    /**
     * 单向发送
     * @param producer 消息生产者对象
     * @param mqMessage 消息
     * @param messageType 消息类型
     */
    void sendOneWay(DefaultMQProducer producer, MQMessage mqMessage, MessageType messageType);
    /**
     * 发送事务消息
     * @param producer 消息生产者对象
     * @param mqMessage 消息包装类
     * @return 发送消息后结果信息
     */
    TransactionSendResult sendInTransaction(TransactionMQProducer producer, MQMessage mqMessage, MessageType messageType);
}
