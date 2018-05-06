package com.microwise.msp.hardware.common;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计任务初始化
 *
 * @author gaohui
 * @date 13-8-6 10:29
 */
public class StatsJobInitor {
    // 计算周期, 每 10 秒计算一次
    public static final int INTERVAL = 10;

    public static final String JOB_KEY = "stats.speedJob";
    public static final String TRIGGER_KEY = "stats.speedTrigger";

    private Scheduler scheduler;

    public StatsJobInitor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void init() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(StatsWriteSpeedJob.class)
                .storeDurably()
                .withIdentity(JOB_KEY)
                .build();
        scheduler.addJob(job, false);

        Trigger simpleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(TRIGGER_KEY)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(INTERVAL).repeatForever())
                .forJob(JOB_KEY).build();

        scheduler.scheduleJob(simpleTrigger);

    }

    public static class StatsWriteSpeedJob implements Job {
        public static final Logger log = LoggerFactory.getLogger(StatsWriteSpeedJob.class);

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            Stats stats = Stats.getInst();
            stats.compute(INTERVAL);
        }
    }
}

