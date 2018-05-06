package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-10-23 20:28
 */
@Component
@Scope("prototype")
@Route("/struts")
public class DevicesController {

    private String deviceId;
    //湿度补偿开关，0 关， 1 开
    private int humCompensate;

    @Autowired
    DeviceService deviceService;

    @Route("/devices/{deviceId}/setHumCompensate/{humCompensate}")
    public String setHumCompensate() {
        Map<String,Object> result = new HashMap<String, Object>();
        boolean success = deviceService.setHumCompensate(deviceId, humCompensate);
        result.put("success",success);
        return Results.json().asRoot(result).done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getHumCompensate() {
        return humCompensate;
    }

    public void setHumCompensate(int humCompensate) {
        this.humCompensate = humCompensate;
    }
}
