package com.kipa.mq.producer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 15:41
 * MQ生产者的配置类
 */
@Data
@NoArgsConstructor
public class MQProducerProperties {

    private String groupName;
    private String nameServerAddress;
    private Integer maxMessageSize ;
    private Integer sendMsgTimeout;
    private Integer retryTimesWhenSendFailed;
}
