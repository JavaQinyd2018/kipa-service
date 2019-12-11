package com.kipa.base;

import com.kipa.common.run.StepInterceptor;
import com.kipa.data.CSVDataProvider;
import com.kipa.data.DataMetaAnnotationListener;
import com.kipa.data.MethodOrderInterceptor;
import com.kipa.env.DatasourceEnvHolder;
import com.kipa.mybatis.service.condition.EnvFlag;
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
 * @author: Qinyadong
 * @date: 2019/4/8 10:13
 * testng 和spring整合，所有测试用例的入口,能满足基本的测试要求
 * 如果要使用redis或者mq请自行配置测试入口@code 样例是：
 * @see DemoApplicationConfiguration
 * @see DemoTestContextConfiguration
 */
@Slf4j
@ContextConfiguration(classes = {SpringIntegrationConfiguration.class})
@Listeners({DataMetaAnnotationListener.class,MethodOrderInterceptor.class, StepInterceptor.class})
public class BasicTestNGSpringContextTests extends AbstractTestNGSpringContextTests {

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

        //2.设定数据源（BaseTestConfiguration默认开启两个数据源，一个是默认数据源，一个是标识为env1的数据源）
        DatasourceEnvHolder.setFlag(new EnvFlag[]{EnvFlag.ENV1});
    }

    @DataProvider(name = "csv")
    public Iterator<Object[]> providerData(Method method) {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        return csvDataProvider.providerData(method);
    }
}