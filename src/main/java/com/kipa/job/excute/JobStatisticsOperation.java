package com.kipa.job.excute;

import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.kipa.job.entity.JobBriefInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qinyadong
 * 作业统计
 */
public final class JobStatisticsOperation {

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    public JobStatisticsOperation(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }


    public int getJobsTotalCount() {
        return this.coordinatorRegistryCenter.getChildrenKeys("/").size();
    }

    public Collection<JobBriefInfo> getAllJobsBriefInfo() {
        final List<String> jobNameList = this.coordinatorRegistryCenter.getChildrenKeys("/");
        if (CollectionUtils.isNotEmpty(jobNameList)) {
            return jobNameList.stream()
                    .map(this::getJobBriefInfo)
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public JobBriefInfo getJobBriefInfo(String jobName) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        JobBriefInfo jobBriefInfo = new JobBriefInfo();
        jobBriefInfo.setJobName(jobName);
        String liteJobConfigJson = coordinatorRegistryCenter.get(jobNodePath.getConfigNodePath());
        if (StringUtils.isNotBlank(liteJobConfigJson)) {
            LiteJobConfiguration liteJobConfig = LiteJobConfigurationGsonFactory.fromJson(liteJobConfigJson);
            jobBriefInfo.setDescription(liteJobConfig.getTypeConfig().getCoreConfig().getDescription());
            jobBriefInfo.setCron(liteJobConfig.getTypeConfig().getCoreConfig().getCron());
            jobBriefInfo.setInstanceCount(this.getJobInstanceCount(jobName));
            jobBriefInfo.setShardingTotalCount(liteJobConfig.getTypeConfig().getCoreConfig().getShardingTotalCount());
            jobBriefInfo.setStatus(this.getJobStatus(jobName));
        }
        return jobBriefInfo;
    }

    private JobBriefInfo.JobStatus getJobStatus(String jobName) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        final List<String> instances = this.coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath());
        if (CollectionUtils.isEmpty(instances)) {
            return JobBriefInfo.JobStatus.CRASHED;
        }

        final List<String> childrenKeys = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getShardingNodePath());
        if (CollectionUtils.isNotEmpty(childrenKeys)) {
            Set<String> stringSet = childrenKeys.stream()
                    .map(childrenKey -> coordinatorRegistryCenter.get(jobNodePath.getShardingNodePath(childrenKey, "instance")))
                    .collect(Collectors.toSet());

            if (CollectionUtils.isNotEmpty(stringSet) && CollectionUtils.containsAll(instances, stringSet)) {
                List<String> serversPath = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getServerNodePath());
                if (CollectionUtils.isNotEmpty(serversPath)) {
                    long disabled = serversPath.stream()
                            .filter(path -> {
                                String serverNodePath = coordinatorRegistryCenter.get(jobNodePath.getServerNodePath(path));
                                return StringUtils.equals("DISABLED", serverNodePath);
                            }).count();
                    return  disabled == serversPath.size() ? JobBriefInfo.JobStatus.DISABLED : JobBriefInfo.JobStatus.OK;
                }
            }
        }
        return JobBriefInfo.JobStatus.SHARDING_ERROR;
    }

    private int getJobInstanceCount(String jobName) {
        return this.coordinatorRegistryCenter.getChildrenKeys((new JobNodePath(jobName)).getInstancesNodePath()).size();
    }

    public Collection<JobBriefInfo> getJobsBriefInfo(String ip) {
        final List<String> jobNames = this.coordinatorRegistryCenter.getChildrenKeys("/");
        if (CollectionUtils.isNotEmpty(jobNames)) {
            return jobNames.stream()
                    .map(jobName -> getJobBriefInfoByJobNameAndIp(jobName, ip))
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private JobBriefInfo getJobBriefInfoByJobNameAndIp(String jobName, String ip) {
        JobBriefInfo jobBriefInfo = new JobBriefInfo();
        if (coordinatorRegistryCenter.isExisted((new JobNodePath(jobName)).getServerNodePath(ip))) {
            jobBriefInfo.setJobName(jobName);
            jobBriefInfo.setStatus(this.getJobStatusByJobNameAndIp(jobName, ip));
            jobBriefInfo.setInstanceCount((int) this.getJobInstanceCountByJobNameAndIp(jobName, ip));
            return jobBriefInfo;
        }
        return jobBriefInfo;
    }

    private JobBriefInfo.JobStatus getJobStatusByJobNameAndIp(String jobName, String ip) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        String status = coordinatorRegistryCenter.get(jobNodePath.getServerNodePath(ip));
        return StringUtils.equalsIgnoreCase("DISABLED",status) ? JobBriefInfo.JobStatus.DISABLED : JobBriefInfo.JobStatus.OK;
    }

    private long getJobInstanceCountByJobNameAndIp(String jobName, String ip) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        final List<String> instances = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath());
        if (CollectionUtils.isNotEmpty(instances)) {
            return instances.stream()
                    .filter(instance -> StringUtils.equals(ip, StringUtils.split(instance, "@-@")[0]))
                    .count();
        }
        return 0;
    }
}
