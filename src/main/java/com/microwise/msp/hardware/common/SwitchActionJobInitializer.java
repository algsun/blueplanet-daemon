package com.microwise.msp.hardware.common;

import com.microwise.msp.hardware.businessbean.SwitchAction;
import com.microwise.msp.hardware.businessbean.SwitchDailyAction;
import com.microwise.msp.hardware.businessbean.SwitchIntervalAction;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.handler.agent.CmdCache;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.handler.agent.command.Command;
import com.microwise.msp.hardware.handler.agent.command.TurningSwitchCommand;
import com.microwise.msp.hardware.handler.codec.v30.Commands;
import com.microwise.msp.hardware.service.SwitchService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 控制模块开关自动控制
 *
 * @author bastengao
 * @date 14-3-7 上午10:51
 */
public class SwitchActionJobInitializer {
    public static final Logger log = LoggerFactory.getLogger(SwitchActionJobInitializer.class);
    public static final String SWITCH_ACTION_INTERVAL_START = "switch-action-interval-start";
    public static final String SWITCH_ACTION_INTERVAL_END = "switch-action-interval-end";
    public static final String SWITCH_ACTION_DAILY = "switch-action-daily";

    // 命令执行默认等待时间
    public static final int CMD_DEFAULT_WAIT_TIME = 5000;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private SwitchService switchService;

    private Scheduler scheduler;

    @Autowired
    public SwitchActionJobInitializer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Autowired
    private CommandSendJobInitor commandSendJobInitor;

    @PostConstruct
    public void init() throws SchedulerException {
        log.info("开关自动控制启动");

        JobDetail jobDetail0 = JobBuilder.newJob(SwitchDailyActionJob.class)
                .withIdentity(SWITCH_ACTION_DAILY)
                .storeDurably()
                .build();

        JobDetail jobDetail = JobBuilder.newJob(SwitchIntervalActionStartJob.class)
                .withIdentity(SWITCH_ACTION_INTERVAL_START)
                .storeDurably()
                .build();

        JobDetail jobDetail2 = JobBuilder.newJob(SwitchIntervalActionEndJob.class)
                .withIdentity(SWITCH_ACTION_INTERVAL_END)
                .storeDurably()
                .build();

        scheduler.addJob(jobDetail0, false);
        scheduler.addJob(jobDetail, false);
        scheduler.addJob(jobDetail2, false);

        for (SwitchDailyAction dailyAction : switchService.findAllDailyActions()) {
            initSwitchDailyAction(dailyAction);
        }
        for (SwitchIntervalAction intervalAction : switchService.findAllIntervalActions()) {
            initSwitchIntervalAction(intervalAction);
        }

    }

    /**
     * 重新加载自动开关动作
     *
     * @param switchActionId
     * @param type
     */
    public void reloadSwitchActions(String deviceId, String switchActionId, int type) throws SchedulerException {
        // 先取消掉之前的任务(如果之前无动作也无所谓), 再加载任务

        if (type == SwitchAction.TYPE_DAILY) {
            TriggerKey key = dailyTriggerKey(deviceId, switchActionId);
            try {
                scheduler.unscheduleJob(key);
            } catch (SchedulerException e) {
                log.error("unschedule switch action triggers", e);
            }

            SwitchDailyAction switchAction = switchService.findDailyActionById(switchActionId);
            if (switchAction != null) {
                initSwitchDailyAction(switchAction);
            }


        } else if (type == SwitchAction.TYPE_INTERVAL) {
            TriggerKey startKey = intervalStartTriggerKey(deviceId, switchActionId);
            TriggerKey endKey = intervalEndTriggerKey(deviceId, switchActionId);

            try {
                scheduler.unscheduleJob(startKey);
                scheduler.unscheduleJob(endKey);
            } catch (SchedulerException e) {
                log.error("unschedule switch action triggers", e);
            }

            SwitchIntervalAction switchAction = switchService.findIntervalActionById(switchActionId);
            if (switchAction != null) {
                initSwitchIntervalAction(switchAction);
            }

        }


    }

    private void initSwitchDailyAction(SwitchDailyAction dailyAction) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("appContext", appContext);
        data.put("action", dailyAction);
        data.put("commandSendJobInitor", commandSendJobInitor);


        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(SWITCH_ACTION_DAILY)
                .withIdentity(dailyTriggerKey(dailyAction))
                .usingJobData(data)
                .withSchedule(
                        CronScheduleBuilder.dailyAtHourAndMinute(
                                dailyAction.getTime().getHours(),
                                dailyAction.getTime().getMinutes()
                        )
                )
                .build();

        scheduler.scheduleJob(trigger);
    }

    private void initSwitchIntervalAction(SwitchIntervalAction intervalAction) throws SchedulerException {
        int interval = intervalAction.getIntervalTime();
        int executionTime = intervalAction.getExecutionTime();

        JobDataMap data = new JobDataMap();
        data.put("appContext", appContext);
        data.put("action", intervalAction);
        data.put("commandSendJobInitor", commandSendJobInitor);

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(SWITCH_ACTION_INTERVAL_START)
                .withIdentity(intervalStartTriggerKey(intervalAction))
                .usingJobData(data)
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(interval + executionTime)
                                .repeatForever()
                )
                .startNow()
                .build();

        scheduler.scheduleJob(trigger);
    }

    public static class SwitchDailyActionJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
            ApplicationContext appContext = (ApplicationContext) jobDataMap.get("appContext");
            SwitchDailyAction switchDailyAction = (SwitchDailyAction) jobDataMap.get("action");
            CommandSendJobInitor commandSendJobInitor = (CommandSendJobInitor) jobDataMap.get("commandSendJobInitor");

            String deviceId = switchDailyAction.getDeviceId();
            int route = switchDailyAction.getRoute();
            int action = switchDailyAction.getAction();

            // do action
            DcoOperateService dcoOperateService = appContext.getBean(DcoOperateService.class);
            CmdPromise cmdPromise = dcoOperateService.turnSwitch(deviceId, route, (action == 1));

            if (cmdPromise != null) {
                // 记录命令, 待命令响应包使用
                Command command = new TurningSwitchCommand(deviceId, Commands.CMD_TURN_SWITCH, cmdPromise.getSequence(), route, action, "每日定时执行");
                CmdCache.getInstance().put(deviceId, cmdPromise.getSequence(), command);

                try {
                    cmdPromise.getDoPromise().waitSafely(CMD_DEFAULT_WAIT_TIME);
                    initSendCommandTrigger(commandSendJobInitor, cmdPromise);
                    log.info("cmd send: {}", cmdPromise.getSendPromise().isResolved());
                    log.info("cmd execute: {}", cmdPromise.getDoPromise().isResolved());
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }
    }


    /**
     * 周期动作 执行期间开始任务
     * <p/>
     */
    public static class SwitchIntervalActionStartJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.info("start");

            JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
            ApplicationContext appContext = (ApplicationContext) jobDataMap.get("appContext");
            SwitchIntervalAction switchIntervalAction = (SwitchIntervalAction) jobDataMap.get("action");
            CommandSendJobInitor commandSendJobInitor = (CommandSendJobInitor) jobDataMap.get("commandSendJobInitor");

            String deviceId = switchIntervalAction.getDeviceId();
            int route = switchIntervalAction.getRoute();
            int action = switchIntervalAction.getAction();

            try {
                // 删除上一次结束执行的 trigger
                jobExecutionContext.getScheduler().unscheduleJob(intervalEndTriggerKey(switchIntervalAction));
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

            // do action
            DcoOperateService dcoOperateService = appContext.getBean(DcoOperateService.class);
            CmdPromise cmdPromise = dcoOperateService.turnSwitch(deviceId, route, (action == 1));
            if (cmdPromise != null) {
                // 记录命令, 待命令响应包使用
                Command command = new TurningSwitchCommand(deviceId, Commands.CMD_TURN_SWITCH, cmdPromise.getSequence(), route, action, "间隔执行");
                CmdCache.getInstance().put(deviceId, cmdPromise.getSequence(), command);

                try {
                    cmdPromise.getDoPromise().waitSafely(CMD_DEFAULT_WAIT_TIME);
                    initSendCommandTrigger(commandSendJobInitor, cmdPromise);
                    log.info("cmd send: {}", cmdPromise.getSendPromise().isResolved());
                    log.info("cmd execute: {}", cmdPromise.getDoPromise().isResolved());
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }

            // 启动结束执行任务
            Trigger endTrigger = TriggerBuilder.newTrigger()
                    .usingJobData(jobDataMap)
                    .withIdentity(intervalEndTriggerKey(switchIntervalAction))
                    .startAt(new Date(System.currentTimeMillis() + switchIntervalAction.getExecutionTime() * 1000))
                    .forJob(SWITCH_ACTION_INTERVAL_END)
                    .build();

            try {
                jobExecutionContext.getScheduler().scheduleJob(endTrigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 周期动作 执行期间结束任务
     */
    public static class SwitchIntervalActionEndJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.info("end");

            JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
            ApplicationContext appContext = (ApplicationContext) jobDataMap.get("appContext");
            SwitchIntervalAction switchIntervalAction = (SwitchIntervalAction) jobDataMap.get("action");
            CommandSendJobInitor commandSendJobInitor = (CommandSendJobInitor) jobDataMap.get("commandSendJobInitor");

            String deviceId = switchIntervalAction.getDeviceId();
            int route = switchIntervalAction.getRoute();
            int action = switchIntervalAction.getAction();

            DcoOperateService dcoOperateService = appContext.getBean(DcoOperateService.class);
            // undo action
            CmdPromise cmdPromise = dcoOperateService.turnSwitch(deviceId, route, (action != 1));

            if (cmdPromise != null) {
                // 记录命令, 待命令响应包使用
                Command command = new TurningSwitchCommand(deviceId, Commands.CMD_TURN_SWITCH, cmdPromise.getSequence(), route, ((action != 1) ? 0 : 1), "间隔执行");
                CmdCache.getInstance().put(deviceId, cmdPromise.getSequence(), command);

                try {
                    cmdPromise.getDoPromise().waitSafely(CMD_DEFAULT_WAIT_TIME);
                    initSendCommandTrigger(commandSendJobInitor, cmdPromise);
                    log.info("cmd send: {}", cmdPromise.getSendPromise().isResolved());
                    log.info("cmd execute: {}", cmdPromise.getDoPromise().isResolved());
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }
    }

    private static void initSendCommandTrigger(CommandSendJobInitor commandSendJobInitor, CmdPromise cmdPromise) {
        if (cmdPromise != null) {
            if (cmdPromise.getDoPromise().isPending()) {
                commandSendJobInitor.initSendCommandTrigger(cmdPromise.getDeviceId(), cmdPromise.getSequence());
                //启动quartz之后清除缓存,防止5秒之后动作执行成功发送邮件通知
                CmdCache.getInstance().getAndEvict(cmdPromise.getDeviceId(), cmdPromise.getSequence());
            }
        }
    }

    private static TriggerKey dailyTriggerKey(SwitchAction switchAction) {
        return new TriggerKey(switchAction.getId(), triggerGroup(switchAction));
    }

    private static TriggerKey dailyTriggerKey(String deviceId, String switchActionId) {
        return new TriggerKey(switchActionId, triggerGroup(deviceId));
    }

    private static TriggerKey intervalStartTriggerKey(SwitchAction switchAction) {
        return intervalStartTriggerKey(switchAction.getDeviceId(), switchAction.getId());
    }

    private static TriggerKey intervalEndTriggerKey(SwitchAction switchAction) {
        return intervalEndTriggerKey(switchAction.getDeviceId(), switchAction.getId());
    }

    private static TriggerKey intervalStartTriggerKey(String deviceId, String switchActionId) {
        return new TriggerKey("start-" + switchActionId, triggerGroup(deviceId));
    }

    private static TriggerKey intervalEndTriggerKey(String deviceId, String switchActionId) {
        return new TriggerKey("end-" + switchActionId, triggerGroup(deviceId));
    }


    private static String triggerGroup(SwitchAction switchAction) {
        return triggerGroup(switchAction.getDeviceId());
    }

    private static String triggerGroup(String deviceId) {
        return String.format("switch-action-" + deviceId);
    }
}
