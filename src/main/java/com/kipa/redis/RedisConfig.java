package com.kipa.redis;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Qinyadong
 * @date: 2019/4/24 15:50
 */
@Data
@NoArgsConstructor
public class RedisConfig {
    /**
     * redis集群信息集合
     */
    private List<String> list;
    /**
     * 集群模式密码
     */
    private String clusterPassword;
    /**
     * 单机host
     */
    private String standAloneHost;
    /**
     * 单机端口
     */
    private Integer standAlonePort;
    /**
     * 单机密码
     */
    private String standAlonePassword;
}
