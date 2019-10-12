package com.kipa.config;

import com.kipa.common.DataConstant;
import com.kipa.mq.consumer.MQConsumerConfig;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/4/17 12:56
 * MQ消费端的配置类
 */
@Configuration
@PropertySource("classpath:mq/mq-consumer.properties")
@ComponentScan("com.kipa.mq.consumer")
public class MQConsumerConfiguration {

    private String nameServerAddress;
    private String groupName;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @PostConstruct
    public void init() {
        Properties properties = PropertiesUtils.loadProperties(DataConstant.CONFIG_FILE);
        groupName = PropertiesUtils.getProperty(properties, null, "rocketmq.consumer.groupName");
        nameServerAddress = PropertiesUtils.getProperty(properties, null, "rocketmq.consumer.nameServerAddress");
        PreCheckUtils.checkEmpty(nameServerAddress, "MQ的消费端的nameServer地址不能为空");
        PreCheckUtils.checkEmpty(groupName, "MQ的消费端的groupName地址不能为空");
    }

    @Bean
    public MQConsumerConfig mqConsumerConfig() {
        MQConsumerConfig config = new MQConsumerConfig();
        config.setNameServerAddress(nameServerAddress);
        config.setGroupName(groupName);
        config.setConsumeThreadMax(consumeThreadMax);
        config.setConsumeThreadMin(consumeThreadMin);
        config.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        return config;
    }

}
