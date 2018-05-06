package com.microwise.msp.hardware.common;

import com.microwise.msp.hardware.service.AnalysisDataFileService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author xiedeng
 * @date 14-7-31
 */
public class AnalysisDataFileJobInitor {

    public static final int INTERVAL = 3600;

    public static final String JOB_KEY = "analysisDataFileJob";

    public static final String TRIGGER_KEY = "analysisDataFileTrigger";

    private Scheduler scheduler;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    public AnalysisDataFileJobInitor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void init() throws SchedulerException {
        addJob(scheduler, JOB_KEY, AnalysisDataFileJob.class);
        addTrigger(scheduler, TRIGGER_KEY, JOB_KEY, INTERVAL);
    }

    public void addJob(Scheduler scheduler, String triggerKey, Class<? extends Job> taskClass) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(taskClass)
                .storeDurably()
                .withIdentity(triggerKey)
                .build();
        scheduler.addJob(jobDetail, false);
    }

    public void addTrigger(Scheduler scheduler, String triggerKey, String jobKey, int interval) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("appContext", appContext);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(interval).repeatForever())
                .forJob(jobKey)
                .usingJobData(data)
                .build();
        scheduler.scheduleJob(trigger);
    }

    public static AnalysisDataFileService getTaskService(JobExecutionContext jobExecutionContext) {
        JobDataMap data = jobExecutionContext.getTrigger().getJobDataMap();
        ApplicationContext appContext = (ApplicationContext) data.get("appContext");
        return appContext.getBean(AnalysisDataFileService.class);
    }

    public static class AnalysisDataFileJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            getTaskService(context).handleFile();
        }
    }
}
