package com.kipa.base;

import com.kipa.config.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:52
 * spring所有配置的整合类，用于测试的继承
 * 1. 通过@Import导入spring的配置类的时候，如果配置类里面有很多@Bean标注的注解，为了在配置类初始化的时候
 * 加载并初始化@Bean标注的类，需要在在配置类上面加注解
 * 2. @Import导入普通的spring类的时候，直接导入，不需要其他的@Component等注解
 * 3. 关于包扫描：有针对性的扫描对应的目录
 */
@Configuration
@Import({DatasourceConfiguration.class,
        MybatisConfiguration.class,
        HttpClientConfiguration.class,
        DubboConfiguration.class,
        MockServerConfiguration.class,
        EhcacheConfiguration.class})
@ComponentScans(value = {
        @ComponentScan("com.kipa.mybatis.service.impl"),
        @ComponentScan("com.kipa.http.service"),
        @ComponentScan("com.kipa.dubbo.service"),
        @ComponentScan("com.kipa.log"),
        @ComponentScan("com.kipa.env"),
        @ComponentScan("com.kipa.mock.http.service"),
        @ComponentScan("com.kipa.mock.dubbo"),
        @ComponentScan("com.kipa.data.ehcache")
})
@PropertySource(value = "classpath:config/business.properties")
@EnableAspectJAutoProxy
@EnableCaching
public class SpringIntegrationConfiguration {

}
