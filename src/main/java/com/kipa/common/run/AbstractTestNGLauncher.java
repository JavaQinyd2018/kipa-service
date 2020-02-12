package com.kipa.common.run;

import lombok.Getter;
import lombok.Setter;
import org.testng.ITestNGListener;
import org.testng.TestNG;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/23 23:40
 * @description 抽象类
 * @since 2.1.0
 */
public abstract class AbstractTestNGLauncher<T> implements TestNGLauncher {

    private TestNGLaunchHandler<T> testNGLaunchHandler;

    /**
     * 同步锁
     */
    private final Object monitor = new Object();

    AbstractTestNGLauncher (TestNGLaunchCondition<T> discovery) {
        this.testNGLaunchHandler = new TestNGLaunchHandler<>(discovery);
    }

    @Setter
    @Getter
    private Class<? extends ITestNGListener> listenerClass;

    protected  void run(Class<? extends T>... testCaseClass) {
        synchronized (monitor) {
            TestNG testNG = new TestNG();
            if (getListenerClass() != null) {
                //默认不使用testng的监听器
                testNG = new TestNG(false);
                testNG.setListenerClasses(Collections.singletonList(listenerClass));
            }
            testNG.setTestClasses(testCaseClass);
            testNG.run();
        }
        //运行完正常退出
//        System.exit(0);
    }

    TestNGLaunchHandler<T> getTestNGLaunchHandler() {
        return testNGLaunchHandler;
    }


    /**
     * 数据格式转换
     * @param typeListWithAnnotation
     * @return
     */
    Class<? extends T>[] convert(List<Class<? extends T>> typeListWithAnnotation) {
        Class<? extends T>[] classArray = new Class[typeListWithAnnotation.size()];
        for (int i = 0; i < typeListWithAnnotation.size(); i++) {
            classArray[i] = typeListWithAnnotation.get(i);
        }
        return classArray;
    }
}
