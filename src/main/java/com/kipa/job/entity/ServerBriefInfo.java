package com.kipa.job.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinyadong
 * 服务分片信息
 */
@Data
public class ServerBriefInfo implements Serializable {

    private String serverIp;
    private final Set<String> instances = new HashSet<>();
    private final Set<String> jobNames = new HashSet<>();
    private int instancesNum;
    private int jobsNum;
    private AtomicInteger disabledJobsNum = new AtomicInteger();

    public ServerBriefInfo(String serverIp) {
        this.serverIp = serverIp;
    }
}
