package com.kipa.common.run;

import org.apache.commons.collections4.CollectionUtils;
import org.testng.TestNG;
import org.testng.reporters.TestHTMLReporter;

import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/18 13:39
 * @desciption 抽象类
 * @since
 */
public abstract class AbstractLauncher implements Launcher  {

    private Class baseClass;

    public <T> AbstractLauncher(Class<T> clazz) {
        this.baseClass = clazz;
    }
    @Override
    public <T> void launch(Class<? extends T>... classes) {
        run(classes);
    }

    @Override
    public <T> void launch(TestNgDiscovery discovery) {
        List<Class<? extends T>> typeListWithAnnotation = getTypeListWithAnnotation(discovery, baseClass);
        if (CollectionUtils.isEmpty(typeListWithAnnotation)) {
            return;
        }
        Class<? extends T>[] convert = convert(typeListWithAnnotation);
        run(convert);
    }

    public abstract <T> List<Class<? extends T>> getTypeListWithAnnotation(TestNgDiscovery discovery, Class<T> baseClass);

    @SafeVarargs
    private final <T> void run(Class<? extends T>... testCaseClass) {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(testCaseClass);
        TestHTMLReporter reporter = new TestHTMLReporter();
        testNG.addListener(reporter);
        testNG.run();
    }

    /**
     * 数据格式转换
     * @param typeListWithAnnotation
     * @return
     */
    private <T> Class<? extends T>[] convert(List<Class<? extends T>> typeListWithAnnotation) {
        Class<? extends T>[] classArray = new Class[typeListWithAnnotation.size()];
        for (int i = 0; i < typeListWithAnnotation.size(); i++) {
            classArray[i] = typeListWithAnnotation.get(i);
        }
        return classArray;
    }
}
