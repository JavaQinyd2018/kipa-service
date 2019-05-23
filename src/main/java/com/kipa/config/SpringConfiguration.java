package com.kipa.config;

import org.springframework.context.annotation.*;

/**
 * @author: Qinyadong
 * @date: 2019/3/21 19:43
 * spring配置文件，包扫描
 */
@Configuration
@ComponentScans(value = {
        @ComponentScan("com.kipa.mybatis"),
        @ComponentScan("com.kipa.http.service"),
@ComponentScan("com.kipa.dubbo.service"),
@ComponentScan("com.kipa.log"),
@ComponentScan("com.kipa.env"),
@ComponentScan("com.kipa.mock.service")}
)
@PropertySource(value = "classpath:application.properties")
@EnableAspectJAutoProxy
public class SpringConfiguration {

}
