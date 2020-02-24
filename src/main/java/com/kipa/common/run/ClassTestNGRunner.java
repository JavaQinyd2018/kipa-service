package com.kipa.common.run;

import org.testng.ITestNGListener;
import org.testng.TestNG;

public class ClassTestNGRunner extends AbstractTestNGRunner<Class<?>[]> {

    public ClassTestNGRunner(boolean useDefaultListeners, Class<? extends ITestNGListener> listenerClass) {
        super(useDefaultListeners, listenerClass);
    }

    @Override
    void doRun(TestNG testNG, Class<?>[] test) {
        testNG.setTestClasses(test);
    }
}
