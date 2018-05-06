package com.microwise.msp.hardware.common;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.handler.agent.CmdCache;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.handler.agent.command.Command;
import com.microwise.msp.hardware.handler.agent.command.TurningSwitchCommand;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;



/**
 * 确保设备下行指令送达率任务
 *
 * @author li.jianfei
 * @date 2014-05-21
 */
public class CommandSendJobInitor {

    public static final Logger log = LoggerFactory.getLogger(CommandSendJobInitor.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private AppCache appCache;

    public void initSendCommandTrigger(String deviceId, int sequence) {
        // 获取缓存指令类型
        Command command = CmdCache.getInstance().get(deviceId, sequence);
        int commandId = command.getCommandId();

        // 组织JobDataMap
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("command", command);
        dataMap.put("appContext", appContext);

        try {
            // 删除相同JobKey的Job
            scheduler.interrupt(JobKey.jobKey(String.format("%s-%s", deviceId, commandId)));
            scheduler.deleteJob(JobKey.jobKey(String.format("%s-%s", deviceId, commandId)));

            JobDetail jobDetail = JobBuilder.newJob(CommandSendJob.class)
                    .withIdentity(String.format("%s-%s", deviceId, commandId))
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(String.format("%s-%s", deviceId, commandId))
                    .withIdentity(String.format("%s-%s", deviceId, commandId))
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1))
                    .usingJobData(dataMap)
                    .startNow()
                    .build();

            // 调度
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (UnableToInterruptJobException e) {
            log.error("CommandSendJobInitor", e);
        } catch (SchedulerException e) {
            log.error("CommandSendJobInitor", e);
        }
    }

    public class CommandSendJob implements InterruptableJob {
        // 等待指令执行时间 5 秒
        private static final int CMD_DEFAULT_WAIT_TIME = 5000;

        /**
         * 是否中断
         */
        private boolean interrupted;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            try {
                ApplicationContext appContext = (ApplicationContext) jobExecutionContext.getTrigger().getJobDataMap().get("appContext");
                Command command = (Command) jobExecutionContext.getTrigger().getJobDataMap().get("command");
                DcoOperateService dcoOperateService = appContext.getBean(DcoOperateService.class);

                CmdPromise cmdPromise;
                DeviceBean deviceBean;
                //时间超过一天跳出循环
                Date start = new DateTime().plusDays(1).toDate();
                do {
                    if (interrupted)  break;
                    if (DateTime.now().toDate().getTime() > start.getTime())  break;

                    // 设备状态为超时退出 TODO 判断多余
                    deviceBean =appCache.loadDevice(command.getDeviceId());
                    if (deviceBean.anomaly == -1) break;

                    //控制“控制模块”开关
                    TurningSwitchCommand turningSwitchCommand = (TurningSwitchCommand) command;
                    cmdPromise = dcoOperateService.turnSwitch(turningSwitchCommand.getDeviceId(),
                            turningSwitchCommand.getRoute(),
                            turningSwitchCommand.getAction() == 1);

                    if (cmdPromise == null) break;

                    // 缓存指令
                    turningSwitchCommand.setSequence(cmdPromise.getSequence());
                    CmdCache.getInstance().put(cmdPromise.getDeviceId(), cmdPromise.getSequence(), turningSwitchCommand);

                    // 等待执行结果
                    cmdPromise.getDoPromise().waitSafely(CMD_DEFAULT_WAIT_TIME);

                    // 执行失败后清除缓存的指令
                    if (cmdPromise.getDoPromise().isPending()) {
                        CmdCache.getInstance().evict(cmdPromise.getDeviceId(), cmdPromise.getSequence());
                    }

                    // 不是 isResolved() 应该是 isPending()
                } while (cmdPromise.getDoPromise().isPending());

                jobExecutionContext.getScheduler().deleteJob(jobExecutionContext.getJobDetail().getKey());
            } catch (SchedulerException e) {
                log.error("CommandSendJobInitor", e);
            } catch (InterruptedException e) {
                log.error("CommandSendJobInitor", e);
            }
        }

        @Override
        public void interrupt() throws UnableToInterruptJobException {
            interrupted = true;
        }
    }
}


