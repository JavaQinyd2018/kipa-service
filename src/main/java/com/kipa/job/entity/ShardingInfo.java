package com.kipa.job.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qinyadong
 * 分片信息
 */
@Data
public class ShardingInfo implements Serializable,Comparable<ShardingInfo> {

    private int item;
    private String serverIp;
    private String instanceId;
    private ShardingInfo.ShardingStatus status;
    private boolean failover;

    public enum ShardingStatus{
        DISABLED,
        RUNNING,
        SHARDING_ERROR,
        PENDING;

        public static ShardingInfo.ShardingStatus getShardingStatus(boolean isDisabled, boolean isRunning, boolean isShardingError) {
            if (isDisabled) {
                return DISABLED;
            } else if (isRunning) {
                return RUNNING;
            } else {
                return isShardingError ? SHARDING_ERROR : PENDING;
            }
        }
    }

    @Override
    public int compareTo(ShardingInfo o) {
        return this.getItem() - o.getItem();
    }
}
