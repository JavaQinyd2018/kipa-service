package com.kipa.common.run;

import org.apache.commons.collections4.MapUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Qinyadong
 * @date 2019/8/23 23:46
 * @description 多线程testNg执行器
 * @since 2.1.0
 */
public class MultiThreadTestNGLauncher<T> extends AbstractTestNGLauncher<T> {

    private static ExecutorService executorService = new ThreadPoolExecutor(3,
            20,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(20),
            new CustomizableThreadFactory("TestNgLauncher"),
            new ThreadPoolExecutor.AbortPolicy());


    public MultiThreadTestNGLauncher(TestNGLaunchCondition<T> discovery) {
        super(discovery);
    }

    @Override
    public void launch() {
        TestNGLaunchHandler<T> testNGLaunchHandler = getTestNGLaunchHandler();
        Map<String, List<Class<? extends T>>> multiLaunchClass = testNGLaunchHandler.getMultiLaunchClass();
        if (MapUtils.isNotEmpty(multiLaunchClass)) {
            multiLaunchClass.forEach((packageName, classeList) -> executorService.execute(() -> run(convert(classeList))));
        }
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
