package com.kipa.base;

import com.kipa.config.EnableRedis;
import com.kipa.config.EnableRocketMQ;
import com.kipa.env.AppConfigScan;
import com.kipa.redis.RedisModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/19
 * 所有配置的总配置类：http、dubbo、mock、mybatis、redis、mq
 */
@Configuration
//导入基本的http、dubbo、mock、mybatis的spring配置文件
@Import(SpringIntegrationConfiguration.class)
//包扫描：扫描带有@Database、@Dubbo、@Http的注解从而动态的切换spring配置
@AppConfigScan("com.kipa.service")
//默认开启集群的redis操作，若要开启集群的请添加RedisModel.CLUSTER注解
@EnableRedis(model = RedisModel.STAND_ALONE)
//开启MQ
@EnableRocketMQ(listenerScanPackage = "com.kipa.service")
public class DemoApplicationConfiguration {

}
