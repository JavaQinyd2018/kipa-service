package com.kipa.mq.producer;

import com.kipa.utils.PreCheckUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 10:48
 * 消息发送服务
 */
@Service("mqProducerService")
public class MQProducerServiceImpl implements MQProducerService {

    /**
     * 超时时间
     */
    private static final long TIME_OUT = 5;

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
        final Message message = assemble(messageBody, topic, tags, keys, delayLevel);
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).build();
        SendResult sendResult = mqProducerManager.send(mqMessage, MessageType.NORMAL);
        return parseResult(sendResult);
    }

    @Override
    public void asyncSend(String messageBody, String topic,String tags, SendCallback sendCallback) {
        final Message message = assemble(messageBody, topic, null, null, 0);
        asyncSend(message, sendCallback, TIME_OUT);
    }

    @Override
    public void asyncSend(Message message, SendCallback sendCallback,long timeout) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).sendCallback(sendCallback).build();
        mqProducerManager.asyncSend(mqMessage,MessageType.NORMAL);
    }

    @Override
    public void sendOneWay(String messageBody, String topic, String tag) {
        final Message message = assemble(messageBody, topic, null, null, 0);
        sendOneWay(message);
    }

    @Override
    public void sendOneWay(Message message) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).build();
        mqProducerManager.sendOneWay(mqMessage, MessageType.NORMAL);
    }

    @Override
    public SendResult send(Message message, long timeout) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).timeout(timeout).build();
        return mqProducerManager.send(mqMessage, MessageType.NORMAL);
    }

    @Override
    public void asyncSend(Message message, MessageQueueSelector selector, Object arg, SendCallback sendCallback) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).selector(selector).arg(arg).sendCallback(sendCallback).build();
        mqProducerManager.asyncSend(mqMessage, MessageType.NORMAL);
    }

    @Override
    public void sendSpecial(String messageBody, String topic, String tags, MessageQueue messageQueue) {
        Message message = assemble(messageBody, topic, tags, null, 0);
        sendSpecial(message, messageQueue, TIME_OUT);
    }

    @Override
    public void sendSpecial(Message message, MessageQueue messageQueue, long timeout) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).messageQueue(messageQueue).timeout(timeout).build();
        mqProducerManager.send(mqMessage, MessageType.SPECIAL);
    }

    @Override
    public boolean sendOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg) {
        Message message = assemble(messageBody, topic, "", null, 0);
        return sendOrder(message, selector, arg);
    }

    @Override
    public boolean sendOrder(Message message, MessageQueueSelector selector, Object arg) {
        check(message);
        return parseResult(sendOrder(message, selector, arg, TIME_OUT));
    }

    @Override
    public SendResult sendOrder(Message message, MessageQueueSelector selector, Object arg, long timeout) {
        MQMessage mqMessage = MQMessage.builder().message(message).selector(selector).arg(arg).timeout(timeout).build();
        return mqProducerManager.send(mqMessage, MessageType.ORDERLY);
    }

    @Override
    public void sendOneWayOrder(String messageBody, String topic, MessageQueueSelector selector, Object arg) {
        Message message = assemble(messageBody, topic, "", null, 0);
        sendOneWayOrder(message, selector, arg);
    }

    @Override
    public void sendOneWayOrder(Message message, MessageQueueSelector selector, Object arg) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).selector(selector).arg(arg).build();
        mqProducerManager.sendOneWay(mqMessage, MessageType.ORDERLY);
    }

    @Override
    public boolean sendInTransaction(String messageBody, String topic,String tags, Object arg) {
        Message message = assemble(messageBody, topic, tags, null, 0);
        return parseResult(sendInTransaction(message, arg));
    }

    @Override
    public TransactionSendResult sendInTransaction(Message message, Object arg) {
        check(message);
        MQMessage mqMessage = MQMessage.builder().message(message).arg(arg).build();
        return mqProducerManager.sendInTransaction(mqMessage, MessageType.NORMAL);
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

    private boolean parseResult(SendResult result) {
        if (result != null) {
            if (result instanceof TransactionSendResult) {
                TransactionSendResult transactionSendResult = (TransactionSendResult) result;
                return transactionSendResult.getLocalTransactionState().compareTo(LocalTransactionState.COMMIT_MESSAGE) == 0;
            }
            return result.getSendStatus().compareTo(SendStatus.SEND_OK) == 0;
        }
        return false;
    }
}
