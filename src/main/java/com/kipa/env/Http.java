package com.kipa.env;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Http {

    @AliasFor("httpFlag")
    String value() default "";

    @AliasFor("value")
    String httpFlag() default "";

}
