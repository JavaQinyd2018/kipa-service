package com.kipa.mq.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:21
 * rocket message的包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MQMessage {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 发送的消息
     */
    private Message message;

    /**
     * 异步消息回调接口
     */
    private SendCallback sendCallback;

    /**
     * 顺序消息选择器
     */
    private MessageQueueSelector selector;

    /**
     * 发送到特定的消息队列
     */
    private MessageQueue messageQueue;
    /**
     * 各种参数信息对象
     */
    private Object arg;

    /**
     * 消息发送超时时间
     */
    private long timeout;
}
