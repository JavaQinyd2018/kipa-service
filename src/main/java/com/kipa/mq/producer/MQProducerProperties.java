package com.kipa.mq.producer;

import com.kipa.core.Properties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 15:41
 * MQ生产者的配置类
 */
@Data
@NoArgsConstructor
public class MQProducerProperties implements Properties {

    /**
     * 组名
     */
    private String groupName;

    /**
     * NameServer的地址
     */
    private String nameServerAddress;
    /**
     * 最大消息数量
     */
    private Integer maxMessageSize ;

    /**
     * 发送信息超时时间
     */
    private Integer sendMsgTimeout;

    /**
     * 重试次数
     */
    private Integer retryTimesWhenSendFailed;
}
