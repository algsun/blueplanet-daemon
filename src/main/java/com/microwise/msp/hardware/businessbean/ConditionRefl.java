package com.microwise.msp.hardware.businessbean;

import java.util.Date;

/**
 * @author gaohui
 * @date 14-2-13 10:31
 */
public class ConditionRefl {

    // 条件反射原始值左值，右值与原始值的偏差 5%
    public static final float SENSOR_VALUE_DELTA = 0.05F;

    // 无条件关
    public static final int ACTION_ALWAYS_OFF = 0;
    // 范围内开，范围外关
    public static final int ACTION_RANGE_IN_ON_OUT_OFF = 2;
    // 低于阈值关，高于阈值开
    public static final int ACTION_LEFT_OFF_RIGHT_ON = 3;
    // 低于阈值开，高于阈值关
    public static final int ACTION_LEFT_ON_RIGHT_OFF = 4;
    // 范围内关，范围外开
    public static final int ACTION_RANGE_IN_OFF_OUT_ON = 5;
    // 无条件开
    public static final int ACTION_ALWAYS_ON = 7;
    // 无条件反射
    public static final int ACTION_NONE = 8;

    private String id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 端口/路
     */
    private int route;
    /**
     * 子设备ID
     */
    private int subTerminalId;
    /**
     * 监测指标ID
     */
    private int sensorId;
    /**
     * 左值
     */
    private int lowLeft;
    /**
     * 原始值
     */
    private int low;
    /**
     * 右值
     */
    private int lowRight;

    /**
     * 结果值
     */
    private double lowTarget;

    /**
     * 左值
     */
    private int highLeft;
    /**
     * 原始值
     */
    private int high;
    /**
     * 右值
     */
    private int highRight;

    /**
     * 结果值
     */
    private double highTarget;

    /**
     * 动作
     */
    private int action;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getSubTerminalId() {
        return subTerminalId;
    }

    public void setSubTerminalId(int subTerminalId) {
        this.subTerminalId = subTerminalId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getLowLeft() {
        return lowLeft;
    }

    public void setLowLeft(int lowLeft) {
        this.lowLeft = lowLeft;
    }

    public int getLowRight() {
        return lowRight;
    }

    public void setLowRight(int lowRight) {
        this.lowRight = lowRight;
    }

    public int getHighLeft() {
        return highLeft;
    }

    public void setHighLeft(int highLeft) {
        this.highLeft = highLeft;
    }

    public int getHighRight() {
        return highRight;
    }

    public void setHighRight(int highRight) {
        this.highRight = highRight;
    }

    public double getLowTarget() {
        return lowTarget;
    }

    public void setLowTarget(double lowTarget) {
        this.lowTarget = lowTarget;
    }

    public double getHighTarget() {
        return highTarget;
    }

    public void setHighTarget(double highTarget) {
        this.highTarget = highTarget;
    }

    @Override
    public String toString() {
        return "ConditionRefl{" +
                "route=" + route +
                ", subTerminalId=" + subTerminalId +
                ", sensorId=" + sensorId +
                ", low=" + low +
                ", high=" + high +
                ", action=" + action +
                '}';
    }
}
