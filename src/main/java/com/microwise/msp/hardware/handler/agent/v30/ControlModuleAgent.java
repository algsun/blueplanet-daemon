package com.microwise.msp.hardware.handler.agent.v30;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.businessservice.ControlModuleService;
import com.microwise.msp.hardware.businessservice.UserService;
import com.microwise.msp.hardware.handler.agent.CmdCache;
import com.microwise.msp.hardware.handler.agent.command.Command;
import com.microwise.msp.hardware.handler.agent.command.TurningSwitchCommand;
import com.microwise.msp.hardware.handler.codec.v30.CmdRespPacket;
import com.microwise.msp.hardware.handler.codec.v30.Commands;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packets;
import com.microwise.msp.hardware.service.ControlModuleNotificationService;
import com.microwise.msp.util.EmailSender;
import com.microwise.msp.util.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 控制模块
 *
 * @author gaohui
 * @date 14-2-11 14:02
 */
@Component("v30ControlModuleAgent")
@Scope("prototype")
public class ControlModuleAgent extends DefaultAgent {
    public static final Logger log = LoggerFactory.getLogger(ControlModuleAgent.class);

    @Autowired
    private ControlModuleService controlModuleService;
    @Autowired
    private ControlModuleNotificationService notificationService;
    @Autowired
    private com.microwise.msp.hardware.service.DeviceService _deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSender emailSender;

    @Override
    protected void onDataReady(DataV30Packet packet) {
        DeviceBean deviceBean = DataV30Packets.fromPacket(packet);
        parseSwitches(packet, deviceBean);
        siteId = deviceBean.siteId;

        controlModuleService.businessProcess(deviceBean);
    }

    @Override
    protected void onCommandAckReady(CmdRespPacket packet) {
        if (siteId == null) {
            return;
        }

        log.debug("ack ready");
        String deviceId = deviceId(packet);


        // 如果是开关控制且控制成功
        if (packet.getOrderId() == Commands.CMD_TURN_SWITCH && packet.getFeedback() == Commands.FEEDBACK_SUCCESS) {
            // 查找站点下对应的通知设置
            List<ControlModuleNotification> notifications = notificationService.findAllBySiteId(siteId);
            if (notifications.isEmpty()) {
                return;
            }

            for (ControlModuleNotification notification : notifications) {
                // 判断是否参与设置 且事件为状态切换
                if (isMatch(deviceId, notification) && ((notification.getTriggerEvent() >> 1) & 0x0001) == 1) {
                    log.debug("match");

                    Command command = CmdCache.getInstance().getAndEvict(deviceId, packet.getSourceSequence());
                    if (command == null) {
                        continue;
                    }
                    if (command.getCommandId() != Commands.CMD_TURN_SWITCH) {
                        continue;
                    }
                    TurningSwitchCommand cmd = (TurningSwitchCommand) command;
                    // 根据以上信息组织短信内容 @gaohui 2014-04-29
                    DeviceBean device = _deviceService.findDeviceById(deviceId);
                    String content = genContent(device, cmd);
                    try {
                        // 查找对应用户
                        User user = userService.findById(notification.getUserId());
                        int notifyMethod = notification.getNotifyMethod();
                        // 短信
                        if ((notifyMethod & 0x0001) == 1) {
                            if (!Strings.isNullOrEmpty(user.getMobile())) {
                                SmsSender.send(content, user.getMobile(), null);
                            }
                        }
                        // email
                        if ((notifyMethod >> 1 & 0x0001) == 1) {
                            emailSender.send(user.getEmail(), "控制模块开关切换", content);
                        }
                    } catch (MessagingException e) {
                        log.error("", e);
                    } catch (UnsupportedEncodingException e) {
                        log.error("", e);
                    }
                }
            }
        }
    }

    /**
     * 生成通知内容
     *
     * @param device
     * @param cmd
     * @return
     */
    private String genContent(DeviceBean device, TurningSwitchCommand cmd) {
        StringBuilder sb = new StringBuilder();
        String zoneName = _deviceService.findZoneName(device.deviceid);

        if (!Strings.isNullOrEmpty(zoneName)) {
            sb.append(zoneName);
            sb.append("的");
        }
        sb.append("控制模块 ").append(device.deviceid);

        // 如果有设备名称
        if (!Strings.isNullOrEmpty(device.deviceName)) {
            sb.append("(").append(device.deviceName).append(") ");
        }

        String alias = notificationService.findAlias(device.deviceid);
        String aliasStr = "";
        if (!Strings.isNullOrEmpty(alias)) {
            aliasStr = "(" + alias + ")";
        }
        sb.append("第")
                .append(cmd.getRoute())
                .append("路")
                .append(aliasStr)
                .append((cmd.getAction() == 1 ? "打开" : "关闭"));

        // 动作原因
        sb.append("【原因：").append(cmd.getCause()).append("】");

        return sb.toString();
    }

    /**
     * 解析开关状态
     *
     * @param dataPacket
     * @param device
     */
    private void parseSwitches(DataV30Packet dataPacket, DeviceBean device) {
        device.setSwitchExists(dataPacket.isSwitchExists());
        device.setSwitchEnable(dataPacket.getSwitchEnable());
        device.setSwitchStatus(dataPacket.getSwitchStatus());
        device.setSwitchCondRefl(dataPacket.isSwitchCondRefl());
        Switches switches = new Switches(device.deviceid,
                dataPacket.getSwitchEnable(),
                dataPacket.getSwitchStatus(),
                dataPacket.getTimestamp(),
                dataPacket.getSwitches());
        device.setSwitches(switches);

        device.setConditionReflExists(dataPacket.isConditionReflExists());

        if (dataPacket.isConditionReflExists()) {
            DataV30Packet.ConditionRefl srcRefl = dataPacket.getConditionRefl();

            ConditionRefl conditionRefl = new ConditionRefl();
            conditionRefl.setDeviceId(device.deviceid);
            conditionRefl.setRoute(srcRefl.getRoute());
            conditionRefl.setSubTerminalId(srcRefl.getSubTerminalId());
            conditionRefl.setSensorId(srcRefl.getSensorId());
            conditionRefl.setLowLeft(srcRefl.getLowLeft());
            conditionRefl.setLow(srcRefl.getLow());
            conditionRefl.setLowRight(srcRefl.getLowRight());
            conditionRefl.setHighLeft(srcRefl.getHighLeft());
            conditionRefl.setHigh(srcRefl.getHigh());
            conditionRefl.setHighRight(srcRefl.getHighRight());
            conditionRefl.setAction(srcRefl.getAction());
            conditionRefl.setUpdateTime(dataPacket.getTimestamp());

            device.setConditionRefl(conditionRefl);
        }
    }

    /**
     * 判断设备是否匹配通知
     *
     * @param deviceId
     * @param notification
     * @return
     */
    private boolean isMatch(String deviceId, ControlModuleNotification notification) {
        // global config
        if (notification.getSubscribeType() == ControlModuleNotification.SUBSCRIBE_TYPE_ALL_DEVICE) {
            return true;
        }

        // custom config
        if (notification.getSubscribeType() == ControlModuleNotification.SUBSCRIBE_TYPE_CUSTOM_DEVICE) {
            return notification.getDeviceIds().contains(deviceId);
        }

        return false;
    }
}
