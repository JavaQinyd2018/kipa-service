package com.kipa.common.run;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleTestNGLauncher<T> implements TestNGLauncher<T>{

        private TestNGLaunchHandler<T> handler;
        public SimpleTestNGLauncher(TestNGLaunchHandler<T> handler) {
            this.handler = handler;
        }

    @Override
    public void launch(ClassTestNGConverter<T> converter, ClassTestNGRunner runner) {
        final List<Class<? extends T>> launchClass = handler.getLaunchClass();
        if (CollectionUtils.isEmpty(launchClass)) {
            log.warn("===========[注意] 当前没有符合条件的可执行用例===========");
        }
        runner.run(converter.convert(launchClass));
    }

    @Override
    public void launch(XmlTestNGConverter<T> converter, XmlTestNGRunner runner) {
        final Map<String, List<Class<? extends T>>> multiLaunchClass = handler.getMultiLaunchClass();
        if (MapUtils.isEmpty(multiLaunchClass)) {
            log.warn("===========[注意] 当前没有符合条件的可执行用例===========");
        }
        runner.run(converter.convert(multiLaunchClass));
    }
}
