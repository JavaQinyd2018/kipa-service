package com.kipa.base;

import com.kipa.config.*;
import com.kipa.mybatis.service.condition.EnvFlag;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:52
 * 所有配置的整合类，用于测试的继承
 */
@Configuration
@Import({SpringConfiguration.class,
        DatasourceConfiguration.class,
        MybatisConfiguration.class,
        HttpClientConfiguration.class,
        DubboConfiguration.class,
        MockServerConfiguration.class,
        EhcacheConfiguration.class})
@EnableMultipleDataSource(env = {EnvFlag.ENV1})
public class BaseConfiguration {
}
