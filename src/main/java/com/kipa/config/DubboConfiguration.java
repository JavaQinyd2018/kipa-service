package com.kipa.config;

import com.kipa.common.DataConstant;
import com.kipa.dubbo.entity.DubboConfig;
import com.kipa.env.DubboContextHolder;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Properties;

/**
 * @Author Yadong Qin
 * @Date 2019/4/6
 * dubbo整合spring配置文件
 */
@Configuration
@PropertySource(value = {"classpath:dubbo/dubbo-consumer.properties"})
public class DubboConfiguration {

    @Value("${dubbo.consumer.application.name}")
    private String applicationName;
    @Value("${dubbo.consumer.application.owner}")
    private String applicationOwner;
    @Value("${dubbo.consumer.application.organization}")
    private String applicationOrganization;

    private String address;
    private String registerProtocol;

    @Value("${dubbo.consumer.register.group}")
    private String registerGroup;

    @Value("${dubbo.consumer.register.timeout}")
    private int registerTimeout;

    @Value("${dubbo.comsumer.reference.protocol}")
    private String rpcProtocol;

    @Value("${dubbo.comsumer.reference.protocol}")
    private String protocol;
    @Value("${dubbo.consumer.reference.version}")
    private String version;

    @Value("${dubbo.consumer.reference.retries}")
    private int retries;

    @Value("${dubbo.consumer.reference.cluster}")
    private String cluster;

    @Value("${dubbo.consumer.reference.group}")
    private String group;

    @Value("${dubbo.consumer.reference.connections}")
    private int connections;

    @Value("${dubbo.comsumer.reference.loadBalance}")
    private String loadBalance;

    @Value("${dubbo.comsumer.reference.check}")
    private boolean check;

    @Value("${dubbo.consumer.reference.timeout}")
    private int timeout;

    @PostConstruct
    public void init() {
        Map<String, Object> config = DubboContextHolder.getConfig();
        String flag = null;
        if (MapUtils.isNotEmpty(config)) {
            flag = (String) config.get("flag");
            version = (String) config.get("version");
            timeout = (Integer) config.get("timeout");
        }

        Properties properties = PropertiesUtils.loadProperties(DataConstant.CONFIG_FILE);
        if (StringUtils.isNotBlank(flag)) {
            registerProtocol = PropertiesUtils.getProperty(properties, flag,"dubbo.consumer.register.protocol");
            address = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.register.address");
            registerGroup = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.register.group");
            registerTimeout = Integer.valueOf(PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.register.timeout"));

            protocol = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.protocol");
            version = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.version");
            retries = Integer.valueOf(PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.retries"));
            cluster = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.cluster");
            timeout = Integer.valueOf(PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.timeout"));
            connections = Integer.valueOf(PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.connections"));
            loadBalance = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.loadBalance");
            check = Boolean.valueOf(PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.reference.check"));
        }else {
            registerProtocol = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.register.protocol");
            address = PropertiesUtils.getProperty(properties, flag, "dubbo.consumer.register.address");
        }
        PreCheckUtils.checkEmpty(address, "注册中心地址或者目标服务地址不能为空");
    }

    @Bean("dubboConfig")
    public DubboConfig dubboConfig() {
        DubboConfig dubboConfig = new DubboConfig();
        dubboConfig.setApplicationName(applicationName);
        dubboConfig.setApplicationOrganization(applicationOrganization);
        dubboConfig.setApplicationOwner(applicationOwner);
        dubboConfig.setRegisterProtocol(registerProtocol);
        dubboConfig.setRegisterGroup(group);
        dubboConfig.setAddress(address);
        dubboConfig.setRegisterTimeout(registerTimeout);
        dubboConfig.setRpcProtocol(rpcProtocol);
        dubboConfig.setProtocol(protocol);
        dubboConfig.setVersion(version);
        dubboConfig.setRetries(retries);
        dubboConfig.setCluster(cluster);
        dubboConfig.setGroup(group);
        dubboConfig.setConnections(connections);
        dubboConfig.setLoadBalance(loadBalance);
        dubboConfig.setCheck(check);
        dubboConfig.setTimeout(timeout);
        return dubboConfig;
    }

    @PreDestroy
    public void destroy() {
        DubboContextHolder.removeConfig();
    }
}
