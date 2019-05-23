package com.kipa.mq.consumer;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/27
 */
@NoArgsConstructor
@Data
public class SubscribeConfig {

    /**
     * 消息模式
     */
    private String topic;
    /**
     * 消费模式
     */
    private ConsumePattern pattern;
    /**
     * 订阅，模式
     */
    private SubscribeModel subscribeModel;
    /**
     * 消息模式
     */
    private MessageModel messageModel;
    /**
     *保存一个消费者订阅的topic下不同的tag以及tag对应的消息体类型
     */
    private Map<String, Method> tags = Maps.newHashMap();
    /**
     * 消费的位置
     */
    private ConsumeFromWhere consumePosition;

    private Map<Method, Class<?>> methodMap = Maps.newHashMap();
}
