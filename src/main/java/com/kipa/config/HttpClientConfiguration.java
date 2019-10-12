package com.kipa.config;

import com.kipa.common.DataConstant;
import com.kipa.env.HttpContextHolder;
import com.kipa.http.core.OkHttpClientConfig;
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
        verifySSLCertificate = Boolean.valueOf(PropertiesUtils.getProperty(properties, flag,"okhttp.client.verifySSLCertificate"));
        certificatePath = PropertiesUtils.getProperty(properties, flag, "okhttp.client.certificatePath");
        keyStorePass = PropertiesUtils.getProperty(properties, flag,"okhttp.client.keyStorePass");
        keyStorePath = PropertiesUtils.getProperty(properties, flag, "okhttp.client.keyStorePath");
        maxIdleConnections = Integer.valueOf(PropertiesUtils.getProperty(properties,flag, "okhttp.client.maxIdleConnections"));
        keepAliveDuration = Integer.valueOf(PropertiesUtils.getProperty(properties, flag, "okhttp.client.keepAliveDuration"));
    }

    @Bean
    public OkHttpClientConfig okHttpClientConfig() {
        OkHttpClientConfig clientConfig = new OkHttpClientConfig();
        clientConfig.setReadTimeout(readTimeout);
        clientConfig.setWriteTimeout(writeTimeout);
        clientConfig.setConnectTimeout(connectTimeout);
        clientConfig.setFollowRedirects(followRedirects);
        clientConfig.setRetry(retry);
        clientConfig.setVerifySSLCertificate(verifySSLCertificate);
        clientConfig.setCertificatePath(certificatePath);
        clientConfig.setKeyStorePass(keyStorePass);
        clientConfig.setKeyStorePath(keyStorePath);
        clientConfig.setMaxIdleConnections(maxIdleConnections);
        clientConfig.setKeepAliveDuration(keepAliveDuration);
        return clientConfig;
    }

    @PreDestroy
    public void destroy() {
        HttpContextHolder.removeFlag();
    }
 }
