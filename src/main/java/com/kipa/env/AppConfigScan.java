package com.kipa.env;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/4/19 19:28
 * 注解扫描器
 * 扫描：@Database、@Http、@dubbo注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AppConfigScan {

    @AliasFor("basePackage")
    String value() default "";

    @AliasFor("value")
    String basePackage() default "";
}
