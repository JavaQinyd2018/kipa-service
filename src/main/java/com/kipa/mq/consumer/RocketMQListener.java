package com.kipa.mq.consumer;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/17 12:59
 * 用改注解标记的类，为RocketMq的消息的消费（订阅）类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQListener {

    /**
     * topic
     * @return
     */
    String topic();

    /**
     * consumer的方式DefaultMQPushConsumer或者DefaultMQPullConsumer
     * @return
     */
    ConsumePattern pattern() default ConsumePattern.PUSH;
    /**
     * 是否是顺序消费
     * @return
     */
    SubscribeModel subscriberModel() default SubscribeModel.CURRENTLY;

    /**
     * 转化为DefaultMqPushConsumer之后订阅的topic
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;

    /**
     * 消费的位置
     * @return
     */
    ConsumeFromWhere consumePosition() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
}
