package com.kipa.base;

import com.kipa.data.CSVDataProvider;
import com.kipa.data.DataMetaAnnotationListener;
import com.kipa.data.MethodOrderInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 10:13
 * testng 和spring整合，所有测试用例的入口,能满足基本的测试要求
 * 如果要使用redis或者mq请自行配置测试入口@code 样例是：
 * @see DemoApplicationConfiguration
 * @see DemoTestContextConfiguration
 */
@ContextConfiguration(classes = BaseConfiguration.class)
@Listeners({DataMetaAnnotationListener.class,MethodOrderInterceptor.class})
public class BaseTestConfiguration extends AbstractTestNGSpringContextTests {

    @DataProvider(name = "csv")
    public Iterator<Object[]> providerData(Method method) {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        return csvDataProvider.providerData(method);
    }
}
