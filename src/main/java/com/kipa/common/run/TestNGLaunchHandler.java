package com.kipa.common.run;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kipa.utils.PackageScanUtils;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Qinyadong
 * @date 2019/8/26 10:15
 * @description 条件处理器
 * @since 2.1.0
 */
class TestNGLaunchHandler<T> {

    private TestNGLaunchCondition<T> discovery;

    TestNGLaunchHandler(TestNGLaunchCondition<T> discovery) {
        this.discovery = discovery;
    }

    /**
     * 获取运行的测试类
     * @return
     */
    List<Class<? extends T>> getLaunchClass() {
        final List<Class<?>> filterClass = discovery.getFilterClass();
        String selectPackage = discovery.getSelectPackage();
        Class<T> baseClass = discovery.getBaseClass();
        Class<? extends Annotation> annotationType = discovery.getAnnotationType();
        PreCheckUtils.checkEmpty(selectPackage, "扫描的包路径不能为空");
        return getSimpleLaunchClass(baseClass, selectPackage,filterClass, annotationType);
    }

    /**
     * 获取多线程执行的测试类映射关系
     * @return
     */
    Map<String, List<Class<? extends T>>> getMultiLaunchClass() {
        final Map<String, List<Class<? extends T>>> map = Maps.newHashMap();
        Class<T> baseClass = discovery.getBaseClass();
        Class<? extends Annotation> annotationType = discovery.getAnnotationType();
        final List<Class<?>> filterClass = discovery.getFilterClass();
        String selectPackage = discovery.getSelectPackage();
        PreCheckUtils.checkEmpty(selectPackage, "扫描的包路径不能为空");
        List<String> allPackageName = PackageScanUtils.getAllPackageName(selectPackage);
        if (CollectionUtils.isNotEmpty(allPackageName)) {
            allPackageName.forEach(packageName -> {
                List<Class<? extends T>> simpleLaunchClass = getSimpleLaunchClass(baseClass, packageName, filterClass, annotationType);
                if(CollectionUtils.isNotEmpty(simpleLaunchClass)) {
                    map.put(packageName, simpleLaunchClass);
                }
            });
        }
        return map;
    }


    private List<Class<? extends T>> getSimpleLaunchClass(Class<T> baseClass,
                                                          String simpleSelectPackage,
                                                          final List<Class<?>> filterClass,
                                                          Class<? extends Annotation> annotationType) {
        List<Class<? extends T>> classList = Lists.newArrayList();
        Set<Class<? extends T>> classes = PackageScanUtils.getClassOfType(simpleSelectPackage, baseClass);
        if (CollectionUtils.isNotEmpty(classes)) {
            Stream<Class<? extends T>> stream = classes.stream();
            //1. 按照过滤的类进行过滤
            if (CollectionUtils.isNotEmpty(filterClass)) {
                stream = stream.filter(type -> !filterClass.contains(type));
            }
            //2.按照注解进行过滤
            if (annotationType != null) {
                stream = stream.filter(type-> type.getAnnotation(annotationType) != null);
            }
            //3. 按照TestCase的order属性进行排序
            classList = stream.filter(type -> type.getAnnotation(TestCase.class) != null)
                    .sorted((o1, o2) -> {
                        TestCase testCase1 = o1.getAnnotation(TestCase.class);
                        TestCase testCase2 = o2.getAnnotation(TestCase.class);
                        return Integer.compare(testCase1.order(), testCase2.order());
                    }).collect(Collectors.toList());
        }
        return classList;
    }
}
