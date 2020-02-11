package com.kipa.config;

import com.kipa.dubbo.excute.DubboProperties;
import com.kipa.env.GlobalEnvironmentProperties;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;

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

//    private String address;
//    private String registerProtocol;

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

    @Autowired
    private GlobalEnvironmentProperties globalEnvironmentProperties;

    private DubboProperties initDubboProperties;

    @PostConstruct
    public void init() {
        initDubboProperties = new DubboProperties();
        //1. todo 需要重新做优化
        initDubboProperties.setRegisterProtocol(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.register.protocol"));
        initDubboProperties.setAddress(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.register.address"));
        initDubboProperties.setGroup(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.register.group"));
        initDubboProperties.setRegisterTimeout(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "dubbo.consumer.register.timeout"));
        //2. 协议相关信息
        initDubboProperties.setProtocol(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.reference.protocol"));
        initDubboProperties.setVersion(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.reference.version"));
        initDubboProperties.setRetries(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "dubbo.consumer.reference.retries"));;
        initDubboProperties.setCluster(PropertiesUtils.getProperty(globalEnvironmentProperties,"dubbo.consumer.reference.cluster"));
        initDubboProperties.setTimeout(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties,  "dubbo.consumer.reference.timeout"));
        initDubboProperties.setConnections(PropertiesUtils.getIntegerProperty(globalEnvironmentProperties, "dubbo.consumer.reference.connections"));
        initDubboProperties.setLoadBalance(PropertiesUtils.getProperty(globalEnvironmentProperties, "dubbo.consumer.reference.loadBalance"));
        initDubboProperties.setCheck(PropertiesUtils.getBooleanProperty(globalEnvironmentProperties,"dubbo.consumer.reference.check"));
    }

    @Bean("dubboProperties")
    public DubboProperties dubboProperties() {
        DubboProperties dubboProperties = new DubboProperties();
        dubboProperties.setApplicationName(applicationName);
        dubboProperties.setApplicationOrganization(applicationOrganization);
        dubboProperties.setApplicationOwner(applicationOwner);
        dubboProperties.setRegisterProtocol(initDubboProperties.getRegisterProtocol());
        dubboProperties.setRegisterGroup(StringUtils.isNotBlank(initDubboProperties.getGroup()) ? initDubboProperties.getGroup() : group);
        dubboProperties.setAddress(initDubboProperties.getAddress());
        dubboProperties.setRegisterTimeout(initDubboProperties.getRegisterTimeout() != 0 ? initDubboProperties.getRegisterTimeout(): registerTimeout);
        dubboProperties.setRpcProtocol(StringUtils.isNotBlank(initDubboProperties.getRpcProtocol()) ? initDubboProperties.getRpcProtocol(): rpcProtocol);
        dubboProperties.setProtocol(StringUtils.isNotBlank(initDubboProperties.getProtocol()) ? initDubboProperties.getProtocol() : protocol);
        dubboProperties.setVersion(initDubboProperties.getVersion() != null ? initDubboProperties.getVersion() : version);
        dubboProperties.setRetries(initDubboProperties.getRetries() != 0 ? initDubboProperties.getRetries(): retries);
        dubboProperties.setCluster(StringUtils.isNotBlank(initDubboProperties.getCluster()) ? initDubboProperties.getCluster(): cluster);
        dubboProperties.setGroup(StringUtils.isNotBlank(initDubboProperties.getGroup())? initDubboProperties.getGroup(): group);
        dubboProperties.setConnections(initDubboProperties.getConnections() != 0 ? initDubboProperties.getConnections(): connections);
        dubboProperties.setLoadBalance(StringUtils.isNotBlank(initDubboProperties.getLoadBalance()) ? initDubboProperties.getLoadBalance(): loadBalance);
        dubboProperties.setCheck(initDubboProperties.isCheck());
        dubboProperties.setTimeout(initDubboProperties.getTimeout() != 0 ? initDubboProperties.getTimeout() : timeout);
        return dubboProperties;
    }
}
