package com.kipa.job.excute;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.kipa.job.entity.JobSettings;

/**
 * @author qinyadong
 */
public final class JobSettingsOperation{

    private final CoordinatorRegistryCenter coordinatorRegistryCenter;

    public JobSettingsOperation(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        this.coordinatorRegistryCenter = coordinatorRegistryCenter;
    }

    public JobSettings getJobSettings(String jobName) {
        JobSettings jobSettings = new JobSettings();
        JobNodePath jobNodePath = new JobNodePath(jobName);
        String configNodePath = coordinatorRegistryCenter.get(jobNodePath.getConfigNodePath());
        final LiteJobConfiguration liteJobConfiguration = LiteJobConfigurationGsonFactory.fromJson(configNodePath);
        JobType jobType = liteJobConfiguration.getTypeConfig().getJobType();
        this.buildSimpleJobSettings(jobName, jobSettings, liteJobConfiguration);
        if (JobType.DATAFLOW == jobType) {
            this.buildDataflowJobSettings(jobSettings, (DataflowJobConfiguration)liteJobConfiguration.getTypeConfig());
        }

        if (JobType.SCRIPT == jobType) {
            this.buildScriptJobSettings(jobSettings, (ScriptJobConfiguration)liteJobConfiguration.getTypeConfig());
        }
        return jobSettings;
    }

    private void buildSimpleJobSettings(String jobName, JobSettings result, LiteJobConfiguration liteJobConfig) {
        result.setJobName(jobName);
        result.setJobType(liteJobConfig.getTypeConfig().getJobType().name());
        result.setJobClass(liteJobConfig.getTypeConfig().getJobClass());
        result.setShardingTotalCount(liteJobConfig.getTypeConfig().getCoreConfig().getShardingTotalCount());
        result.setCron(liteJobConfig.getTypeConfig().getCoreConfig().getCron());
        result.setShardingItemParameters(liteJobConfig.getTypeConfig().getCoreConfig().getShardingItemParameters());
        result.setJobParameter(liteJobConfig.getTypeConfig().getCoreConfig().getJobParameter());
        result.setMonitorExecution(liteJobConfig.isMonitorExecution());
        result.setMaxTimeDiffSeconds(liteJobConfig.getMaxTimeDiffSeconds());
        result.setMonitorPort(liteJobConfig.getMonitorPort());
        result.setFailover(liteJobConfig.getTypeConfig().getCoreConfig().isFailover());
        result.setMisfire(liteJobConfig.getTypeConfig().getCoreConfig().isMisfire());
        result.setJobShardingStrategyClass(liteJobConfig.getJobShardingStrategyClass());
        result.setDescription(liteJobConfig.getTypeConfig().getCoreConfig().getDescription());
        result.setReconcileIntervalMinutes(liteJobConfig.getReconcileIntervalMinutes());
        result.getJobProperties().put(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), liteJobConfig.getTypeConfig().getCoreConfig().getJobProperties().get(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER));
        result.getJobProperties().put(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), liteJobConfig.getTypeConfig().getCoreConfig().getJobProperties().get(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER));
    }

    private void buildDataflowJobSettings(JobSettings result, DataflowJobConfiguration config) {
        result.setStreamingProcess(config.isStreamingProcess());
    }

    private void buildScriptJobSettings(JobSettings result, ScriptJobConfiguration config) {
        result.setScriptCommandLine(config.getScriptCommandLine());
    }
}
