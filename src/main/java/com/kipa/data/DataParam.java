package com.kipa.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Qinyadong
 * @date: 2019/4/10 12:25
 * 数据参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataParam {

    /**
     * 参数名称
     * @return
     */
    String paramName() default "";

    /**
     * 参数名称对应的值
     * @return
     */
    String paramValue() default "";

}
