package com.kipa.mq.consumer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Qinyadong
 * @date: 2019/4/22 15:25
 * 订阅MQ消息的信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * 订阅消息的target
     * @return
     */
    String tag() default "";

    /**
     * 消息的类型
     * @return
     */
    Class<?> messageType() default Object.class;
}
