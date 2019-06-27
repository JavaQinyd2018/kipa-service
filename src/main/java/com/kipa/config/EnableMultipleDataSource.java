package com.kipa.config;

import com.kipa.mybatis.service.condition.EnvFlag;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/6/25 17:54
 * 开启多数据源配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMultipleDataSource {

    EnvFlag[] env() default {};
}
