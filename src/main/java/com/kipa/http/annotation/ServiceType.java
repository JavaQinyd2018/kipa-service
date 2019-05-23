package com.kipa.http.annotation;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 */

import com.kipa.http.emuns.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface ServiceType {

    RequestType requestType() default RequestType.PARAMETER;
}
