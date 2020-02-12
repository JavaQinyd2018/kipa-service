package com.kipa.common.run;

import com.kipa.base.BaseTestNGSpringContextTests;
import com.kipa.base.BasicTestNGSpringContextTests;
import org.apache.commons.lang3.ClassUtils;

/**
 * @author Qinyadong
 * @date 2019/8/27 10:10
 * @description 定制的执行器
 * @since
 */
public class TestNGExecutor<T> {

    private Class<T> baseClass;

    public TestNGExecutor(Class<T> baseClass) {
        if (!ClassUtils.isAssignable(baseClass, BaseTestNGSpringContextTests.class)) {
            throw new IllegalArgumentException("基类必须为BaseTestNGSpringContextTests的子类");
        }
        this.baseClass = baseClass;
    }

    /**
     * 单线程执行器
     * @param discovery
     */
    public void execute(TestNGDiscovery discovery) {
        TestNGLaunchCondition<T> condition = new TestNGLaunchCondition<>();
        condition.setBaseClass(baseClass);
        condition.setFilterClass(discovery.getFilterClass());
        condition.setAnnotationType(discovery.getAnnotationType());
        condition.setSelectPackage(discovery.getSelectPackage());
        SimpleTestNGLauncher<T> launcher = new SimpleTestNGLauncher<>(condition);
        launcher.setListenerClass(discovery.getListenerClass());
        launcher.launch();
    }

    /**
     * 多线程执行器
     * @param discovery
     */
    public void multiThreadExecute(TestNGDiscovery discovery) {
        TestNGLaunchCondition<T> condition = new TestNGLaunchCondition<>();
        condition.setBaseClass(baseClass);
        condition.setFilterClass(discovery.getFilterClass());
        condition.setAnnotationType(discovery.getAnnotationType());
        condition.setSelectPackage(discovery.getSelectPackage());
        MultiThreadTestNGLauncher<T> multiThreadTestNGLauncher = new MultiThreadTestNGLauncher<>(condition);
        multiThreadTestNGLauncher.setListenerClass(discovery.getListenerClass());
        multiThreadTestNGLauncher.launch();
    }
}
