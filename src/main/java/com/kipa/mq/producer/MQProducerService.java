package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 10:31
 * Mq消息的生产者类
 */
public interface MQProducerService {

    //=============同步普通发送
    boolean send(String messageBody, String topic);

    boolean send(String messageBody, String topic, String tags);

    boolean send(String messageBody, String topic, String tags, String keys);

    boolean send(String messageBody, String topic, String keys, int delayLevel);

    boolean send(String messageBody, String topic, String tags, String keys, int delayLevel);

    SendResult send(Message message, long timeout);
    //=========================================

    void asyncSend(String messageBody, String topic, String tags, SendCallback sendCallback);

    void asyncSend(Message message, SendCallback sendCallback, long timeout);

    void asyncSend(Message message, MessageQueueSelector selector, Object arg, SendCallback sendCallback);

    void sendOneWay(String messageBody, String topic, String tags);

    void sendOneWay(Message message);

    void sendSpecial(String messageBody, String topic, String tags, MessageQueue messageQueue);

    void sendSpecial(Message message, MessageQueue messageQueue,long timeout);

    boolean sendOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg);

    boolean sendOrder(Message message,  MessageQueueSelector selector, Object arg);

    SendResult sendOrder(Message message, MessageQueueSelector selector, Object arg, long timeout);

    void sendOneWayOrder(String messageBody,String topic,MessageQueueSelector selector, Object arg);

    void sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg);

    boolean sendInTransaction(String messageBody, String topic,String tags, Object arg);

    TransactionSendResult sendInTransaction(Message message, Object arg);
}
