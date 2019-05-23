package com.kipa.env;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Dubbo {

    @AliasFor("configFlag")
     String value() default "";
    /**
     * 环境配置标识
     * @return
     */
    @AliasFor("value")
    String configFlag() default "";

    /**
     * 调用provider的version
     * @return
     */
    String version() default "";

    /**
     * 超时时间
     */
    int timeout() default 12000;
}
