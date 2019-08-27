package com.kipa.common.run;

import lombok.Builder;
import lombok.Getter;
import org.testng.ITestNGListener;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/26 18:08
 * @description
 * @since
 */
@Builder
@Getter
public class TestNGDiscovery {

    /**
     * 基于T的基类 类似于BaseTestConfiguration以及自定义配置类
     */
    private Class<?> baseClass;
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

    private Class<? extends ITestNGListener> listenerClass;
}
