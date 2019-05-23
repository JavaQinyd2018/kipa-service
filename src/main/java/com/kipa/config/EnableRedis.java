package com.kipa.config;

import com.kipa.redis.RedisModel;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/24 15:25
 * 开启redis注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfiguration.class)
public @interface EnableRedis {

    RedisModel model() default RedisModel.CLUSTER;
}
