package com.kipa.config;

import com.kipa.common.DataConstant;
import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.mq.consumer.MQConsumerProperties;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${rocketmq.consumer.nameServerAddress}")
    private String nameServerAddress;

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    private MQConsumerProperties mqConsumerProperties;

    @PostConstruct
    public void init() {
        mqConsumerProperties = new MQConsumerProperties();
        mqConsumerProperties.setGroupName(PropertiesUtils.getProperty(globalEnvironmentProperties, "rocketmq.consumer.groupName"));
        mqConsumerProperties.setNameServerAddress(PropertiesUtils.getProperty(globalEnvironmentProperties, "rocketmq.consumer.nameServerAddress"));
    }

    @Bean
    public MQConsumerProperties mqConsumerProperties() {
        MQConsumerProperties mqConsumerProperties = new MQConsumerProperties();
        mqConsumerProperties.setNameServerAddress(StringUtils.isNotBlank(mqConsumerProperties.getGroupName())? mqConsumerProperties.getGroupName(): nameServerAddress);
        mqConsumerProperties.setGroupName(StringUtils.isNotBlank(mqConsumerProperties.getGroupName())? mqConsumerProperties.getGroupName(): groupName);
        mqConsumerProperties.setConsumeThreadMax(consumeThreadMax);
        mqConsumerProperties.setConsumeThreadMin(consumeThreadMin);
        mqConsumerProperties.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        PreCheckUtils.checkEmpty(mqConsumerProperties.getNameServerAddress(), "MQ的消费端的nameServer地址不能为空");
        PreCheckUtils.checkEmpty(mqConsumerProperties.getGroupName(), "MQ的消费端的groupName地址不能为空");
        return mqConsumerProperties;
    }

}
