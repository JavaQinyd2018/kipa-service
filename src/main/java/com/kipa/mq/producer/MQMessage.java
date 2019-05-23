package com.kipa.mq.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

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

    private String methodName;

    private Message message;

    private SendCallback sendCallback;

    private MessageQueueSelector selector;

    private Object arg;

    private LocalTransactionExecuter localTransactionExecuter;

}
