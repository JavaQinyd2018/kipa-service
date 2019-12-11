package com.kipa.mock.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:59
 * dubbo 接口的注册器
 */
@Service("mockDubboServiceRegister")
public class MockDubboServiceRegister {

    public void exportService(MockDubboProperties config, String interfaceName, GenericService genericService) {
        ServiceConfig<GenericService> serviceConfig = new ServiceConfig<>();
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(config.getApplicationName());
        applicationConfig.setOwner(config.getApplicationOwner());
        applicationConfig.setOrganization(config.getApplicationOrganization());

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(config.getAddress());
        registryConfig.setProtocol(config.getRegisterProtocol());
        registryConfig.setTimeout(config.getRegisterTimeout());

        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setInterface(interfaceName);
        serviceConfig.setRef(genericService);
        serviceConfig.setGeneric("true");
        serviceConfig.setFilter("mockDubboProviderFilter");
        serviceConfig.export();
    }
}
