package com.kipa.job.excute;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.kipa.job.entity.JobBriefInfo;
import com.kipa.job.entity.JobSettings;
import com.kipa.job.entity.ServerBriefInfo;
import com.kipa.job.entity.ShardingInfo;
import com.kipa.job.excute.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;

/**
 * @author qinyadong
 */
public final class BaseJobOperation implements InitializingBean {

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    private JobActionOperation jobActionOperation;
    private JobSettingsOperation jobSettingsOperation;
    private JobStatisticsOperation jobStatisticsOperation;
    private ServerStatisticsOperation serverStatisticsOperation;
    private ShardingStatisticsOperation shardingStatisticsOperation;

    public BaseJobOperation(String zkAddress, String digest, String namespace) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(zkAddress, namespace);
        if (StringUtils.isNotBlank(digest)) {
            zookeeperConfiguration.setDigest(digest);
        }
        coordinatorRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        coordinatorRegistryCenter.init();
        jobActionOperation = new JobActionOperation(coordinatorRegistryCenter);
        jobSettingsOperation = new JobSettingsOperation(coordinatorRegistryCenter);
        jobStatisticsOperation = new JobStatisticsOperation(coordinatorRegistryCenter);
        serverStatisticsOperation = new ServerStatisticsOperation(coordinatorRegistryCenter);
        shardingStatisticsOperation = new ShardingStatisticsOperation(coordinatorRegistryCenter);
    }

    public JobSettings getJobSettings(String jobName) {
        return jobSettingsOperation.getJobSettings(jobName);
    }

    public void trigger(String jobName) {
        jobActionOperation.trigger(jobName);
    }

    public int getJobsTotalCount() {
        return jobStatisticsOperation.getJobsTotalCount();
    }

    public Collection<JobBriefInfo> getAllJobsBriefInfo() {
        return jobStatisticsOperation.getAllJobsBriefInfo();
    }

    public JobBriefInfo getJobBriefInfo(String jobName) {
        return jobStatisticsOperation.getJobBriefInfo(jobName);
    }

    public Collection<JobBriefInfo> getJobsBriefInfo(String ip) {
        return jobStatisticsOperation.getJobsBriefInfo(ip);
    }

    public int getServersTotalCount() {
        return (int) serverStatisticsOperation.getServersTotalCount();
    }

    public Collection<ServerBriefInfo> getAllServersBriefInfo() {
        return serverStatisticsOperation.getAllServersBriefInfo();
    }

    public Collection<ShardingInfo> getShardingInfo(String jobName) {
        return shardingStatisticsOperation.getShardingInfo(jobName);
    }
}
