package com.kipa.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
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


/**
 * @author: Qinyadong
 * @date: 2019/3/21 11:24
 * mybatis 整合spring配置文件
 */
@Configuration
/**
 *启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
 */
@EnableTransactionManagement
public class MybatisConfiguration {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DruidDataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "dataSource") DruidDataSource dataSource) {
        final org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setCacheEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setEnvironment(new Environment("development",new JdbcTransactionFactory(), dataSource));
        return new DefaultSqlSessionFactory(configuration);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") DruidDataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        final MapperScannerConfigurer mapperScannerConfigure = new MapperScannerConfigurer();
        mapperScannerConfigure.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigure.setBasePackage("com.kipa.mybatis.mapper");
        return mapperScannerConfigure;
    }
}
