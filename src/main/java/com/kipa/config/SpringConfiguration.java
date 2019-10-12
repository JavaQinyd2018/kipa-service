package com.kipa.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 19:43
 * spring配置文件，包扫描
 */
@Configuration
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
public class SpringConfiguration {

}
