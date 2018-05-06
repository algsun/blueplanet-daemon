package com.microwise.msp.hardware.common;

import com.microwise.msp.hardware.threads.SupplymentGatewayDataThread;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 数据回补任务
 *
 * @author gaohui
 * @date 13-8-7 17:42
 */
public class SupplymentJobInitor {

    @Autowired
    private ApplicationContext appContext;

    private Scheduler scheduler;

    public SupplymentJobInitor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void init() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(RunnableJob.class)
                .storeDurably()
                .withIdentity("Supplyment-TASK")
                .build();

        scheduler.addJob(job, false);

        if(SysConfig.isCacheDatabackup){
            // TODO 时间可配置 @gaohui 2013-08-07
//            addTrigger(SupplymentCacheDataThread.class, "SupplymentCacheTrigger", 60);
        }

        if(SysConfig.getInstance().isSdDataBackup()){
            // TODO 时间可配置 @gaohui 2013-08-07
            addTrigger(SupplymentGatewayDataThread.class, "SupplymentGatewayTrigger", 60);
        }
    }

    public void addTrigger(Class<? extends Runnable> taskClass, String triggerKey, int minutesInterval) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("appContext", appContext);
        data.put("taskClass", taskClass);

        Trigger simpleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(minutesInterval).repeatForever())
                .forJob("Supplyment-TASK")
                .usingJobData(data)
                .build();

        scheduler.scheduleJob(simpleTrigger);
    }

    public static class RunnableJob implements Job {
        public static final Logger log = LoggerFactory.getLogger(RunnableJob.class);

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.debug("执行调度");

            JobDataMap data = jobExecutionContext.getTrigger().getJobDataMap();
            ApplicationContext appContext = (ApplicationContext) data.get("appContext");
            Class<? extends Runnable> taskClass = (Class<? extends Runnable>) data.get("taskClass");
            Runnable task = appContext.getBean(taskClass);
            task.run();
        }
    }
}
