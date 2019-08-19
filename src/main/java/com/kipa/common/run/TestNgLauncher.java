package com.kipa.common.run;

import com.google.common.collect.Lists;
import com.kipa.utils.PackageScanUtils;
import com.kipa.utils.PreCheckUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Qinyadong
 * @date 2019/8/16 16:57
 * @desciption testng的运行器
 * @since 2.2.0
 */
public class TestNgLauncher extends AbstractLauncher {

    public <T> TestNgLauncher(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public <T> List<Class<? extends T>> getTypeListWithAnnotation(TestNgDiscovery discovery, Class<T> baseClass) {
        List<Class<? extends T>> classList = Lists.newArrayList();
        final List<Class<?>> filterClass = discovery.getFilterClass();
        String selectPackage = discovery.getSelectPackage();
        PreCheckUtils.checkEmpty(selectPackage, "扫描的包路径不能为空");
        Set<Class<? extends T>> classes = PackageScanUtils.getClassOfType(selectPackage, baseClass);
        if (CollectionUtils.isNotEmpty(classes)) {
            Stream<Class<? extends T>> stream = classes.stream();
            //1. 按照过滤的类进行过滤
            if (CollectionUtils.isNotEmpty(filterClass)) {
                stream = stream.filter(type -> !filterClass.contains(type));
            }
            //2.按照注解进行过滤
            Class<? extends Annotation> annotationType = discovery.getAnnotationType();
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
