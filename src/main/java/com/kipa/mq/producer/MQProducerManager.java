package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 14:33
 * 消息发送的接口，定义消息发送的方式
 */
public interface MQProducerManager {

    /**
     * 同步发送
     * @param message
     * @return
     */
    boolean send(Message message);

    /**
     * 异步发送
     * @param message
     * @param sendCallback
     */
    void asyncSend(Message message, SendCallback sendCallback);

    /**
     * 单向发送
     * @param message
     */
    void sendOneWay(Message message);

    /**
     * 发送顺序消息
     * @param message
     * @param selector
     * @param arg
     * @return
     */
    boolean sendOrder(Message message, MessageQueueSelector selector, Object arg);

    /**
     * 单项发送顺序消息
     * @param message
     * @param selector
     * @param arg
     * @return
     */
    boolean sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg);

    /**
     * 发送事物消息
     * @param message
     * @param arg
     * @return
     */
    boolean sendInTransaction(Message message, Object arg);
}
