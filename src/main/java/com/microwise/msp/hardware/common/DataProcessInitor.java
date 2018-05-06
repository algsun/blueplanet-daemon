package com.microwise.msp.hardware.common;

import com.microwise.msp.hardware.businessservice.DataProcessService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

public class DataProcessInitor {

	Scheduler scheduler;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	public DataProcessInitor(Scheduler scheduler){
		this.scheduler = scheduler;
	}

	@PostConstruct
	public void init() throws SchedulerException{
		//判断是否启动
		if(!SysConfig.countSwitch){
			return;
		}

        //小时有机污染物
        addJob(scheduler, "MathHourOpJob",MathHourOpJob.class);
        addTrigger(scheduler, "MathHourOpJob", "MathHourOpJob", "0 10 * * * ?");

        //小时无机污染物
        addJob(scheduler, "MathHourIpJob",MathHourIpJob.class);
        addTrigger(scheduler, "MathHourIpJob", "MathHourIpJob", "0 10 * * * ?");

        //小时含硫污染物
        addJob(scheduler, "MathHourSpJob",MathHourSpJob.class);
        addTrigger(scheduler, "MathHourSpJob", "MathHourSpJob", "0 10 * * * ?");

        //小时水流量
        addJob(scheduler, "mathHourWaterFlowJob",MathHourWaterFlowJob.class);
        addTrigger(scheduler, "mathHourWaterFlowJob", "mathHourWaterFlowJob", "0 10 * * * ?");

        //小时水速
        addJob(scheduler,"mathHourPulseJob",MathHourPulseJob.class);
        addTrigger(scheduler,"mathHourPulseJob","mathHourPulseJob", "0 10 * * * ?");

        //小时水位
        addJob(scheduler,"mathHourWaterLevelJob",MathHourWaterLevelJob.class);
        addTrigger(scheduler,"mathHourWaterLevelJob","mathHourWaterLevelJob","0 10 * * * ?");

        //KDJ
        addJob(scheduler, "mathKDJ",MathKDJ.class);
        addTrigger(scheduler, "mathKDJ", "mathKDJ", "0 0 4 * * ?");
//        addTrigger(scheduler, "mathKDJ", "mathKDJ", "0 */1 * * * ?");

        //删除日志解析
        addJob(scheduler, "deleteLogAnalysis",DeleteLogAnalysis.class);
        addTrigger(scheduler, "deleteLogAnalysis", "deleteLogAnalysis", "0 0 1 * * ?");

        if (SysConfig.countPmSensor) {
            //pm值计算
            addJob(scheduler, "mathPmSensor", MathPmSensorData.class);
            addTrigger(scheduler, "mathPmSensor", "mathPmSensor", SysConfig.countPmSensorTime * 3600);
        }
	}

	public void addJob(Scheduler scheduler,String triggerKey,Class<? extends Job> taskClass) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(taskClass)
				.storeDurably()
				.withIdentity(triggerKey)
				.build();
		scheduler.addJob(jobDetail, false);
	}

	public void addTrigger(Scheduler scheduler,String triggerKey,String jobKey,String rule) throws SchedulerException {
		JobDataMap data = new JobDataMap();
		data.put("appContext", appContext);

		CronTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(rule))
				.forJob(jobKey)
				.usingJobData(data)
				.build();
		scheduler.scheduleJob(trigger);
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

	public static DataProcessService getTaskService(JobExecutionContext jobExecutionContext){
		JobDataMap data = jobExecutionContext.getTrigger().getJobDataMap();
		ApplicationContext appContext = (ApplicationContext) data.get("appContext");
		DataProcessService dataProcessService = (DataProcessService)appContext.getBean(DataProcessService.class);
		return dataProcessService;
	}

    public static class MathHourWaterFlowJob implements Job{
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            getTaskService(context).mathHourData(Defines._WATER_FLOW,"水流量");
        }
    }

    public static class MathHourPulseJob implements Job{
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            getTaskService(context).mathHourData(Defines._PULSE,"水速");
        }
    }

    public static class MathHourWaterLevelJob implements Job{
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            getTaskService(context).mathHourData(Defines._WATER_LEVEL,"水位");
        }
    }

    public static class MathHourOpJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).mathHourOp();
        }
    }

    public static class MathHourIpJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).mathHourIp();
        }
    }

    public static class MathHourSpJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).mathHourSp();
        }
    }

    public static class MathKDJ implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).mathKDJ();
        }
    }

    public static class MathPmSensorData implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).mathPmSensor();
        }
    }

    public static class DeleteLogAnalysis implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
                throws JobExecutionException {
            getTaskService(jobExecutionContext).deleteLogAnalysisFile();
        }
    }

}
