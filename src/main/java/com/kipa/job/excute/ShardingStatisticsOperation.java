package com.kipa.job.excute;

import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.kipa.job.entity.ShardingInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qinyadong
 */
public final class ShardingStatisticsOperation {

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    public ShardingStatisticsOperation(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }

    public Collection<ShardingInfo> getShardingInfo(String jobName) {
        String shardingRootPath = (new JobNodePath(jobName)).getShardingNodePath();
        List<String> items = this.coordinatorRegistryCenter.getChildrenKeys(shardingRootPath);
        if (CollectionUtils.isNotEmpty(items)) {
            return items.stream().map(item -> this.getShardingInfo(jobName, item))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private ShardingInfo getShardingInfo(String jobName, String item) {
        ShardingInfo shardingInfo = new ShardingInfo();
        shardingInfo.setItem(Integer.parseInt(item));
        JobNodePath jobNodePath = new JobNodePath(jobName);
        String instanceId = this.coordinatorRegistryCenter.get(jobNodePath.getShardingNodePath(item, "instance"));
        boolean disabled = this.coordinatorRegistryCenter.isExisted(jobNodePath.getShardingNodePath(item, "disabled"));
        boolean running = this.coordinatorRegistryCenter.isExisted(jobNodePath.getShardingNodePath(item, "running"));
        boolean shardingError = !this.coordinatorRegistryCenter.isExisted(jobNodePath.getInstanceNodePath(instanceId));
        shardingInfo.setStatus(ShardingInfo.ShardingStatus.getShardingStatus(disabled, running, shardingError));
        shardingInfo.setFailover(this.coordinatorRegistryCenter.isExisted(jobNodePath.getShardingNodePath(item, "failover")));
        if (StringUtils.isNotBlank(instanceId)) {
            String[] ipAndPid = instanceId.split("@-@");
            shardingInfo.setServerIp(ipAndPid[0]);
            shardingInfo.setInstanceId(ipAndPid[1]);
        }
        return shardingInfo;
    }
}
