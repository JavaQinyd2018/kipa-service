package com.kipa.job.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 作业信息
 */
@Data
public class JobBriefInfo implements Serializable, Comparable<JobBriefInfo>{

    private String jobName;
    private JobBriefInfo.JobStatus status;
    private String description;
    private String cron;
    private int instanceCount;
    private int shardingTotalCount;

    public enum JobStatus{
        OK,
        CRASHED,
        DISABLED,
        SHARDING_ERROR;
    }

    @Override
    public int compareTo(JobBriefInfo o) {
        return this.getJobName().compareTo(o.getJobName());
    }
}
