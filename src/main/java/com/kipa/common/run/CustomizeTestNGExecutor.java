package com.kipa.common.run;

import com.kipa.base.BasicTestNGSpringContextTests;

/**
 * @author Qinyadong
 * @date 2019/8/27 10:10
 * @description 定制的执行器
 * @since
 */
public class CustomizeTestNGExecutor {

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
