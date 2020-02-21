package com.kipa.job.service;

import com.kipa.common.KipaProcessException;
import com.kipa.job.JobOperationService;
import com.kipa.job.entity.*;
import com.kipa.job.excute.BaseJobOperation;
import com.kipa.utils.PreCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * @author qinyadong
 * 作业操作的服务
 */
@Service("jobOperationService")
public class JobOperationServiceImpl implements JobOperationService {

    @Autowired
    private ElasticJobProperties elasticJobProperties;

    @Override
    public JobSettings getJobSettings(String namespace, String jobName) {
        PreCheckUtils.checkEmpty(jobName, "作业名称不能为空");
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getJobSettings(jobName);
    }

    @Override
    public void trigger(String namespace, String jobName) {
        PreCheckUtils.checkEmpty(jobName, "作业名称不能为空");
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        baseJobOperation.trigger(jobName);
    }

    @Override
    public int getJobsTotalCount(String namespace) {
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getJobsTotalCount();
    }

    @Override
    public Collection<JobBriefInfo> getAllJobsBriefInfo(String namespace) {
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getAllJobsBriefInfo();
    }

    @Override
    public JobBriefInfo getJobBriefInfo(String namespace, String jobName) {
        PreCheckUtils.checkEmpty(jobName, "作业名称不能为空");
        BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getJobBriefInfo(jobName);
    }

    @Override
    public Collection<JobBriefInfo> getJobsBriefInfo(String namespace, String ip) {
        PreCheckUtils.checkEmpty(ip, "ip不能为空");
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getJobsBriefInfo(ip);
    }

    @Override
    public int getServersTotalCount(String namespace) {
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getServersTotalCount();
    }

    @Override
    public Collection<ServerBriefInfo> getAllServersBriefInfo(String namespace) {
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getAllServersBriefInfo();
    }

    @Override
    public Collection<ShardingInfo> getShardingInfo(String namespace, String jobName) {
        PreCheckUtils.checkEmpty(jobName, "作业名称不能为空");
        final BaseJobOperation baseJobOperation = build(elasticJobProperties, namespace);
        return baseJobOperation.getShardingInfo(jobName);
    }

    private BaseJobOperation build(ElasticJobProperties elasticJobProperties, String namespace) {
        PreCheckUtils.checkEmpty(namespace, "作业名称空间不能为空");
        BaseJobOperation baseJobOperation = new BaseJobOperation(elasticJobProperties.getZkAddressList(), elasticJobProperties.getDigest(), namespace);
        try {
            baseJobOperation.afterPropertiesSet();
        } catch (Exception e) {
            throw new KipaProcessException("作业调用配置初始化失败");
        }
        return baseJobOperation;
    }
}
