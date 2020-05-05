package com.kipa.common.run;

import org.testng.ITestNGListener;
import org.testng.TestNG;

import java.util.Collections;

/**
 * @author qinyadong
 * @param <T>
 */
public abstract class AbstractTestNGRunner<T> implements TestNGRunner<T> {

    private Class<? extends ITestNGListener> listenerClass;

    private boolean useDefaultListeners;

    private final Object monitor = new Object();

    public AbstractTestNGRunner(boolean useDefaultListeners, Class<? extends ITestNGListener> listenerClass) {
        this.listenerClass = listenerClass;
        this.useDefaultListeners = useDefaultListeners;
    }

    @Override
    public void run(T test) {
        synchronized (monitor) {
            TestNG testNG = new TestNG(useDefaultListeners);
            if (listenerClass != null) {
                testNG.setUseDefaultListeners(useDefaultListeners);
                testNG.setListenerClasses(Collections.singletonList(listenerClass));
            }
            doRun(testNG, test);
            testNG.run();
        }
    }

    abstract void doRun(TestNG testNG, T test);
}
