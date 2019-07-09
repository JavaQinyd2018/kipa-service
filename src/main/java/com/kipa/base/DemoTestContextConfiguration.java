package com.kipa.base;

import com.kipa.data.CSVDataProvider;
import com.kipa.data.DataMetaAnnotationListener;
import com.kipa.env.Database;
import com.kipa.env.Dubbo;
import com.kipa.env.Http;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/19
 * 框架高级用法整合样例，切勿直接继承该类
 */
@Slf4j
@Database(datasourceFlag = "dev")
@Http(httpFlag = "dev")
@Dubbo(configFlag = "dev",version = "1.0.0",timeout = 120000)
@Listeners({DataMetaAnnotationListener.class})
@ContextConfiguration(classes = DemoApplicationConfiguration.class)
public class DemoTestContextConfiguration extends AbstractTestNGSpringContextTests {

    private static final String BANNER_PATH= "customize/kipa-banner.txt";

    static {
        Resource resource = new ClassPathResource(BANNER_PATH);
        if (!ObjectUtils.isEmpty(resource)) {
            try {
                InputStream inputStream = resource.getInputStream();
                String banner = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                log.debug("框架初始化：\n{}",banner);
            } catch (IOException e) {
                log.warn("获取banner信息失败");
            }
        }
    }

    @DataProvider(name = "csv")
    public Iterator<Object[]> providerData(Method method) {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        return csvDataProvider.providerData(method);
    }
}
