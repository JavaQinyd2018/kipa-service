package com.kipa.base;

import com.kipa.data.CSVDataProvider;
import com.kipa.data.DataMetaAnnotationListener;
import com.kipa.env.Database;
import com.kipa.env.Dubbo;
import com.kipa.env.Http;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/19
 * 框架高级用法整合样例，切勿直接继承该类
 */
@Database(datasourceFlag = "dev")
@Http(httpFlag = "dev")
@Dubbo(configFlag = "dev",version = "1.0.0",timeout = 120000)
@Listeners({DataMetaAnnotationListener.class})
@ContextConfiguration(classes = DemoApplicationConfiguration.class)
public class DemoTestContextConfiguration extends AbstractTestNGSpringContextTests {

    @DataProvider(name = "csv")
    public Iterator<Object[]> providerData(Method method) {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        return csvDataProvider.providerData(method);
    }
}
