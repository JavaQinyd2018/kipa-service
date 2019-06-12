package com.kipa.mock.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:40
 * mock调用类型
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeType {

    MockType type() default MockType.RESPONSE;

}
