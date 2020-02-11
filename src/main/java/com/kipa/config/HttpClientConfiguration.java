package com.kipa.config;

import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.http.excute.OkHttpClientProperties;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;

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

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    private OkHttpClientProperties initOkHttpClientProperties;

    @PostConstruct
    public void init() {
        initOkHttpClientProperties = new OkHttpClientProperties();
        initOkHttpClientProperties.setVerifySSLCertificate(PropertiesUtils.getBooleanProperty(globalEnvironmentProperties,"okhttp.client.verifySSLCertificate"));
        initOkHttpClientProperties.setCertificatePath(PropertiesUtils.getProperty(globalEnvironmentProperties, "okhttp.client.certificatePath"));
        initOkHttpClientProperties.setKeyStorePass(PropertiesUtils.getProperty(globalEnvironmentProperties,"okhttp.client.keyStorePass"));
        initOkHttpClientProperties.setMaxIdleConnections(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "okhttp.client.maxIdleConnections"));
        initOkHttpClientProperties.setKeepAliveDuration(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "okhttp.client.keepAliveDuration"));
    }

    @Bean
    public OkHttpClientProperties okHttpClientProperties() {
        OkHttpClientProperties properties = new OkHttpClientProperties();
        properties.setReadTimeout(readTimeout);
        properties.setWriteTimeout(writeTimeout);
        properties.setConnectTimeout(connectTimeout);
        properties.setFollowRedirects(followRedirects);
        properties.setRetry(retry);
        properties.setVerifySSLCertificate(initOkHttpClientProperties.isVerifySSLCertificate());
        properties.setCertificatePath(StringUtils.isNotBlank(initOkHttpClientProperties.getCertificatePath()) ? initOkHttpClientProperties.getCertificatePath() : certificatePath);
        properties.setKeyStorePass(StringUtils.isNotBlank(initOkHttpClientProperties.getKeyStorePass()) ? initOkHttpClientProperties.getKeyStorePass(): keyStorePass);
        properties.setKeyStorePath(StringUtils.isNotBlank(initOkHttpClientProperties.getCertificatePath())? initOkHttpClientProperties.getKeyStorePath() : keyStorePath);
        properties.setMaxIdleConnections(initOkHttpClientProperties.getMaxIdleConnections() != 0 ? initOkHttpClientProperties.getMaxIdleConnections(): maxIdleConnections);
        properties.setKeepAliveDuration(initOkHttpClientProperties.getKeepAliveDuration() != 0 ? initOkHttpClientProperties.getKeepAliveDuration(): keepAliveDuration);
        return properties;
    }
 }
