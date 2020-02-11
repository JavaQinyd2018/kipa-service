package com.kipa.env;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Import(EnvironmentSwitchRegistrar.class)
public @interface EnableEnvironmentSwitch {

    EnvironmentType env() default EnvironmentType.DEFAULT;
}
