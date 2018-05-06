package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.common.SwitchActionJobInitializer;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-3-10 上午10:18
 */
@Component
@Scope("prototype")
@Route("/struts")
public class SwitchActionChangeController {
    public static final Logger log = LoggerFactory.getLogger(SwitchActionChangeController.class);

    @Autowired
    SwitchActionJobInitializer jobInitializer;

    // input
    // 设备ID
    private String deviceId;

    // 动作 id
    private String switchActionId;

    // 类型
    private int type;

    /**
     * 控制模块自动动作时间类改变
     *
     * @return
     */
    @Route(value = "devices/{deviceId}/switch-action-time", method = MethodType.PUT)
    public String time() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            System.out.println(switchActionId);
            System.out.println(type);
            jobInitializer.reloadSwitchActions(deviceId, switchActionId, type);
            result.put("success", true);
        } catch (SchedulerException e) {
            e.printStackTrace();
            result.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(result).done();
    }


    /**
     * 控制模块自动动作时间类改变
     *
     * @return
     */
    @Route(value = "devices/{deviceId}/switch-action-sensor", method = MethodType.PUT)
    public String sensor() {
        Map<String, Object> result = new HashMap<String, Object>();
        return Results.json().asRoot(result).done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSwitchActionId() {
        return switchActionId;
    }

    public void setSwitchActionId(String switchActionId) {
        this.switchActionId = switchActionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
