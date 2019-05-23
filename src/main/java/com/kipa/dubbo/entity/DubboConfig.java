package com.kipa.dubbo.entity;

import lombok.Data;

/**
 * @author: Qinyadong
 * @date: 2019/4/2 16:32
 * dubbo 消费端配置信息的包装类
 */
@Data
public class DubboConfig {

    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 应用所有者
     * 调用服务负责人，用于服务治理，请填写负责人公司邮箱前缀
     */
    private String applicationOwner;
    /**
     * 应用所属组织
     */
    private String applicationOrganization;

    //2.连接注册中心配置
    /**
     * 注册中心地址
     * 格式：host+端口
     */
    private String address;
    /**
     * 注册协议
     * 注册中心地址协议，支持dubbo, http, local三种协议，分别表示：dubbo地址、http地址、本地注册中心
     */
    private String registerProtocol;
    /**
     * 注册中心所属组
     */
    private String registerGroup;
    /**
     * rpc 协议--RegisterType这个枚举
     */
    private String rpcProtocol;

    /**
     * 注册中心请求超时时间(毫秒)
     */
    private int registerTimeout;


    //3.引用远程服务
    /**
     * dubbo、http、local
     */
    private String protocol;
    /**
     * 可选
     * 服务版本，与服务提供者的版本一致
     */
    private String version;
    /**
     *远程服务调用重试次数
     * 不需要的话设置为0
     */
    private int retries;
    /**
     *集群方式，可选：failover/failfast/failsafe/failback/forking
     */
    private String cluster;

    /**
     * 服务分组，当一个接口有多个实现，可以用分组区分，必需和服务提供方一致
     */
    private String group;
    /**
     * 对每个提供者的最大连接数
     */
    private int connections;
    /**
     * 负载均衡策略
     * random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用
     */
    private String loadBalance;

    /**
     * 启动时检查提供者是否存在，true报错，false忽略
     * check
     */
    private boolean check;

    /**
     * 接口引用超时时间
     */
    private int timeout;
}
