package com.microwise.msp.hardware.handler.listener;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorCondition;
import com.microwise.msp.hardware.businessbean.SwitchSensorAction;
import com.microwise.msp.hardware.businessbean.Switches;
import com.microwise.msp.hardware.businessservice.DataReceivedListener;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.CommandSendJobInitor;
import com.microwise.msp.hardware.handler.agent.CmdCache;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.handler.agent.command.Command;
import com.microwise.msp.hardware.handler.agent.command.TurningSwitchCommand;
import com.microwise.msp.hardware.handler.codec.v30.Commands;
import com.microwise.msp.hardware.handler.codec.v30.Switch;
import com.microwise.msp.hardware.service.DeviceSensorService;
import com.microwise.msp.hardware.service.SwitchService;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 控制模块监测指标控制
 *
 * @author bastengao
 * @date 14-3-24 下午1:59
 */
@Component
@Scope("prototype")
public class ControlModuleAutoSwitchDateReceivedListener implements DataReceivedListener {
    public static final Logger log = LoggerFactory.getLogger(ControlModuleAutoSwitchDateReceivedListener.class);

    @Autowired
    private SwitchService switchService;

    @Autowired
    private DeviceSensorService deviceSensorService;

    @Autowired
    private DcoOperateService dcoOperateService;

    @Autowired
    private CommandSendJobInitor commandSendJobInitor;

    @Autowired
    private AppCache appCache;

    @Override
    public void onDataReceived(DeviceBean deviceBean) {

        String deviceId = deviceBean.deviceid;
        // 查询设备是否参与的自动控制, 如果不是，直接退出
        if (!switchService.isDeviceUsedForSensorAction(deviceId)) {
            return;
        }

        // 查询设备参与的所有的控制模块对应的条件
        List<SwitchSensorAction> switchSensorActions = switchService.findSwitchSensorActionsByDeviceId(deviceId);
        for (SwitchSensorAction switchSensorAction : switchSensorActions) {
            // 遍历每一组条件， 获取每一组条件对应的数据
            int doAction = -1;
            ConditionEvaluation conditionEvaluation;
            try {
                conditionEvaluation = evalCondition(switchSensorAction);
                if (conditionEvaluation.isPassed()) {
                    doAction = (switchSensorAction.getAction() == SwitchSensorAction.ACTIOIN_ON) ?
                            SwitchSensorAction.ACTIOIN_ON :
                            SwitchSensorAction.ACTION_OFF;
                } else {
                    doAction = (switchSensorAction.getAction() == SwitchSensorAction.ACTIOIN_ON) ?
                            SwitchSensorAction.ACTION_OFF :
                            SwitchSensorAction.ACTIOIN_ON;
                    // 条件取反
                    conditionEvaluation.setDescription("不满足" + conditionEvaluation.getDescription() + "的条件");
                }
            } catch (ConditionExpiredException e) {
                continue;
            }

            String controlModuleId = switchSensorAction.getControlModuleId();
            int route = switchSensorAction.getRoute();
            Switches switches = switchService.findLastSwitch(controlModuleId);
            if (switches != null) {
                // 如果当前状态是要执行动作的状态，则不用动作
                Switch switcH = switches.getValues().get(route - 1);
                if (switcH.isOn() == (doAction == 1)) {
                    continue;
                }
            }
            // 根据获取的设备数据，求对应条件的真假，做相应地动作。
            log.info("control module: {} #{}, do action: {}", controlModuleId, route, doAction);
            CmdPromise cmdPromise = dcoOperateService.turnSwitch(controlModuleId, route, (doAction == 1));
            if (cmdPromise == null) {
                continue;
            }

            // 记录命令, 待命令响应包使用
            Command command = new TurningSwitchCommand(controlModuleId, Commands.CMD_TURN_SWITCH, cmdPromise.getSequence(), route, doAction, conditionEvaluation.getDescription());
            CmdCache.getInstance().put(controlModuleId, cmdPromise.getSequence(), command);

            try {
                //TODO 等待时间很难确定,10毫秒等不到反馈转入quartz执行 @lijiafnei 2014-5-29
                cmdPromise.getDoPromise().waitSafely(10);
                if (cmdPromise.getDoPromise().isPending()) {
                    commandSendJobInitor.initSendCommandTrigger(cmdPromise.getDeviceId(), cmdPromise.getSequence());
                    //启动quartz之后清除缓存,防止5秒之后动作执行成功发送邮件通知
                    CmdCache.getInstance().evict(cmdPromise.getDeviceId(), cmdPromise.getSequence());
                }
                log.info("cmd send: {}", cmdPromise.getSendPromise().isResolved());
                log.info("cmd execute: {}", cmdPromise.getDoPromise().isResolved());
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 计算监测指标条件
     *
     * @param switchSensorAction
     * @return
     * @throws ConditionExpiredException
     */
    private ConditionEvaluation evalCondition(SwitchSensorAction switchSensorAction) throws ConditionExpiredException {
        boolean result = false;
        StringBuffer conditionDescription = new StringBuffer();
        ConditionEvaluation conditionEvaluation = new ConditionEvaluation();
        for (int i = 0; i < switchSensorAction.getSensorConditions().size(); i++) {
            boolean sensorConditionResult = false;

            SensorCondition sensorCondition = switchSensorAction.getSensorConditions().get(i);
            String deviceId = sensorCondition.getDeviceId();
            int sensorId = sensorCondition.getSensorId();
            NodeSensor deviceSensor = deviceSensorService.findByDeviceIdAndSensorId(deviceId, sensorId);

            if (deviceSensor == null) {
                sensorConditionResult = false;
            }

            // 如果数据的时间离现在太久远，则放弃判断
            // 目前逻辑是条件中只要有一个数据时间太久远则放弃整个全部条件判断
            if (isExpired(deviceSensor)) {
                throw new ConditionExpiredException("expired condition");
            }

            conditionDescription.append(sensorCondition.getSensorCNName());
            switch (sensorCondition.getOperator()) {
                case SensorCondition.OPERATOR_LESS_THAN:
                    sensorConditionResult = deviceSensor.getSensorPhysicalValue() < sensorCondition.getValue();
                    conditionDescription.append("小于");
                    break;
                case SensorCondition.OPERATOR_MORE_THAN:
                    sensorConditionResult = deviceSensor.getSensorPhysicalValue() > sensorCondition.getValue();
                    conditionDescription.append("大于");
                    break;
                case SensorCondition.OPERATOR_EQUAL:
                    sensorConditionResult = deviceSensor.getSensorPhysicalValue() == sensorCondition.getValue();
                    conditionDescription.append("等于");
                    break;
            }
            conditionDescription.append(sensorCondition.getValue()).append(sensorCondition.getSensorUnits());

            if (i == 0) {
                // 如果是第一个对 result 进行初始化
                result = sensorConditionResult;
            } else {

                switch (switchSensorAction.getLogic()) {
                    case SwitchSensorAction.LOGIC_AND:
                        result = result && sensorConditionResult;
                        break;

                    case SwitchSensorAction.LOGIC_OR:
                        result = result || sensorConditionResult;
                        break;
                }
            }

            // 快速失败或者成功
            switch (switchSensorAction.getLogic()) {
                // 如果是与, 快速失败
                case SwitchSensorAction.LOGIC_AND:
                    if (result == false) {
                        conditionEvaluation.setPassed(false);
                        conditionEvaluation.setDescription(conditionDescription.toString());
                        return conditionEvaluation;
                    }
                    if (switchSensorAction.getSensorConditions().size() > 1) {
                        conditionDescription.append("并且");
                    }
                    break;

                // 如果是或, 快速成功
                case SwitchSensorAction.LOGIC_OR:
                    if (result == true) {
                        conditionEvaluation.setPassed(true);
                        conditionEvaluation.setDescription(conditionDescription.toString());
                        return conditionEvaluation;
                    }
                    if (switchSensorAction.getSensorConditions().size() > 1) {
                        conditionDescription.append("或者");
                    }
                    break;
            }
        }

        conditionEvaluation.setPassed(result);
        conditionEvaluation.setDescription(conditionDescription.toString());
        return conditionEvaluation;
    }

    /**
     * 是否失效
     * <p/>
     * 目前时间判断是比较粗暴地一个小时，我觉得应该是设备的两倍的工作周期 @gaohui 2014-04-17
     *
     * @param deviceSensor
     * @return
     */
    private boolean isExpired(NodeSensor deviceSensor) {
        Date timestamp = deviceSensor.getStamp();

        String deviceId = deviceSensor.getNodeid();
        DeviceBean device = appCache.loadDevice(deviceId);
        if (device != null) {
            return DateTime.now().minusSeconds(device.interval * 2).isAfter(timestamp.getTime());
        }

        if (timestamp.before(new DateTime().minusHours(1).toDate())) {
            return true;
        }

        return false;
    }

    /**
     * 条件过期
     */
    private static class ConditionExpiredException extends Exception {
        private ConditionExpiredException() {
        }

        private ConditionExpiredException(String message) {
            super(message);
        }

        private ConditionExpiredException(String message, Throwable cause) {
            super(message, cause);
        }

        private ConditionExpiredException(Throwable cause) {
            super(cause);
        }
    }

    class ConditionEvaluation {
        /**
         * 是否通过
         */
        private boolean passed;

        /**
         * 条件描述
         */
        private String description;

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
