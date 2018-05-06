package com.microwise.msp.hardware.businessbean;

/**
 * 监测指标浮动值
 *
 * @author liuzhu
 * @date 2016-5-16
 */
public class FloatSensor {

    /**
     * 自增id
     */
    private int id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 监测指标id
     */
    private int sensorId;

    /**
     * 下限浮动值
     */
    private double min_down_float;

    /**
     * 上限浮动值
     */
    private double max_up_float;

    /**
     * 0 值浮动值
     */
    private double min_up_float;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getMin_down_float() {
        return min_down_float;
    }

    public void setMin_down_float(double min_down_float) {
        this.min_down_float = min_down_float;
    }

    public double getMax_up_float() {
        return max_up_float;
    }

    public void setMax_up_float(double max_up_float) {
        this.max_up_float = max_up_float;
    }

    public double getMin_up_float() {
        return min_up_float;
    }

    public void setMin_up_float(double min_up_float) {
        this.min_up_float = min_up_float;
    }
}
