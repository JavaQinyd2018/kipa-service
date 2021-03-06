package com.kipa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kipa.env.DatasourceEnvHolder;
import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.mybatis.service.condition.*;
import com.kipa.mybatis.ext.DatasourceProperties;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/7
 * 数据源spring配置文件
 */
@Configuration
@PropertySource(value = {"classpath:db/mybatis.properties"})
public class DatasourceConfiguration {

    private DatasourceProperties datasourceProperties;

    private DatasourceProperties datasourceProperties1;

    private DatasourceProperties datasourceProperties2;

    private DatasourceProperties datasourceProperties3;

    private DatasourceProperties datasourceProperties4;


    /**
     * 固定的配置
     */
    @Value("${mybatis.datasource.initialSize}")
    private int initialSize;

    @Value("${mybatis.datasource.minIdle}")
    private int minIdle;

    @Value("${mybatis.datasource.maxActive}")
    private int maxActive;

    @Value("${mybatis.datasource.maxWait}")
    private long maxWait;

    @Value("${mybatis.datasource.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${mybatis.datasource.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    @Value("${mybatis.datasource.validationQuery}")
    private String validationQuery;

    @Value("${mybatis.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${mybatis.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${mybatis.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${mybatis.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${mybatis.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    @PostConstruct
    private void init() {
        //1. 默认的数据源 初始化
        datasourceProperties = getConfigByFlag(null, globalEnvironmentProperties);
        //2. 根据环境的标识初始化其他数据源
        EnvFlag[] env = DatasourceEnvHolder.getEnv();
        if (ArrayUtils.isNotEmpty(env)) {
            List<EnvFlag> envFlags = Arrays.asList(env);
            datasourceProperties1 = envFlags.contains(EnvFlag.ENV1) ? getConfigByFlag(EnvFlag.ENV1.getCode(), globalEnvironmentProperties) : null;
            datasourceProperties2 = envFlags.contains(EnvFlag.ENV2) ? getConfigByFlag(EnvFlag.ENV2.getCode(), globalEnvironmentProperties) : null;
            datasourceProperties3 = envFlags.contains(EnvFlag.ENV3) ? getConfigByFlag(EnvFlag.ENV3.getCode(), globalEnvironmentProperties) : null;
            datasourceProperties4 = envFlags.contains(EnvFlag.ENV4) ? getConfigByFlag(EnvFlag.ENV4.getCode(), globalEnvironmentProperties) : null;
        }
    }

    /**
     *  <!-- 配置初始化大小、最小、最大 -->
     * 	    <property name="initialSize" value="1" />
     * 	    <property name="minIdle" value="1" />
     * 	    <property name="maxActive" value="20" />
     * 	    <!-- 配置获取连接等待超时的时间 -->
     * 	    <property name="maxWait" value="60000" />
     * 	    <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
     * 	    <property name="timeBetweenEvictionRunsMillis" value="60000" />
     * 	    <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
     * 	    <property name="minEvictableIdleTimeMillis" value="300000" />
     * 	    <property name="validationQuery" value="SELECT 'x'" />
     * 	    <property name="testWhileIdle" value="true" />
     * 	    <property name="testOnBorrow" value="false" />
     * 	    <property name="testOnReturn" value="false" />
     *
     * 	    <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
     * 	    <property name="poolPreparedStatements" value="true" />
     * 	    <property name="maxPoolPreparedStatementPerConnectionSize" value="20" />
     *
     * 	   	<!-- 监控统计用的filter:stat
     * 	   	日志用的filter:log4j
     * 	   	防御sql注入的filter:wall -->
     * 	    <!-- 配置监控统计拦截的filters，去掉后监控界面sql无法统计 -->
     * 	    <property name="filters" value="stat" />
     *
     * @return
     */

    @Bean(name = "dataSource",initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource dataSource() {
        return getDataSource(datasourceProperties);
    }

    @Conditional({Datasource1Condition.class})
    @Bean(name = "dataSource1",initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource1() {
        return getDataSource(datasourceProperties1);
    }

    @Conditional({Datasource2Condition.class})
    @Bean(name = "dataSource2",initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource2() {
        return getDataSource(datasourceProperties2);
    }

    @Conditional({Datasource3Condition.class})
    @Bean(name = "dataSource3",initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource3() {
        return getDataSource(datasourceProperties3);
    }

    @Conditional({Datasource4Condition.class})
    @Bean(name = "dataSource4",initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource4() {
        return getDataSource(datasourceProperties4);
    }

    @PreDestroy
    public void destroy() {
        DatasourceEnvHolder.removeFlag();
    }

    private DatasourceProperties getConfigByFlag(String flag, GlobalEnvironmentProperties properties) {
        String driverName = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.driver");
        String url = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.url");
        String username = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.username");
        String password = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.password");
        PreCheckUtils.checkEmpty(driverName, flag + "环境的数据源配置driver不能为空");
        PreCheckUtils.checkEmpty(url, flag + "环境的数据源配置的url不能为空");
        PreCheckUtils.checkEmpty(username, flag + "环境的数据源配置的username不能为空");
        PreCheckUtils.checkEmpty(password, flag + "环境的数据源配置的password不能为空");
        DatasourceProperties config = new DatasourceProperties();
        config.setDriverName(driverName);
        config.setUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }

    private DruidDataSource getDataSource(DatasourceProperties datasourceProperties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        PreCheckUtils.checkEmpty(datasourceProperties, "数据源配置不能为空");
        druidDataSource.setDriverClassName(datasourceProperties.getDriverName());
        druidDataSource.setUrl(datasourceProperties.getUrl());
        druidDataSource.setUsername(datasourceProperties.getUsername());
        druidDataSource.setPassword(datasourceProperties.getPassword());
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        return druidDataSource;
    }
}
