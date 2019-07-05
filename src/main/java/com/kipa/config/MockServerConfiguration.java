package com.kipa.config;

import com.kipa.common.KipaProcessException;
import com.kipa.mock.dubbo.MockDubboConfig;
import com.kipa.mock.http.entity.MockServerConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 18:59
 * Mock server整合spring配置信息
 */
@Configuration
@PropertySource(value = "classpath:mock/mock-server.properties")
public class MockServerConfiguration {

    private static final String MOCK_CONFIG = "config/mockserver.properties";

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

    @PostConstruct
    private void init() {
            try {
                Properties properties = PropertiesLoaderUtils.loadAllProperties(MOCK_CONFIG);
                String host = (String) properties.get("mock.server.remote.host");
                remoteHost = StringUtils.isBlank(host) ? null : host;
                String portStr = (String) properties.get("mock.server.remote.port");
                remotePort = StringUtils.isBlank(portStr) ? null : Integer.valueOf(portStr);
                String localPortStr = properties.getProperty("mock.server.local.port");
                if (StringUtils.isNotBlank(localPortStr)) {
                    localPort = Integer.valueOf(localPortStr);
                }

                //mock dubbo的配置
                String protocol = properties.getProperty("mock.server.dubbo.protocol");
                if (StringUtils.isNotBlank(protocol)) {
                    registerProtocol = protocol;
                }
                String mockAddress = properties.getProperty("mock.server.dubbo.address");
                if (StringUtils.isNotBlank(mockAddress)) {
                    this.address = mockAddress;
                }
            } catch (IOException e) {
                throw new KipaProcessException(e);
            }
    }

    @Bean
    public MockServerConfig mockServerConfig() {
        MockServerConfig config = new MockServerConfig();
        init();
        config.setRemoteHost(remoteHost);
        config.setRemotePort(remotePort);
        config.setLocalPort(localPort);
        return config;
    }

    @Bean
    public MockDubboConfig mockDubboConfig() {
        MockDubboConfig dubboConfig = new MockDubboConfig();
        dubboConfig.setRegisterProtocol(registerProtocol);
        dubboConfig.setAddress(address);
        dubboConfig.setRegisterTimeout(registerTimeout);

        dubboConfig.setApplicationName(applicationName);
        dubboConfig.setApplicationOwner(applicationOwner);
        dubboConfig.setApplicationOrganization(applicationOrganization);
        return dubboConfig;
    }
}
