package com.kipa.mq.producer;

import com.kipa.utils.RunTimeUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 17:45
 * MQ生产者的组装类
 */
@Service
public class MQProducerGenerator {

    /**
     * producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
     * @param config
     * @param type
     * @return
     */
    public MQProducer build(MQProducerConfig config, ProducerType type) {
        if (type == ProducerType.DEFAULT_PRODUCER) {
            DefaultMQProducer producer  = new DefaultMQProducer();
            producer.setNamesrvAddr(config.getNameServerAddress());
            producer.setProducerGroup(config.getGroupName());
            producer.setMaxMessageSize(config.getMaxMessageSize());
            producer.setRetryTimesWhenSendFailed(config.getRetryTimesWhenSendFailed());
            producer.setSendMsgTimeout(config.getSendMsgTimeout());
            producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
            return producer;
        }else if (type == ProducerType.TRANSACTION_PRODUCER){
            TransactionMQProducer producer = new TransactionMQProducer();
            producer.setNamesrvAddr(config.getNameServerAddress());
            producer.setProducerGroup(config.getGroupName());
            producer.setMaxMessageSize(config.getMaxMessageSize());
            producer.setRetryTimesWhenSendFailed(config.getRetryTimesWhenSendFailed());
            producer.setSendMsgTimeout(config.getSendMsgTimeout());
            producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
            return producer;
        }else {
            throw new RuntimeException("没有对应类型的生产者");
        }
    }

}
