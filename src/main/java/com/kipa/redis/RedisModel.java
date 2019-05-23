package com.kipa.redis;

/**
 * @author: Qinyadong
 * @date: 2019/4/24 15:27
 * 集群还是单机
 */
public enum  RedisModel {
    /**
     * 单机版
     */
    STAND_ALONE,
    /**
     * 集群版
     */
    CLUSTER;
}
