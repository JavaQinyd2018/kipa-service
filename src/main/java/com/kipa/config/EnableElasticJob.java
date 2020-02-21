package com.kipa.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author qinyadong
 * 开启elastic-job定时任务组件
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ElasticJobConfiguration.class)
public @interface EnableElasticJob {
}
