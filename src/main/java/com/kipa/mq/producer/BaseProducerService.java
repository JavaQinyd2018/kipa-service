package com.kipa.mq.producer;

import org.apache.rocketmq.client.producer.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 18:27
 */
public interface BaseProducerService {

    SendResult send(DefaultMQProducer mqProducer, MQMessage mqMessage);

    void asnycSend(DefaultMQProducer producer, MQMessage mqMessage);

    TransactionSendResult sendInTransaction(TransactionMQProducer producer, MQMessage mqMessage);
}
