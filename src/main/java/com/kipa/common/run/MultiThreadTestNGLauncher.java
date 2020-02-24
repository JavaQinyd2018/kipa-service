package com.kipa.common.run;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.testng.xml.XmlSuite;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MultiThreadTestNGLauncher<T> {

    private static ExecutorService executorService = new ThreadPoolExecutor(3,
            20,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(20),
            new CustomizableThreadFactory("TestNgLauncher"),
            new ThreadPoolExecutor.AbortPolicy());


    private TestNGLaunchHandler<T> handler;

    public MultiThreadTestNGLauncher(TestNGLaunchHandler<T> handler) {
        this.handler = handler;
    }

    public void launch(XmlListTestNgConverter<T> converter, XmlTestNGRunner runner) {
        final Map<String, List<Class<? extends T>>> multiLaunchClass = handler.getMultiLaunchClass();
        if (MapUtils.isEmpty(multiLaunchClass)) {
            log.warn("===========[注意] 当前没有符合条件的可执行用例===========");
        }
        final List<XmlSuite> xmlSuiteList = converter.convert(multiLaunchClass);
        xmlSuiteList.forEach(xmlSuite -> {
            executorService.execute(() -> {
                runner.run(xmlSuite);
            });
        });

        executorService.shutdown();
        try {
            if(executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
