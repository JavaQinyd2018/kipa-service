package com.kipa.base;

import com.kipa.env.EnableEnvironmentSwitch;
import com.kipa.env.EnvironmentType;
import org.springframework.context.annotation.PropertySource;

/**
 * @author qinyadong
 * @date 2019.03.02
 * <p>框架提供了默认的Spring配置类</p>
 * 1. 默认配置了环境：会自动读取 classpath:application.properties 文件
 * 2. 默认的业务数据文件：会加载 classpath:config/business.properties 文件
 */
//业务默认的数据配置文件
@PropertySource(value = "classpath:config/business.properties")
//业务默认的环境： application.properties
@EnableEnvironmentSwitch(env = EnvironmentType.DEFAULT)
public class BasicSpringIntegrationConfiguration extends BaseSpringIntegrationConfiguration {
}
