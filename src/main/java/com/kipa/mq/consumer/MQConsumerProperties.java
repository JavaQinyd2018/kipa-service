package com.kipa.mq.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 15:41
 */
@Data
@NoArgsConstructor
public class MQConsumerProperties {

    private String nameServerAddress;
    private String groupName;
    private int consumeThreadMin;
    private int consumeThreadMax;
    private int consumeMessageBatchMaxSize;
}
