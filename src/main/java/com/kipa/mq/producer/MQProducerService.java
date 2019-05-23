package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 10:31
 * Mq消息的生产者类
 */
public interface MQProducerService {

    boolean send(String messageBody, String topic);

    boolean send(String messageBody, String topic, String tags);

    boolean send(String messageBody, String topic, String tags, String keys);

    boolean send(String messageBody, String topic, String keys, int delayLevel);

    boolean send(String messageBody, String topic, String tags, String keys, int delayLevel);

    void asyncSend(String messageBody, String topic, SendCallback sendCallback);

    void asyncSend(Message message, SendCallback sendCallback);

    void sendOneWay(String messageBody, String topic);

    void sendOneWay(Message message);

    boolean sendOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg);

    boolean sendOrder(Message message,  MessageQueueSelector selector, Object arg);

    boolean sendOneWayOrder(String messageBody,String topic,MessageQueueSelector selector, Object arg);

    boolean sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg);

    boolean sendInTransaction(String messageBody, String topic, Object arg);

    boolean sendInTransaction(Message message, Object arg);
}
