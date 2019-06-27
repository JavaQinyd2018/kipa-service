package com.kipa.config;

import com.kipa.mybatis.service.condition.EnvFlag;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/6/25 17:54
 * @since:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMultipleDataSource {

    EnvFlag[] env() default {};
}
