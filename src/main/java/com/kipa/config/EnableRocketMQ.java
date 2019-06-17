package com.kipa.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/23 13:04
 * 开启MQ
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MQProducerConfiguration.class, MQConsumerConfiguration.class})
public @interface EnableRocketMQ {

    String listenerScanPackage();
}
