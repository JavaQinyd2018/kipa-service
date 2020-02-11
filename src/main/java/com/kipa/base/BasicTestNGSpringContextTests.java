package com.kipa.base;

import org.springframework.test.context.ContextConfiguration;

/**
 * @author Qinyadong
 * @date 2019/4/8 10:13
 *
 * <p> 提供了默认的Spring和TestNG整合的入口类,能满足基本的测试要求</p>
 * 1. 加载了Spring集成的配置类
 * 2. 继承了Spring整合TestNG的入口基类
 *
 * <p> 如果要使用Redis 或者MQ等请自行配置测试入口@code 样例是：</p>
 * @see com.kipa.base.DemoSpringIntegrationConfiguration
 * @see com.kipa.base.DemoTestNGSpringContextTests
 */
@ContextConfiguration(classes = {BasicSpringIntegrationConfiguration.class})
public class BasicTestNGSpringContextTests extends BaseTestNGSpringContextTests {

}
