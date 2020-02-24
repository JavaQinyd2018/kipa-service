package com.kipa.common.run;

import com.kipa.base.BasicTestNGSpringContextTests;

/**
 * @author Qinyadong
 * @date 2019/8/26 11:53
 * @description testng执行器
 * @since 2.1.0
 */
public final class DefaultTestNGExecutor {

    private TestNGExecutor<BasicTestNGSpringContextTests> testNGExecutor;

    /**
     * 初始化的时候创建执行器
     */
    public DefaultTestNGExecutor() {
        testNGExecutor = new TestNGExecutor<>(BasicTestNGSpringContextTests.class);
    }

    /**
     * 单一执行器
     * @param discovery
     */
    public void executeClass(TestNGDiscovery discovery) {
        testNGExecutor.executeClass(discovery);
    }

    public void executeXml(TestNGDiscovery discovery) {
        testNGExecutor.executeXml(discovery);
    }
    /**
     * 多包扫描执行器
     * @param discovery
     */
    public void multiThreadExecute(TestNGDiscovery discovery) {
        testNGExecutor.multiThreadExecute(discovery);
    }
}
