package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.CommandSendJobInitor;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.handler.agent.CmdCache;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.handler.agent.command.Command;
import com.microwise.msp.hardware.handler.agent.command.TurningSwitchCommand;
import com.microwise.msp.hardware.handler.codec.v30.Commands;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-26 11:41
 */
@Component
@Scope("prototype")
@Route("/struts")
public class DeviceCommandController {
    public static final Logger log = LoggerFactory.getLogger(DeviceCommandController.class);
    // 命令执行默认等待时间
    public static final int CMD_DEFAULT_WAIT_TIME = 5000;

    @Autowired
    private DcoOperateService dcoOperateService;

    @Autowired
    private CommandSendJobInitor commandSendJobInitor;

    @Autowired
    private AppCache appCache;

    //input
    private String deviceId;
    private int parentId;
    private int interval;
    private String siteId;

    //目标湿度
    private int targetHumidity;
    //湿度高阈值
    private int highHumidity;
    //湿度低阈值
    private int lowHumidity;
    //目标温度
    private int targetTemperature;
    //空调开关机状态 0-关 1-开
    private int switchState;

    //修改设备工作周期
    @Route("devices/modify_interval")
    public String modifyInterval() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            DeviceBean device = new DeviceBean();
            device.deviceid = deviceId;
            device.interval = interval;
            CmdPromise cmdPromise = dcoOperateService.modifyInterval(device);
            resp.put("success", true);
            if (device.version == Defines.VERSION_3) {
                waitCmdPromise(resp, cmdPromise);
            } else {
                resp.put("sendSuccess", true);
                resp.put("doSuccess", true);
            }
            appCache.evictDevice(deviceId);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    @Route("devices/setHumidityController")
    public String setHumidityController() {
        Map<String, Object> resp = new HashMap<String, Object>();
        if (StringUtils.isBlank(deviceId)) {
            resp.put("success", false);
            resp.put("sendSuccess", false);
            resp.put("doSuccess", false);
        } else {
            CmdPromise cmdPromise = dcoOperateService.setHumidityController(deviceId, targetHumidity, highHumidity, lowHumidity);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        }
        return Results.json().asRoot(resp).done();
    }


    //开启全网巡检
    @Route("devices/openPolling")
    public String openPolling() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.openPolling(siteId, interval);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    //关闭全网巡检
    @Route("devices/closePolling")
    public String closePolling() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.closePolling(siteId);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    @Route("devices/{deviceId}/default-parent/{parentId}")
    public String setDefaultParentId() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setDefaultParentId(deviceId, parentId);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    // input
    // deviceId, route, subNodeId, sensorId, lowLeft, low, lowRight, highLeft, high, highRight, switchAction
    // 子节点
    private String subNodeId;
    // 监测指标ID
    private int sensorId;
    // 低阈值
    private float low;
    // 高阈值
    private float high;
    // 动作
    private int switchAction;

    @Route("devices/{deviceId}/condition-refl")
    public String setConditionRefl() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setConditionRefl(deviceId, route, subNodeId, sensorId, low, high, switchAction);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    //input
    // deviceId, route, onOrOff
    // 哪一路
    private int route;
    // 开或者关
    private boolean onOrOff;

    @Route("devices/{deviceId}/turnSwitch")
    public String turnSwitch() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.turnSwitch(deviceId, route, onOrOff);

            resp.put("success", true);
            if (cmdPromise != null) {
                // 记录命令, 待命令响应包使用
                int doAction = onOrOff ? 1 : 0;
                Command command = new TurningSwitchCommand(deviceId, Commands.CMD_TURN_SWITCH, cmdPromise.getSequence(), route, doAction, "手动控制");
                CmdCache.getInstance().put(deviceId, cmdPromise.getSequence(), command);

                waitCmdPromise(resp, cmdPromise);
                // 网关未收到设备反馈指令
                if (cmdPromise.getDoPromise().isPending()) {
                    commandSendJobInitor.initSendCommandTrigger(cmdPromise.getDeviceId(), cmdPromise.getSequence());
                    //启动quartz之后清除缓存,防止5秒之后动作执行成功发送邮件通知
                    CmdCache.getInstance().evict(deviceId, cmdPromise.getSequence());
                }
            }
            if (resp.get("sendSuccess") == null) {
                resp.put("sendSuccess", false);
            }
            if (resp.get("doSuccess") == null) {
                resp.put("doSuccess", false);
            }

        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    //deviceId
    @Route("devices/{deviceId}/restart")
    public String restart() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.restart(deviceId);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    //input
    private boolean inOrOut;

    @Route(value = "devices/{deviceId}/suspend", params = "inOrOut")
    public String suspend() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.suspendRelay(deviceId, inOrOut);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    /**
     * 标定模式
     *
     * @return
     */
    @Route(value = "devices/{deviceId}/demarcate", params = "inOrOut")
    public String demarcate() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.demarcate(deviceId, inOrOut);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    /**
     * 灵敏度级别
     */
    private byte level;

    /**
     * 设置振动传感器灵敏度级别
     *
     * @return
     */
    @Route(value = "devices/sensitivity")
    public String sensitivity() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setSensitivity(deviceId, level);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    /**
     * 开关设备显示屏
     *
     * @return
     */
    @Route(value = "devices/{deviceId}/switchScreen", params = "onOrOff")
    public String switchScreen() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.switchScreen(deviceId, onOrOff);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

//    /**
//     * 开关蜂鸣器
//     *
//     * @return
//     */
//    @Route(value = "devices/{deviceId}/buzzerSwitch", params = "onOrOff")
//    public String buzzerSwitch() {
//        Map<String, Object> resp = new HashMap<String, Object>();
//        try {
//            CmdPromise cmdPromise = dcoOperateService.buzzerSwitch(deviceId, onOrOff);
//            resp.put("success", true);
//            waitCmdPromise(resp, cmdPromise);
//        } catch (Exception e) {
//            resp.put("success", false);
//            log.error("", e);
//        }
//
//        return Results.json().asRoot(resp).done();
//    }

    @Route("devices/{deviceId}/available-parents")
    public String queryAvailableParents() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.queryAvailableParents(deviceId);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }


    // input
    // deviceId
    // enable true 启用, false 退出
    private boolean enable;

    /**
     * 节点 RF 不休眠
     *
     * @return
     */
    @Route("devices/{deviceId}/rf-alive")
    public String setRFSleep() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setRFAlive(deviceId, enable);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    /**
     * 设置设备阈值报警状态
     *
     * @return
     */
    @Route("device/{deviceId}/threshold-alarm-state")
    public String changeThresholdAlarmState() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.changeThresholdAlarmState(deviceId, enable);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    /**
     * 设置设备监测指标阈值报警范围
     *
     * @return
     */
    @Route("device/{deviceId}/sensor-threshold")
    public String setSensorThreshold() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setSensorThreshold(deviceId, sensorId, high, low);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }

    /**
     * 设备定位
     *
     * @return
     */
    @Route("device/{deviceId}/locate")
    public String locate() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.locate(deviceId);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(resp).done();
    }


    /**
     * 等待命令结果，并组装反馈结果
     *
     * @param resp
     * @param cmdPromise
     */
    private void waitCmdPromise(Map<String, Object> resp, CmdPromise cmdPromise) {
        if (cmdPromise == null) {
            resp.put("sendSuccess", false);
            resp.put("doSuccess", false);
            return;
        }
        try {
            cmdPromise.getDoPromise().waitSafely(CMD_DEFAULT_WAIT_TIME);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        resp.put("sendSuccess", cmdPromise.getSendPromise().isResolved());
        resp.put("doSuccess", cmdPromise.getDoPromise().isResolved());
    }

    /**
     * 设置空调目标湿度
     *
     * @return
     */
    @Route("devices/{deviceId}/setAirConditionerHumidity")
    public String setAirConditionerHumidity() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setAirConditionerHumidity(deviceId, targetHumidity);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    /**
     * 设置空调目标温度
     *
     * @return
     */
    @Route("devices/{deviceId}/setAirConditionerTemperature")
    public String setAirConditionerTemperature() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setAirConditionerTemperature(deviceId, targetTemperature);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    /***
     * 设置空调开关状态
     *
     * @return
     */
    @Route("devices/{deviceId}/setAirConditionerSwitchState")
    public String setAirConditionerSwitchState() {
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            CmdPromise cmdPromise = dcoOperateService.setAirConditionerSwitchState(deviceId, switchState);
            resp.put("success", true);
            waitCmdPromise(resp, cmdPromise);
        } catch (Exception e) {
            resp.put("success", false);
            log.error("", e);
        }

        return Results.json().asRoot(resp).done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        this.inOrOut = inOrOut;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public boolean isOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(boolean onOrOff) {
        this.onOrOff = onOrOff;
    }

    public String getSubNodeId() {
        return subNodeId;
    }

    public void setSubNodeId(String subNodeId) {
        this.subNodeId = subNodeId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public float getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getSwitchAction() {
        return switchAction;
    }

    public void setSwitchAction(int switchAction) {
        this.switchAction = switchAction;
    }

    public boolean isEnable() {
        return enable;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getTargetHumidity() {
        return targetHumidity;
    }

    public void setTargetHumidity(int targetHumidity) {
        this.targetHumidity = targetHumidity;
    }

    public int getHighHumidity() {
        return highHumidity;
    }

    public void setHighHumidity(int highHumidity) {
        this.highHumidity = highHumidity;
    }

    public int getLowHumidity() {
        return lowHumidity;
    }

    public void setLowHumidity(int lowHumidity) {
        this.lowHumidity = lowHumidity;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(int targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public int getSwitchState() {
        return switchState;
    }

    public void setSwitchState(int switchState) {
        this.switchState = switchState;
    }
}