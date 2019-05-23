package com.kipa.mq.consumer;

import com.kipa.utils.PreCheckUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */
@Component
public class MQConsumerGenerator {

    public MQConsumer createMQConsumer(MQConsumerConfig mqConsumerConfig, SubscribeConfig subscribeConfig) {
        PreCheckUtils.checkEmpty(mqConsumerConfig, "MQ消费端的配置信息不能为空");
        PreCheckUtils.checkEmpty(subscribeConfig, "MQ消费端的消息订阅配置不能为空");
        if (subscribeConfig.getPattern() == ConsumePattern.PUSH) {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
            consumer.setNamesrvAddr(mqConsumerConfig.getNameServerAddress());
            consumer.setConsumerGroup(mqConsumerConfig.getGroupName());
            consumer.setConsumeThreadMin(mqConsumerConfig.getConsumeThreadMin());
            consumer.setConsumeThreadMax(mqConsumerConfig.getConsumeThreadMax());
            consumer.setConsumeConcurrentlyMaxSpan(mqConsumerConfig.getConsumeMessageBatchMaxSize());
            consumer.setConsumeFromWhere(subscribeConfig.getConsumePosition());
            consumer.setMessageModel(subscribeConfig.getMessageModel());
            Map<String, Method> tags = subscribeConfig.getTags();
            try {
                if (MapUtils.isNotEmpty(tags)) {
                    List<String> tmpTags = new ArrayList<>(tags.keySet());
                    if (tmpTags.size() > 1 && tmpTags.contains("*")) {
                        throw new IllegalArgumentException("订阅的tag不合法");
                    }
                    String tag = StringUtils.join(tmpTags, "||");
                    consumer.subscribe(subscribeConfig.getTopic(), tag);
                }else {
                    consumer.subscribe(subscribeConfig.getTopic(), "*");
                }
            } catch (MQClientException e) {
                throw new IllegalArgumentException("订阅语法错误", e);
            }
            return consumer;
        }else if (subscribeConfig.getPattern() == ConsumePattern.PULL) {
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer();
            consumer.setNamesrvAddr(mqConsumerConfig.getNameServerAddress());
            consumer.setConsumerGroup(mqConsumerConfig.getGroupName());
            consumer.setMessageModel(subscribeConfig.getMessageModel());
            return consumer;
        }
        throw new IllegalArgumentException("不合法的消息消费模式");
    }

}
