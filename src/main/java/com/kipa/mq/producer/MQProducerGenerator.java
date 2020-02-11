package com.kipa.mq.producer;

import com.kipa.common.KipaProcessException;
import com.kipa.core.ClientFactory;
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
public class MQProducerGenerator implements ClientFactory<MQProducer, MQProducerProperties> {

    private ProducerType producerType;
    public MQProducerGenerator(ProducerType producerType) {
        this.producerType = producerType;
    }

    @Override
    public MQProducer create(MQProducerProperties properties) throws Exception {
        return build(properties, producerType);
    }

    /**
     * producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
     * @param properties
     * @param type
     * @return
     */
    private MQProducer build(MQProducerProperties properties, ProducerType type) {
        DefaultMQProducer producer;
        if (type == ProducerType.DEFAULT_PRODUCER) {
            producer  = new DefaultMQProducer();
        }else if (type == ProducerType.TRANSACTION_PRODUCER){
            producer = new TransactionMQProducer();
        }else {
            throw new KipaProcessException("没有对应类型的生产者");
        }
        producer.setNamesrvAddr(properties.getNameServerAddress());
        producer.setProducerGroup(properties.getGroupName());
        producer.setMaxMessageSize(properties.getMaxMessageSize());
        producer.setRetryTimesWhenSendFailed(properties.getRetryTimesWhenSendFailed());
        producer.setSendMsgTimeout(properties.getSendMsgTimeout());
        producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
        return producer;
    }

}
