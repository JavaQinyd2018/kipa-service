package com.kipa.job.excute;

import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.kipa.job.entity.ServerBriefInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author qinyadong
 */
public final class ServerStatisticsOperation {

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    public ServerStatisticsOperation(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }


    public long getServersTotalCount() {
        final List<String> jobNameList = coordinatorRegistryCenter.getChildrenKeys("/");
        if (CollectionUtils.isNotEmpty(jobNameList)) {
            return jobNameList.stream()
                    .map(jobName-> {
                        JobNodePath jobNodePath = new JobNodePath(jobName);
                        List<String> childrenKeys = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getServerNodePath());
                        return new HashSet<String>(childrenKeys).size();
                    }).count();
        }
        return 0;
    }

    public Collection<ServerBriefInfo> getAllServersBriefInfo() {
        ConcurrentHashMap<String, ServerBriefInfo> servers = new ConcurrentHashMap<>();
        final List<String> jobNameList = coordinatorRegistryCenter.getChildrenKeys("/");
        if (CollectionUtils.isNotEmpty(jobNameList)) {

            jobNameList.forEach(jobName -> {
                JobNodePath jobNodePath = new JobNodePath(jobName);
                List<String> childrenKeys = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getServerNodePath());
                childrenKeys.forEach(s -> {
                    servers.putIfAbsent(s, new ServerBriefInfo(s));
                    ServerBriefInfo serverInfo = servers.get(s);
                    String status = this.coordinatorRegistryCenter.get(jobNodePath.getServerNodePath(s));
                    if (StringUtils.equals("DISABLED",status)) {
                        serverInfo.getDisabledJobsNum().incrementAndGet();
                    }

                    serverInfo.getJobNames().add(jobName);
                    serverInfo.setJobsNum(serverInfo.getJobNames().size());
                });

                List<String> instances = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath());
                if (CollectionUtils.isNotEmpty(instances)) {
                    instances.forEach(instance -> {
                        String serverIp = instance.split("@-@")[0];
                        ServerBriefInfo serverInfo = servers.get(serverIp);
                        if (null != serverInfo) {
                            serverInfo.getInstances().add(instance);
                            serverInfo.setInstancesNum(serverInfo.getInstances().size());
                        }
                    });
                }
            });
        }
        return servers.values().stream().filter(Objects::nonNull).sorted().collect(Collectors.toList());
    }

}
