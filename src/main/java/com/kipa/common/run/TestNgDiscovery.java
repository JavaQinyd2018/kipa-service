package com.kipa.common.run;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/8/16 17:29
 * @desciption 测试发现类
 * @since 2.2.0
 */
@AllArgsConstructor
@Getter
public class TestNgDiscovery{

    /**
     * 要选择的包路径名
     */
    private String selectPackage;
    /**
     * 需要排除的测试类
     */
    private List<Class<?>> filterClass;
    /**
     * 需要带有的注解
     */
    private Class<? extends Annotation> annotationType;



    public static TestNgDiscoveryBuilder builder() {
        return new TestNgDiscoveryBuilder();
    }

    public static class TestNgDiscoveryBuilder {
        private String selectPackage;
        private List<Class<?>> filterClassList = Lists.newArrayList();
        private Class<? extends Annotation> annotationType;

        public TestNgDiscoveryBuilder selectPackage(String selectPackage) {
            this.selectPackage = selectPackage;
            return this;
        }

        public TestNgDiscoveryBuilder filterClass(Class<?>... filterClass) {
            this.filterClassList.addAll(Arrays.asList(filterClass));
            return this;
        }

        public TestNgDiscoveryBuilder withAnnotationType(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
            return this;
        }

        public TestNgDiscovery build() {
            return new TestNgDiscovery(this.selectPackage, this.filterClassList, this.annotationType);
        }
    }
}


