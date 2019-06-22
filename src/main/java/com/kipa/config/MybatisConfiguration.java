package com.kipa.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:24
 * mybatis 整合spring配置文件
 */
@Configuration
/**
 *启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
 */
//@EnableTransactionManagement
public class MybatisConfiguration {

/*
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DruidDataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
*/

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier(value = "dataSource") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper");
        return mapperScannerConfigure;
    }

//======================================================================================================================

/*    @Bean(name = "transactionManager1")
    public PlatformTransactionManager transactionManager1(@Qualifier("dataSource1") DruidDataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }*/

    @Bean("sqlSessionFactory1")
    public SqlSessionFactoryBean sqlSessionFactory1(@Qualifier(value = "dataSource1") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Bean("mapperScannerConfigurer1")
    public MapperScannerConfigurer mapperScannerConfigurer1() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory1");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper1");
        return mapperScannerConfigure;
    }

    //======================================================================================================================

/*    @Bean(name = "transactionManager2")
    public PlatformTransactionManager transactionManager2(@Qualifier("dataSource2") DruidDataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }*/

    @Bean("sqlSessionFactory2")
    public SqlSessionFactoryBean sqlSessionFactory2(@Qualifier(value = "dataSource2") DruidDataSource dataSource) {
        return getSqlSessionFactory(dataSource);
    }

    @Bean("mapperScannerConfigurer2")
    public MapperScannerConfigurer mapperScannerConfigurer2() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory2");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.dao.mapper2");
        return mapperScannerConfigure;
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
