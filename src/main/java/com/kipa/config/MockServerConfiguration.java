package com.kipa.config;

import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.mock.dubbo.MockDubboProperties;
import com.kipa.mock.http.entity.MockServerProperties;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 18:59
 * Mock server整合spring配置信息
 */
@Configuration
@PropertySource(value = "classpath:mock/mock-server.properties")
public class MockServerConfiguration {

    @Value("${mock.server.remote.host}")
    private String remoteHost;

    @Value("${mock.server.remote.port}")
    private Integer remotePort;

    @Value("${mock.server.local.port}")
    private Integer localPort;

    /**
     * mock dubbo的配置
     */
    @Value("${mock.server.dubbo.applicationName}")
    private String applicationName;

    @Value("${mock.server.dubbo.applicationOwner}")
    private String applicationOwner;

    @Value("${mock.server.dubbo.applicationOrganization}")
    private String applicationOrganization;

    @Value("${mock.server.dubbo.address}")
    private String address;

    @Value("${mock.server.dubbo.protocol}")
    private String registerProtocol;

    @Value("${mock.server.dubbo.timeout}")
    private int registerTimeout;

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    private MockServerProperties initMockServerProperties;

    private MockDubboProperties initMockDubboProperties;

    @PostConstruct
    private void init() {
        //http mock配置信息
        initMockServerProperties = new MockServerProperties();
        initMockServerProperties.setRemoteHost(PropertiesUtils.getProperty(globalEnvironmentProperties, "mock.server.remote.host"));
        initMockServerProperties.setRemotePort(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "mock.server.remote.port"));
        initMockServerProperties.setLocalPort(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties,"mock.server.local.port"));

        //mock dubbo的配置
        initMockDubboProperties = new MockDubboProperties();
        initMockDubboProperties.setRegisterProtocol(PropertiesUtils.getProperty(globalEnvironmentProperties,"mock.server.dubbo.protocol"));
        initMockDubboProperties.setAddress(PropertiesUtils.getProperty(globalEnvironmentProperties,"mock.server.dubbo.address"));
    }

    @Bean
    public MockServerProperties mockServerProperties() {
        MockServerProperties properties = new MockServerProperties();
        properties.setRemoteHost(StringUtils.isNotBlank(initMockServerProperties.getRemoteHost()) ? initMockServerProperties.getRemoteHost() : remoteHost);
        properties.setRemotePort(initMockServerProperties.getRemotePort() != 0 ? initMockServerProperties.getRemotePort() : remotePort);
        properties.setLocalPort(initMockServerProperties.getLocalPort() != 0 ? initMockServerProperties.getLocalPort() : localPort);
        return properties;
    }

    @Bean
    public MockDubboProperties mockDubboProperties() {
        MockDubboProperties dubboProperties = new MockDubboProperties();
        dubboProperties.setRegisterProtocol(StringUtils.isNotBlank(initMockDubboProperties.getRegisterProtocol()) ? initMockDubboProperties.getRegisterProtocol(): registerProtocol);
        dubboProperties.setAddress(StringUtils.isNotBlank(initMockDubboProperties.getAddress()) ? initMockDubboProperties.getAddress(): address);
        dubboProperties.setRegisterTimeout(registerTimeout);

        dubboProperties.setApplicationName(applicationName);
        dubboProperties.setApplicationOwner(applicationOwner);
        dubboProperties.setApplicationOrganization(applicationOrganization);
        return dubboProperties;
    }
}
