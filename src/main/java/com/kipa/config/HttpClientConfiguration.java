package com.kipa.config;

import com.kipa.common.DataConstant;
import com.kipa.env.HttpContextHolder;
import com.kipa.http.excute.OkHttpClientProperties;
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
 * @Date: 2019/4/6
 * http整合spring配置文件
 */
@Configuration
@PropertySource(value = {"classpath:http/httpclient.properties"})
public class HttpClientConfiguration {

    @Value("${okhttp.client.connectTimeout}")
    private long connectTimeout;

    @Value("${okhttp.client.readTimeout}")
    private long readTimeout;

    @Value("${okhttp.client.writeTimeout}")
    private long writeTimeout;

    @Value("${okhttp.client.followRedirects}")
    private boolean followRedirects;

    @Value("${okhttp.client.retry}")
    private boolean retry;

    @Value("${okhttp.client.verifySSLCertificate}")
    private boolean verifySSLCertificate;

    @Value("${okhttp.client.certificatePath}")
    private String certificatePath;

    @Value("${okhttp.client.keyStorePath}")
    private String keyStorePath;

    @Value("${okhttp.client.keyStorePass}")
    private String keyStorePass;

    @Value("${okhttp.client.maxIdleConnections}")
    private int maxIdleConnections;

    @Value("${okhttp.client.keepAliveDuration}")
    private long keepAliveDuration;

    @PostConstruct
    public void init() {
        String flag = HttpContextHolder.getFlag();
        Properties properties = PropertiesUtils.loadProperties(DataConstant.CONFIG_FILE);
        verifySSLCertificate = Boolean.parseBoolean(PropertiesUtils.getProperty(properties, flag,"okhttp.client.verifySSLCertificate"));
        certificatePath = PropertiesUtils.getProperty(properties, flag, "okhttp.client.certificatePath");
        keyStorePass = PropertiesUtils.getProperty(properties, flag,"okhttp.client.keyStorePass");
        keyStorePath = PropertiesUtils.getProperty(properties, flag, "okhttp.client.keyStorePath");
        maxIdleConnections = Integer.parseInt(PropertiesUtils.getProperty(properties,flag, "okhttp.client.maxIdleConnections"));
        keepAliveDuration = Integer.parseInt(PropertiesUtils.getProperty(properties, flag, "okhttp.client.keepAliveDuration"));
    }

    @Bean
    public OkHttpClientProperties okHttpClientProperties() {
        OkHttpClientProperties properties = new OkHttpClientProperties();
        properties.setReadTimeout(readTimeout);
        properties.setWriteTimeout(writeTimeout);
        properties.setConnectTimeout(connectTimeout);
        properties.setFollowRedirects(followRedirects);
        properties.setRetry(retry);
        properties.setVerifySSLCertificate(verifySSLCertificate);
        properties.setCertificatePath(certificatePath);
        properties.setKeyStorePass(keyStorePass);
        properties.setKeyStorePath(keyStorePath);
        properties.setMaxIdleConnections(maxIdleConnections);
        properties.setKeepAliveDuration(keepAliveDuration);
        return properties;
    }

    @PreDestroy
    public void destroy() {
        HttpContextHolder.removeFlag();
    }
 }
