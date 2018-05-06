package com.microwise.msp.hardware.businessbean;

import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;

/**
 * @author xiedeng
 * @date 14-12-8
 */
public class DeviceProperties {

    /**
     * 用后两个bit 标识：1：生产阶段（默认）2：试用阶段
     */
    private Integer deviceProperty;

    /**
     * 降雨状态
     *  0代表无降雨，1代表有降雨
     */
    private Integer rainState;

    /**
     * 雨量筒状态
     *  0代表已关盖，1代表已开盖，2代表错误，3代表正在开盖或关盖
     */
    private Integer rainGaugeState;

    /**
     * 振动传感器灵敏度(1-高，2-中，3-低)
     */
    private int sensitivity;



    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 恒湿机运行状态
     */
    private DataV30Packet.HumidityController humidityController;

    private DataV30Packet.AirConditionerController airConditionerController;

    /**
     * 设备显示屏开关状态
     * 0-关；1-开
     */
    private Integer screenState;


    public DataV30Packet.HumidityController getHumidityController() {
        return humidityController;
    }

    public void setHumidityController(DataV30Packet.HumidityController humidityController) {
        this.humidityController = humidityController;
    }

    public Integer getRainState() {
        return rainState;
    }

    public void setRainState(Integer rainState) {
        this.rainState = rainState;
    }

    public Integer getRainGaugeState() {
        return rainGaugeState;
    }

    public void setRainGaugeState(Integer rainGaugeState) {
        this.rainGaugeState = rainGaugeState;
    }

    public Integer getDeviceProperty() {
        return deviceProperty;
    }

    public void setDeviceProperty(Integer deviceProperty) {
        this.deviceProperty = deviceProperty;
    }

    public Integer getScreenState() {
        return screenState;
    }

    public void setScreenState(Integer screenState) {
        this.screenState = screenState;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public DataV30Packet.AirConditionerController getAirConditionerController() {
        return airConditionerController;
    }

    public void setAirConditionerController(DataV30Packet.AirConditionerController airConditionerController) {
        this.airConditionerController = airConditionerController;
    }
}
