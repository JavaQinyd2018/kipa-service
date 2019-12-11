package com.kipa.common.run;

import com.kipa.base.BasicTestNGSpringContextTests;

/**
 * @author Qinyadong
 * @date 2019/8/26 11:53
 * @description testng执行器
 * @since 2.1.0
 */
public final class TestNGExecutor {

    /**
     * 单一执行器
     * @param discovery
     */
    public void execute(TestNGDiscovery discovery) {
        TestNGLaunchCondition<BasicTestNGSpringContextTests> condition = new TestNGLaunchCondition<>();
        condition.setBaseClass(BasicTestNGSpringContextTests.class);
        condition.setFilterClass(discovery.getFilterClass());
        condition.setAnnotationType(discovery.getAnnotationType());
        condition.setSelectPackage(discovery.getSelectPackage());
        SimpleTestNGLauncher<BasicTestNGSpringContextTests> launcher = new SimpleTestNGLauncher<>(condition);
        launcher.setListenerClass(discovery.getListenerClass());
        launcher.launch();
    }

    /**
     * 多包扫描执行器
     * @param discovery
     */
    public void multiThreadExecute(TestNGDiscovery discovery) {
        TestNGLaunchCondition<BasicTestNGSpringContextTests> condition = new TestNGLaunchCondition<>();
        condition.setBaseClass(BasicTestNGSpringContextTests.class);
        condition.setFilterClass(discovery.getFilterClass());
        condition.setAnnotationType(discovery.getAnnotationType());
        condition.setSelectPackage(discovery.getSelectPackage());
        MultiThreadTestNGLauncher<BasicTestNGSpringContextTests> multiThreadTestNGLauncher = new MultiThreadTestNGLauncher<>(condition);
        multiThreadTestNGLauncher.setListenerClass(discovery.getListenerClass());
        multiThreadTestNGLauncher.launch();
    }
}
