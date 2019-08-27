package com.kipa.common.run;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/16 17:29
 * @desciption 测试发现类
 * @since 2.2.0
 */
@Data
class TestNGLaunchCondition<T> {

    /**
     * 基于T的基类 类似于BaseTestConfiguration以及自定义配置类
     */
    private Class<T> baseClass;
    /**
     * 要选择的包路径名
     */
    private String selectPackage;
    /**
     * 需要排除的测试类
     */
    private List<Class<?>> filterClass;
    /**
     * 需要带有的注解
     */
    private Class<? extends Annotation> annotationType;
}


