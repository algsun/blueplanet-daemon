package com.microwise.msp.hardware.businessbean;

import java.util.Date;

/**
 * 开关自动动作
 *
 * @author gaohui
 * @date 14-3-6 13:08
 */
public class SwitchAction {
    public static final int TYPE_DAILY = 1;
    public static final int TYPE_INTERVAL = 2;

    /**
     * id uuid
     */
    protected String id;

    /**
     * 设备Id
     */
    protected String deviceId;

    /**
     * 路数
     */
    protected int route;

    /**
     * 最后修改时间
     */
    protected Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
