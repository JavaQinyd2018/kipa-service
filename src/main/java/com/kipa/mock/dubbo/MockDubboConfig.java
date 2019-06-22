package com.kipa.mock.dubbo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qinyadong
 * @date: 2019/5/29 14:49
 * dubbo 接口的mock配置
 */
@Data
@NoArgsConstructor
public class MockDubboConfig {

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
     * rpc 协议--RegisterType这个枚举
     */
    private String rpcProtocol;

    /**
     * 注册中心请求超时时间(毫秒)
     */
    private int registerTimeout;


}
