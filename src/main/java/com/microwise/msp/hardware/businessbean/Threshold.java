package com.microwise.msp.hardware.businessbean;

/**
 * 监测指标阈值
 *
 * @author li.jianfei
 * @date 2016-07-13
 */
public class Threshold {

    /**
     * 唯一标识
     */
    private int id;

    /**
     * 位置点ID
     */
    private String locationId;

    /**
     * 监测指标
     */
    private int sensorId;

    /**
     * 条件类型
     * 1-范围；2-大于；3-小于；4-大于等于；5-小于等于
     */
    private int conditionType;

    /**
     * 目标值
     */
    private float target;
    /**
     * 浮动值
     */
    private float floating;


    // TODO 以下代码为木卫一相关业务代码,后期需要将木卫一上下限存储与位置点报警业务分离
    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 最小值
     */
    private float minValue;

    /**
     * 最大值
     */
    private float maxValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getConditionType() {
        return conditionType;
    }

    public void setConditionType(int conditionType) {
        this.conditionType = conditionType;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getFloating() {
        return floating;
    }

    public void setFloating(float floating) {
        this.floating = floating;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
