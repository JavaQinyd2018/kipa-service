package com.kipa.common.run;

/**
 * @author Qinyadong
 * @date 2019/8/18 13:32
 * @desciption 测试类运行发布器
 * @since 2.2.0
 */
public interface Launcher {

    /**
     * 根据类型运行测试类
     * @param classes
     */
    <T> void launch(Class<? extends T>... classes);

    /**
     * 根据测试发现运行测试类，可以定制运行的顺序
     * @param discovery
     */
    <T> void launch(TestNgDiscovery discovery);
}
