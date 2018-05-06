package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 巡检。其实就是通过网关下的所有设备(除过网关), 统一以一种固定的工作周期工作，通常都比较实际工作时间短。
 * 巡检有状态，开始和结束。开始后，只有结束后，才能再次开始。
 *
 * @author gaohui
 * @date 13-11-7 15:12
 */
@Component
@Scope("prototype")
@Route("/struts")
public class PatrolCheckController {
    public static final Logger log = LoggerFactory.getLogger(PatrolCheckController.class);

    @Autowired
    private DcoOperateService dcoOperateService;
    @Autowired
    private DeviceService deviceService;

    // input
    // 网关ID
    private String deviceId;
    private int interval;

    //output
    private boolean success = false;
    private String msg;

    // 开始巡检
    @Route(value = "/devices/{deviceId}/patrol-check", params = {"method=start"})
    public String start() {
        try {
            DeviceBean device  = deviceService.findDeviceById(deviceId);
            if(device == null){
                success = false;
                msg = "device not found";
                return Results.json().done();
            }

            // 检查是否是网关/根节点 @gaohui 2013-11-07
            if(!DcoOperateService.isRoot(device)){
                success = false;
                msg = "device is not gateway or root node";
                return Results.json().done();
            }

            CmdPromise cmdPromise = dcoOperateService.pollingOpen(deviceId, interval);
            if (cmdPromise != null) {
                success = true;
            }
        } catch (Exception e) {
            log.error("开始巡检", e);
            msg = e.getMessage();
        }
        return Results.json().done();
    }

    // 结束巡检
    @Route(value = "/devices/{deviceId}/patrol-check", params = {"method=end"})
    public String end() {
        try {
            DeviceBean device  = deviceService.findDeviceById(deviceId);
            if(device == null){
                success = false;
                msg = "device not found";
                return Results.json().done();
            }

            // 检查是否是网关/根节点 @gaohui 2013-11-07
            if(!DcoOperateService.isRoot(device)){
                success = false;
                msg = "device is not gateway or root node";
                return Results.json().done();
            }

            CmdPromise cmdPromise = dcoOperateService.pollingClose(deviceId);
            if (cmdPromise != null) {
                success = true;
            }
        } catch (Exception e) {
            log.error("结束巡检", e);
            msg = e.getMessage();
        }
        return Results.json().done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
