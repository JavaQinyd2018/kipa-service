package com.kipa.core;

/**
 * 调用对象创建的工厂类
 * @param <C> client 实体对象
 * @param <P> properties 属性文件对象
 */
public interface ClientFactory<C,P> {

    /**
     * 创建一个 client实体
     * @param properties 属性文件队形
     * @return
     */
    C create(P properties) throws Exception;
}
