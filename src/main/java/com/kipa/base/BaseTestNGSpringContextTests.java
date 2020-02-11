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
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author qinyadong
 * @date 2019.03.02
 *
 * <p> Spring整合TestNG的入口类 </p>
 * 1. 整合了TestNG
 * 2. 打印了Banner
 * 3. 设置了默认数据源
 * 4. 添加了默认数据驱动
 */
@Slf4j
@Listeners({DataMetaAnnotationListener.class, MethodOrderInterceptor.class, StepInterceptor.class})
public class BaseTestNGSpringContextTests extends AbstractTestNGSpringContextTests {

    private static final String BANNER_PATH= "customize/kipa-banner.txt";

    static {
        Resource resource = new ClassPathResource(BANNER_PATH);
        if (!ObjectUtils.isEmpty(resource)) {
            try {
                InputStream inputStream = resource.getInputStream();
                String banner = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                log.info("框架初始化：\n{}",banner);
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
