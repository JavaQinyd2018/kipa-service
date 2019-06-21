package com.kipa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kipa.env.DatabaseContextHolder;
import com.kipa.mybatis.type.DatasourceConfig;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

/**
 * @Author: Yadong Qin
 * @Date: 2019/4/7
 * 数据源spring配置文件
 */
@Configuration
@PropertySource(value = {"classpath:db/mybatis.properties"})
public class DatasourceConfiguration {

    private static final String DB_FILE = "config/db.properties";

    private DatasourceConfig datasourceConfig;

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

    @PostConstruct
    private void init() {
        Properties properties = PropertiesUtils.loadProperties(DB_FILE);
        String flag = DatabaseContextHolder.getFlag();
        String driverName = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.driver");
        String url = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.url");
        String username = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.username");
        String password = PropertiesUtils.getProperty(properties, flag, "mybatis.datasource.password");
        PreCheckUtils.checkEmpty(driverName, "数据源配置driver不能为空");
        PreCheckUtils.checkEmpty(url, "数据源配置的url不能为空");
        PreCheckUtils.checkEmpty(username, "数据源配置的username不能为空");
        PreCheckUtils.checkEmpty(password, "数据源配置的password不能为空");
        datasourceConfig = new DatasourceConfig();
        datasourceConfig.setDriverName(driverName);
        datasourceConfig.setUrl(url);
        datasourceConfig.setUsername(username);
        datasourceConfig.setPassword(password);
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
    public DruidDataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        PreCheckUtils.checkEmpty(druidDataSource, "数据源配置不能为空");
        druidDataSource.setDriverClassName(datasourceConfig.getDriverName());
        druidDataSource.setUrl(datasourceConfig.getUrl());
        druidDataSource.setUsername(datasourceConfig.getUsername());
        druidDataSource.setPassword(datasourceConfig.getPassword());
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


    @PreDestroy
    public void destroy() {
        DatabaseContextHolder.removeFlag();
    }
}
