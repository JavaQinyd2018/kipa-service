package com.kipa.job.excute;

import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author qinyadong
 */
public class JobActionOperation {

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    public JobActionOperation(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }

    public void trigger(String jobName) {
        JobNodePath jobNodePath = new JobNodePath(jobName);
        List<String> childrenKeys = coordinatorRegistryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath());
        if (CollectionUtils.isNotEmpty(childrenKeys)) {
            childrenKeys.forEach(path -> {
                String instanceNodePath = jobNodePath.getInstanceNodePath(path);
                coordinatorRegistryCenter.persist(instanceNodePath, "TRIGGER");
            });
        }
    }
}
