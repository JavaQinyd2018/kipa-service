package com.kipa.config;

import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.job.entity.ElasticJobProperties;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:job/elastic-job.properties")
@ComponentScan("com.kipa.job.service")
public class ElasticJobConfiguration {

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    @Value("${elastic.job.zk.host}")
    private String zkAddress;

    @Value("${elastic.job.zk.digest}")
    private String digest;

    /**
     * 初始化包装类
     */
    private ElasticJobProperties initElasticJobProperties;

    @PostConstruct
    public void init() {
        initElasticJobProperties = new ElasticJobProperties();
        initElasticJobProperties.setZkAddressList(PropertiesUtils.getProperty(globalEnvironmentProperties, "elastic.job.zk.host"));
        initElasticJobProperties.setDigest(PropertiesUtils.getProperty(globalEnvironmentProperties, "elastic.job.zk.digest"));
    }


    @Bean
    public ElasticJobProperties elasticJobProperties() {
        ElasticJobProperties elasticJobProperties = new ElasticJobProperties();
        elasticJobProperties.setZkAddressList(StringUtils.isNotBlank(initElasticJobProperties.getZkAddressList())? initElasticJobProperties.getZkAddressList(): zkAddress);
        elasticJobProperties.setDigest(initElasticJobProperties.getDigest());
        PreCheckUtils.checkEmpty(elasticJobProperties.getZkAddressList(), "定时任务注册中心地址不能为空");
        return elasticJobProperties;
    }
}
