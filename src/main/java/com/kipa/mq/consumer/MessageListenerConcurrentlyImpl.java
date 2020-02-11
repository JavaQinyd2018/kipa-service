package com.kipa.mq.consumer;

import com.kipa.common.KipaProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */
@Slf4j
public class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

    private SubscribeConfig config;

    public MessageListenerConcurrentlyImpl(SubscribeConfig config) {
        this.config = config;
    }
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            MessageHandler.handleMessage(config, list);
        } catch (Exception e) {
            MessageHandler.handleException(e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
