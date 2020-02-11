package com.kipa.config;

import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.mq.producer.MQProducerProperties;
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

/**
 * @author: Qinyadong
 * @date: 2019/4/17 12:54
 * MQ生产端的配置类
 */
@Configuration
@PropertySource("classpath:mq/mq-producer.properties")
@ComponentScan("com.kipa.mq.producer")
public class MQProducerConfiguration {

    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.nameServerAddress}")
    private String nameServerAddress;
    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize ;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    private MQProducerProperties initMqProducerProperties;

    @PostConstruct
    public void init() {
        initMqProducerProperties = new MQProducerProperties();
        initMqProducerProperties.setNameServerAddress(PropertiesUtils.getProperty(globalEnvironmentProperties, "rocketmq.producer.nameServerAddress"));
        initMqProducerProperties.setGroupName(PropertiesUtils.getProperty(globalEnvironmentProperties, "rocketmq.producer.groupName"));
    }

    @Bean
    public MQProducerProperties mqProducerProperties() {
        MQProducerProperties mqProducerProperties = new MQProducerProperties();
        mqProducerProperties.setNameServerAddress(StringUtils.isNotBlank(initMqProducerProperties.getNameServerAddress())? initMqProducerProperties.getNameServerAddress(): nameServerAddress);
        mqProducerProperties.setGroupName(StringUtils.isNotBlank(initMqProducerProperties.getGroupName()) ? initMqProducerProperties.getGroupName(): groupName);
        mqProducerProperties.setMaxMessageSize(maxMessageSize);
        mqProducerProperties.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        mqProducerProperties.setSendMsgTimeout(sendMsgTimeout);
        PreCheckUtils.checkEmpty(nameServerAddress, "MQ的nameServer地址不能为空");
        PreCheckUtils.checkEmpty(groupName, "MQ的groupName地址不能为空");
        return mqProducerProperties;
    }

}
