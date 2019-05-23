package com.kipa.data;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/20
 * 测试用例数据注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataMeta {

    @AliasFor("dataParameter")
    DataParam[] value() default {};
    /**
     * 所有的参数写到csv数据文件，通过csv文件路径解析并获取参数
     * @return
     */
    @AliasFor("value")
    DataParam[] dataParameter() default {};

}
