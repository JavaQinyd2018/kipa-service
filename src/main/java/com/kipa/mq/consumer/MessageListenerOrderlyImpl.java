package com.kipa.mq.consumer;

import com.kipa.common.KipaProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */
@Slf4j
public class MessageListenerOrderlyImpl implements MessageListenerOrderly {

    private SubscribeConfig config;

    public MessageListenerOrderlyImpl(SubscribeConfig config) {
        this.config = config;
    }
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        try {
            MessageHandler.handleMessage(config, list);
        } catch (Exception e) {
            handleException(e);
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private static void handleException(final Exception e) {
        Class exceptionClass = e.getClass();
        if (exceptionClass.equals(UnsupportedEncodingException.class)) {
            log.error(e.getMessage());
        } else if (exceptionClass.equals(KipaProcessException.class)) {
            log.error(e.getMessage());
        }
    }
}
