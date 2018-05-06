package com.microwise.msp.hardware.businessbean;

/**
 * 短信发送状态 Bean
 *
 * @author li.jianfei
 * @date 2013-08-30
 */
public class SmsFlagBean {

    /**
     * 区域ID
     */
    private String zoneId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 传感标识
     */
    private int sensorPhysicalId;

    /**
     * 发送状态
     */
    private int sendState;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSensorPhysicalId() {
        return sensorPhysicalId;
    }

    public void setSensorPhysicalId(int sensorPhysicalId) {
        this.sensorPhysicalId = sensorPhysicalId;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }
}
