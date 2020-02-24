package com.kipa.common.run;

import com.kipa.base.BaseTestNGSpringContextTests;
import org.apache.commons.lang3.ClassUtils;

public class TestNGExecutor<T> {

    private Class<T> baseClass;

    public TestNGExecutor(Class<T> baseClass) {
        if (!ClassUtils.isAssignable(baseClass, BaseTestNGSpringContextTests.class)) {
            throw new IllegalArgumentException("基类必须为BaseTestNGSpringContextTests的子类");
        }
        this.baseClass = baseClass;
    }


    public void executeClass(TestNGDiscovery discovery) {
        SimpleTestNGLauncher<T> launcher = buildLauncher(discovery);
        ClassTestNGConverter<T> converter = new ClassTestNGConverter<>();
        ClassTestNGRunner runner = new ClassTestNGRunner(discovery.isUseDefaultListeners(), discovery.getListenerClass());
        launcher.launch(converter, runner);
    }

    public void executeXml(TestNGDiscovery discovery) {
        SimpleTestNGLauncher<T> launcher = buildLauncher(discovery);
        XmlTestNGConverter<T> converter = new XmlTestNGConverter<>();
        XmlTestNGRunner runner = new XmlTestNGRunner(discovery.isUseDefaultListeners(),discovery.getListenerClass());
        launcher.launch(converter, runner);
    }

    public void multiThreadExecute(TestNGDiscovery discovery) {
        TestNGLaunchCondition<T> condition = buildCondition(discovery);
        TestNGLaunchHandler<T> handler = new TestNGLaunchHandler<>(condition);
        MultiThreadTestNGLauncher<T> launcher = new MultiThreadTestNGLauncher<>(handler);
        XmlListTestNgConverter<T> converter = new XmlListTestNgConverter<>();
        XmlTestNGRunner runner = new XmlTestNGRunner(discovery.isUseDefaultListeners(), discovery.getListenerClass());
        launcher.launch(converter, runner);
    }

    private SimpleTestNGLauncher<T> buildLauncher(TestNGDiscovery discovery) {
        TestNGLaunchCondition<T> condition = buildCondition(discovery);
        TestNGLaunchHandler<T> handler = new TestNGLaunchHandler<>(condition);
        return new SimpleTestNGLauncher<>(handler);
    }

    private TestNGLaunchCondition<T> buildCondition(TestNGDiscovery discovery) {
        TestNGLaunchCondition<T> condition = new TestNGLaunchCondition<>();
        condition.setBaseClass(baseClass);
        condition.setFilterClass(discovery.getFilterClass());
        condition.setAnnotationType(discovery.getAnnotationType());
        condition.setSelectPackage(discovery.getSelectPackage());
        return condition;
    }

}
