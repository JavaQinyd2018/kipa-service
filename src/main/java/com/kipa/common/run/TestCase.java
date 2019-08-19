package com.kipa.common.run;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Qinyadong
 * @date 2019/8/16 17:35
 * @desciption 测试用例的标识注解，方便进行过滤，按条件运行
 * @since 2.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestCase {

    int order() default 0;

    String description() default "";
}
