package com.kipa.common.run;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/23 21:19
 * @description 单一的处理器
 * @since 2.1.0
 */
public class SimpleTestNGLauncher<T> extends AbstractTestNGLauncher<T> {

    public SimpleTestNGLauncher(TestNGLaunchCondition<T> discovery) {
        super(discovery);
    }

    @Override
    public void launch() {
        TestNGLaunchHandler<T> testNGLaunchHandler = getTestNGLaunchHandler();
        List<Class<? extends T>> launchClass = testNGLaunchHandler.getLaunchClass();
        if (CollectionUtils.isNotEmpty(launchClass)) {
            run(convert(launchClass));
        }
    }
}
