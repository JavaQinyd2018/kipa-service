package com.kipa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kipa.mybatis.service.condition.*;
import com.kipa.mybatis.service.impl.*;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * @author Qinyadong
 * @date 2019/3/21 11:24
 * mybatis 整合spring配置文件
 */
/**
 *启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfiguration {

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DruidDataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier(value = "dataSource") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Bean("mapperScannerConfigurer")
    @Primary
    public MapperScannerConfigurer mapperScannerConfigurer() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper");
        return mapperScannerConfigure;
    }

    @Bean
    public DatabaseService databaseService() {
        return new DatabaseService();
    }
//======================================================================================================================

    @Conditional({Datasource1Condition.class})
    @Bean("sqlSessionFactory1")
    public SqlSessionFactoryBean sqlSessionFactory1(@Qualifier(value = "dataSource1") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Conditional({Datasource1Condition.class})
    @Bean("mapperScannerConfigurer1")
    public MapperScannerConfigurer mapperScannerConfigurer1() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory1");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper1");
        return mapperScannerConfigure;
    }

    @Conditional({Datasource1Condition.class})
    @Bean("databaseService1")
    public DatabaseService1 databaseService1() {
        return new DatabaseService1();
    }
    //======================================================================================================================

    @Conditional({Datasource2Condition.class})
    @Bean("sqlSessionFactory2")
    public SqlSessionFactoryBean sqlSessionFactory2(@Qualifier(value = "dataSource2") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Conditional({Datasource2Condition.class})
    @Bean("mapperScannerConfigurer2")
    public MapperScannerConfigurer mapperScannerConfigurer2() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory2");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper2");
        return mapperScannerConfigure;
    }

    @Conditional({Datasource2Condition.class})
    @Bean("databaseService2")
    public DatabaseService2 databaseService2() {
        return new DatabaseService2();
    }
    //======================================================================================================================

    @Conditional({Datasource3Condition.class})
    @Bean("sqlSessionFactory3")
    public SqlSessionFactoryBean sqlSessionFactory3(@Qualifier(value = "dataSource3") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Conditional({Datasource3Condition.class})
    @Bean("mapperScannerConfigurer3")
    public MapperScannerConfigurer mapperScannerConfigurer3() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory3");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper3");
        return mapperScannerConfigure;
    }

    @Conditional({Datasource3Condition.class})
    @Bean("databaseService3")
    public DatabaseService3 databaseService3() {
        return new DatabaseService3();
    }
    //======================================================================================================================

    @Conditional({Datasource4Condition.class})
    @Bean("sqlSessionFactory4")
    public SqlSessionFactoryBean sqlSessionFactory4(@Qualifier(value = "dataSource4") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Conditional({Datasource4Condition.class})
    @Bean("mapperScannerConfigurer4")
    public MapperScannerConfigurer mapperScannerConfigurer4() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory4");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper4");
        return mapperScannerConfigure;
    }

    @Conditional({Datasource4Condition.class})
    @Bean("databaseService4")
    public DatabaseService4 databaseService4() {
        return new DatabaseService4();
    }

    private SqlSessionFactoryBean getSqlSessionFactory(DataSource dataSource) {
        final org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setEnvironment(new Environment("development",new JdbcTransactionFactory(), dataSource));
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
    }
}
