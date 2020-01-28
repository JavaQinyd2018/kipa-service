/*
package com.kipa.job;

import com.dangdang.ddframe.job.lite.lifecycle.api.JobAPIFactory;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobStatisticsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.ShardingStatisticsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.domain.JobBriefInfo;
import com.dangdang.ddframe.job.lite.lifecycle.domain.ShardingInfo;
import com.google.common.base.Optional;
import com.kipa.common.DataConstant;
import com.kipa.utils.PreCheckUtils;
import com.kipa.utils.PropertiesUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;

*/
/**
 * @author Qinyadong
 * job操作的工具类
 *//*

public final class JobOperation {

    private JobOperation() {}

    */
/**
     * 获取当前名称空间下面所有定时任务的详细信息
     * @param env 环境标识
     * @param namespace 名称空间
     * @return 作业的详细信息
     *//*

    public static Collection<JobBriefInfo> getAllJobsBriefInfo(String env, String namespace) {
        JobStatisticsAPI jobStatisticsAPI = createJobStatisticsAPI(env, namespace);
        return jobStatisticsAPI.getAllJobsBriefInfo();
    }

    */
/**
     * 获取所有的作业名称
     * @param env
     * @param namespace
     * @return
     *//*

    public static Collection<String> getAllJobName(String env, String namespace) {
        Collection<JobBriefInfo> allJobsBriefInfo = getAllJobsBriefInfo(env, namespace);
        if (CollectionUtils.isNotEmpty(allJobsBriefInfo)) {
            return allJobsBriefInfo.stream().map(JobBriefInfo::getJobName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    */
/**
     * 获取当前名称空间下面特定job定时任务的详细信息
     * @param env
     * @param namespace
     * @param jobName
     * @return
     *//*

    public static JobBriefInfo getJobBriefInfo(String env, String namespace, String jobName) {
        final JobStatisticsAPI jobStatisticsAPI = createJobStatisticsAPI(env, namespace);
        return jobStatisticsAPI.getJobBriefInfo(jobName);
    }

    */
/**
     * 获取定时任务的个数
     * @param env
     * @param namespace
     * @return
     *//*

    public static int getJobsTotalCount(String env, String namespace) {
        final JobStatisticsAPI jobStatisticsAPI = createJobStatisticsAPI(env, namespace);
        return jobStatisticsAPI.getJobsTotalCount();
    }

    */
/**
     * 获取作业的分片信息
     * @param env
     * @param namespace
     * @param jobName
     * @return
     *//*

    public static Collection<ShardingInfo> getShardingInfo(String env, String namespace, String jobName) {
        final ShardingStatisticsAPI shardingStatisticsAPI = createShardingStatisticsAPI(env, namespace);
        return shardingStatisticsAPI.getShardingInfo(jobName);
    }
    */
/**
     * 手动触发定时任务
     * @param env
     * @param namespace
     * @param jobName
     *//*

    public static void triggerJob(String env, String namespace, String jobName) {
        Collection<String> allJobName = getAllJobName(env, namespace);
        if (CollectionUtils.isEmpty(allJobName) || !allJobName.contains(jobName)) {
            throw new IllegalArgumentException("名称为："+jobName+"不存在");
        }
        final JobOperateAPI jobOperateAPI = createJobOperateAPI(env, namespace);
        jobOperateAPI.trigger(Optional.of(jobName), Optional.<String>absent());
    }


    private static JobStatisticsAPI createJobStatisticsAPI(String env, String namespace) {
        PreCheckUtils.checkEmpty(namespace,"名称空间不能为空");
        final ElasticJobProperties elasticJobProperties = readProperties(env);
        return JobAPIFactory.createJobStatisticsAPI(elasticJobProperties.getZkAddressList(),
                namespace,
                Optional.fromNullable(elasticJobProperties.getDigest()));
    }

    private static JobOperateAPI createJobOperateAPI(String env, String namespace) {
        PreCheckUtils.checkEmpty(namespace,"名称空间不能为空");
        final ElasticJobProperties elasticJobProperties = readProperties(env);
        return JobAPIFactory.createJobOperateAPI(elasticJobProperties.getZkAddressList(),
                namespace,
                Optional.fromNullable(elasticJobProperties.getDigest()));
    }

    private static ShardingStatisticsAPI createShardingStatisticsAPI(String env, String namespace) {
        PreCheckUtils.checkEmpty(namespace,"名称空间不能为空");
        final ElasticJobProperties elasticJobProperties = readProperties(env);
        return JobAPIFactory.createShardingStatisticsAPI(elasticJobProperties.getZkAddressList(),
                namespace,
                Optional.fromNullable(elasticJobProperties.getDigest()));
    }

    private static ElasticJobProperties readProperties(String env) {
        final Properties properties = PropertiesUtils.loadProperties(DataConstant.CONFIG_FILE);
        String host = PropertiesUtils.getProperty(properties, env, "elastic.job.zk.host");
        String digest = PropertiesUtils.getProperty(properties, env, "elastic.job.zk.digest");
        PreCheckUtils.checkEmpty(host,"定时任务的注册中心地址不能为空");
        ElasticJobProperties elasticJobProperties = new ElasticJobProperties();
        elasticJobProperties.setZkAddressList(host);
        elasticJobProperties.setDigest(digest);
        return elasticJobProperties;
    }
}
*/
