package com.kipa.mq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import java.util.List;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */

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
            MessageHandler.handleException(e);
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

}
