package com.kipa.dubbo.excute;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.kipa.core.ClientFactory;
import com.kipa.dubbo.enums.RegisterType;
import com.kipa.utils.PreCheckUtils;

/**
 * @author: Qinyadong
 * @date: 2019/4/3 18:44
 * 引用配置的生成器 ReferenceConfig
 */
public class ReferenceConfigClientFactory implements ClientFactory<ReferenceConfig<GenericService>, DubboProperties> {

    @Override
    public ReferenceConfig<GenericService> create(DubboProperties properties) throws Exception {
        return build(properties);
    }

    /**
     * 构建泛化的服务消费引用类
     * @param dubboProperties dubbo的配置属性对象
     * @return 响应的结果信息
     */
    private ReferenceConfig<GenericService> build(DubboProperties dubboProperties) {
        PreCheckUtils.checkEmpty(dubboProperties, "dubbo的配置信息不能为空");
        PreCheckUtils.checkEmpty(dubboProperties.getApplicationName(), "dubbo消费端的应用名称不能为空");
        final ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        //1.
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboProperties.getApplicationName());
        applicationConfig.setOrganization(dubboProperties.getApplicationOrganization());
        applicationConfig.setOwner(dubboProperties.getApplicationOwner());

        referenceConfig.setApplication(applicationConfig);
        String address = dubboProperties.getAddress();
        PreCheckUtils.checkEmpty(address,"注册中心的地址为空");
        RegistryConfig registryConfig = null;
        String registerProtocol = dubboProperties.getRegisterProtocol();
        PreCheckUtils.checkEmpty(registerProtocol, "注册协议不能为空");
        String registerGroup = dubboProperties.getRegisterGroup();
        String rpcProtocol = dubboProperties.getRpcProtocol();
        RegisterType registerType = RegisterType.getByMessage(registerProtocol);
        if (registerType == null) {
            throw new IllegalArgumentException("注册协议类型不存在");
        }
        switch (registerType) {
            case REGISTRY_ZOOKEEPER:
                registryConfig = new RegistryConfig();
                registryConfig.setProtocol(RegisterType.REGISTRY_ZOOKEEPER.getMessage());
                registryConfig.setAddress(address);
                registryConfig.setGroup(registerGroup);
                referenceConfig.setRegistry(registryConfig);
                referenceConfig.setProtocol(rpcProtocol);
                break;
            case REGISTRY_REDIS:
                registryConfig = new RegistryConfig();
                registryConfig.setProtocol(RegisterType.REGISTRY_REDIS.getMessage());
                registryConfig.setAddress(address);
                registryConfig.setGroup(registerGroup);
                referenceConfig.setRegistry(registryConfig);
                referenceConfig.setProtocol(rpcProtocol);
                break;
            case REGISTRY_MULTICAST:
                registryConfig = new RegistryConfig();
                registryConfig.setProtocol(RegisterType.REGISTRY_MULTICAST.getMessage());
                registryConfig.setAddress(address);
                registryConfig.setGroup(registerGroup);
                referenceConfig.setRegistry(registryConfig);
                referenceConfig.setProtocol(rpcProtocol);
                break;
            case REGISTRY_SIMPLE:
                registryConfig = new RegistryConfig();
                registryConfig.setAddress(address);
                referenceConfig.setRegistry(registryConfig);
                referenceConfig.setProtocol(rpcProtocol);
                break;
            case DIRECTED_LINK:
                break;
            default:
                break;
        }
        referenceConfig.setRetries(dubboProperties.getRetries());
        referenceConfig.setCluster(dubboProperties.getCluster());
        referenceConfig.setVersion(dubboProperties.getVersion());
        referenceConfig.setTimeout(dubboProperties.getTimeout());
        referenceConfig.setGroup(dubboProperties.getGroup());
        referenceConfig.setConnections(dubboProperties.getConnections());
        referenceConfig.setLoadbalance(dubboProperties.getLoadBalance());
        referenceConfig.setFilter("mockDubboConsumerFilter");
        return referenceConfig;
    }
}
