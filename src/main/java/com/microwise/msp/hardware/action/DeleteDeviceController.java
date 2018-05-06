package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessservice.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiedeng
 * @date 13-10-25
 */
@Component
@Scope("prototype")
@Route("/struts")
public class DeleteDeviceController {

    @Autowired
    @Qualifier("DeviceService")
    private DeviceService deviceService;

    private String deviceId;

    @Route("/deleteDevice")
    public String deleteDevices() {
        if (StringUtils.isNotBlank(deviceId)) {
            if (deviceService.deleteDevice(deviceId)) {
                print("{\"success\":true}");
            } else {
                print("{\"success\":false}");
            }
        } else {
            print("{\"success\":false}");
        }
        return null;
    }

    /**
     * 打印返回信息
     *
     * @param msg 返回信息
     */
    private void print(String msg) {
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter print = null;
        try {
            print = response.getWriter();
            print.print(msg);
        } catch (IOException e) {

        } finally {
            print.close();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
