package com.kipa.env;

import java.lang.annotation.*;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/6
 * 数据源配置注解，用来切换数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Database {

    /**
     * 默认是没有标识，需要特定制定标识的，需要传入标识
     * @return
     */
    String datasourceFlag() default "";
}
