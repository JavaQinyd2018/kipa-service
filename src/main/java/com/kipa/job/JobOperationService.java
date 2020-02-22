package com.kipa.job;

import com.kipa.job.entity.JobBriefInfo;
import com.kipa.job.entity.JobSettings;
import com.kipa.job.entity.ServerBriefInfo;
import com.kipa.job.entity.ShardingInfo;
import java.util.Collection;

/**
 * @author qinyadong
 * Job 操作的服务类
 */
public interface JobOperationService {

    /**
     * 获取job的设置信息
     * @param namespace 名称空间
     * @param jobName 作业名称
     * @return 作业配置
     */
    JobSettings getJobSettings(String namespace, String jobName);

    /**
     * 手动触发定时作业
     * @param jobName 作业名称
     */
    void trigger(String namespace,String jobName);

    /**
     * 获取作业总数
     * @param namespace 名称空间
     * @return 数值
     */
    int getJobsTotalCount(String namespace);

    /**
     * 获取所有作业信息
     * @param namespace 名称空间
     * @return
     */
    Collection<JobBriefInfo> getAllJobsBriefInfo(String namespace);

    /**
     * 获取所有的jobName
     * @param namespace 名称空间
     * @return 作业名称集合
     */
    Collection<String> getAllJobNames(String namespace);
    /**
     * 获取特定作业信息
     * @param namespace 名称空间
     * @param jobName 作业名称
     * @return 作业信息
     */
    JobBriefInfo getJobBriefInfo(String namespace, String jobName);

    /**
     * 获取特定ip下面的所有作业信息
     * @param namespace 名称空间
     * @param ip ip信息
     * @return 响应返回值
     */
    Collection<JobBriefInfo> getJobsBriefInfo(String namespace,String ip);

    /**
     * 获取服务端所有任务总数
     * @param namespace
     * @return
     */
    int getServersTotalCount(String namespace);

    /**
     * 获取服务器信息
     * @param namespace
     * @return
     */
    Collection<ServerBriefInfo> getAllServersBriefInfo(String namespace);

    /**
     * 获取作业的分片信息
     * @param jobName
     * @return
     */
    Collection<ShardingInfo> getShardingInfo(String namespace, String jobName);
}
