package com.kipa.mq.producer;

import com.kipa.utils.PreCheckUtils;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 10:48
 * 消息发送服务
 */
@Service("mqProducerService")
public class MQProducerServiceImpl implements MQProducerService {

    @Autowired
    private MQProducerManager mqProducerManager;

    @Override
    public boolean send(String messageBody, String topic) {
        return send(messageBody, topic, null);
    }

    @Override
    public boolean send(String messageBody, String topic, String tags) {
        return send(messageBody, topic, tags, null, 0);
    }

    @Override
    public boolean send(String messageBody, String topic, String tags, String keys) {
        return send(messageBody, topic, tags, keys, 0);
    }

    @Override
    public boolean send(String messageBody, String topic, String keys, int delayLevel) {
        return send(messageBody, topic, null, keys, delayLevel);
    }

    @Override
    public boolean send(String messageBody, String topic, String tags, String keys, int delayLevel) {
        final Message mqMessage = assemble(messageBody, topic, tags, keys, delayLevel);
        check(mqMessage);
        return mqProducerManager.send(mqMessage);
    }

    @Override
    public void asyncSend(String messageBody, String topic, SendCallback sendCallback) {
        final Message mqMessage = assemble(messageBody, topic, null, null, 0);
        check(mqMessage);
        mqProducerManager.asyncSend(mqMessage,sendCallback);
    }

    @Override
    public void asyncSend(Message message, SendCallback sendCallback) {
        check(message);
        mqProducerManager.asyncSend(message, sendCallback);
    }

    @Override
    public void sendOneWay(String messageBody, String topic) {
        final Message mqMessage = assemble(messageBody, topic, null, null, 0);
        check(mqMessage);
        mqProducerManager.sendOneWay(mqMessage);
    }

    @Override
    public void sendOneWay(Message message) {
        check(message);
        mqProducerManager.sendOneWay(message);
    }

    @Override
    public boolean sendOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg) {
        final Message mqMessage = assemble(messageBody, topic, null, null, 0);
        return mqProducerManager.sendOrder(mqMessage, selector, arg);
    }

    @Override
    public boolean sendOrder(Message message, MessageQueueSelector selector, Object arg) {
        check(message);
        return mqProducerManager.sendOrder(message,selector, arg);
    }

    @Override
    public boolean sendOneWayOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg) {
        final Message mqMessage = assemble(messageBody, topic, null, null, 0);
        check(mqMessage);
        return sendOneWayOrder(mqMessage, selector, arg);
    }

    @Override
    public boolean sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg) {
        return mqProducerManager.sendOneWayOrder(message, selector, arg);
    }

    @Override
    public boolean sendInTransaction(String messageBody, String topic, Object arg) {
        final Message mqMessage = assemble(messageBody, topic, null, null, 0);
        check(mqMessage);
        return mqProducerManager.sendInTransaction(mqMessage, arg);
    }

    @Override
    public boolean sendInTransaction(Message message, Object arg) {
        check(message);
        return mqProducerManager.sendInTransaction(message, arg);
    }


    private void check(Message message) {
        PreCheckUtils.checkEmpty(message, "MQ消息对象不能为空");
        PreCheckUtils.checkEmpty(message.getBody(), "MQ消息体message不能为空");
        PreCheckUtils.checkEmpty(message.getTopic(), "MQ消息的topic不能为空");
    }

    private Message assemble(String message, String topic, String tags, String keys, int delayLevel) {
        Message mqMessage = new Message();
        mqMessage.setBody(message.getBytes());
        mqMessage.setTags(tags);
        mqMessage.setTopic(topic);
        mqMessage.setKeys(keys);
        mqMessage.setDelayTimeLevel(delayLevel);
        return mqMessage;
    }
}
