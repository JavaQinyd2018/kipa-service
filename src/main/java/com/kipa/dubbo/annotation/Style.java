package com.kipa.dubbo.annotation;

import com.kipa.dubbo.enums.InvokeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:57
 * 参数风格：普通的键值对参数，或者包装对象
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Style {

    InvokeType type() default InvokeType.SYNCHRONOUS;

}
