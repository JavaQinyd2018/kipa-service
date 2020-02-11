package com.kipa.base;

import com.kipa.config.EnableMultipleDataSource;
import com.kipa.config.EnableRedis;
import com.kipa.config.EnableRocketMQ;
import com.kipa.env.EnableEnvironmentSwitch;
import com.kipa.env.EnvironmentType;
import com.kipa.mybatis.service.condition.EnvFlag;
import com.kipa.redis.RedisModel;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author Yadong Qin
 * @Date 2019/4/19
 * <p>自定义配置的样例，切记这仅仅是样例，需要根据自己的项目需要进行配置</>
 * <p>所有配置的总配置类：http、dubbo、mock、mybatis、redis、mq</p>
 * <p>切勿直接继承!!!!! </p>
 */

//1. 开启特定的测试环境- 测试
@EnableEnvironmentSwitch(env = EnvironmentType.TEST)
//2. 默认开启集群的redis操作，若要开启集群的请添加RedisModel.CLUSTER注解
@EnableRedis(model = RedisModel.STAND_ALONE)
//3. 开启RocketMQ的配置
@EnableRocketMQ(listenerScanPackage = "com.kipa.service")
//4. 开启多数据源
@EnableMultipleDataSource(env = {EnvFlag.ENV1, EnvFlag.ENV2, EnvFlag.ENV3, EnvFlag.ENV4})
//5. 默认的业务数据文件，通过Spring提供的原生的 @PropertySource 注解
@PropertySource("classpath:config/business.properties")
public class DemoSpringIntegrationConfiguration extends BaseSpringIntegrationConfiguration{

}
