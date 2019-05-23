package com.kipa.dubbo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: Qinyadong
 * @date: 2019/4/3 17:20
 */
@AllArgsConstructor
@Getter
public enum RegisterType {

    /**
     * 注册中心--zookeeper
     */
     REGISTRY_ZOOKEEPER("zookeeper"),

    /**
     *广播订阅
     */
    REGISTRY_MULTICAST("multicast"),

    /**
     * 注册中心-redis
     */
    REGISTRY_REDIS("redis"),

    /**
     * Simple注册中心
     * Dogfooding
     * 注册中心本身就是一个普通的Dubbo服务，可以减少第三方依赖，使整体通讯方式一致。
     * 适用性说明
     * 此SimpleRegistryService只是简单实现，不支持集群，可作为自定义注册中心的参考，但不适合直接用于生产环境。
     */
    REGISTRY_SIMPLE("simple"),

    /**
     * 不用注册中心，直连服务
     */
    DIRECTED_LINK("direct");


    public static RegisterType getByMessage(String message) {
        RegisterType[] values = RegisterType.values();
        for (RegisterType value : values) {
            if (StringUtils.equalsIgnoreCase(value.message, message)) {
                return value;
            }
        }
        return null;
    }
    private String message;
}
