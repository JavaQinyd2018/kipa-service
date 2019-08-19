package com.kipa.common.run;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Qinyadong
 * @date 2019/8/17 12:49
 * @desciption 测试类分为多个测试步骤 每个步骤的注解
 * @since 2.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Step {

    int order() default 0;

    String description() default "";
}
