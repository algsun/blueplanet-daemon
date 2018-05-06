package com.microwise.msp.hardware.businessbean;

/**
 * @author bastengao
 * @date 14-3-24 下午4:40
 */
public class SensorCondition {

    public static final int OPERATOR_MORE_THAN = 1;
    public static final int OPERATOR_LESS_THAN = 2;
    public static final int OPERATOR_EQUAL = 3;

    // ID
    private String id;
    // 动作ID
    private String sensorActionId;
    // 设备ID
    private String deviceId;
    // 监测指标ID
    private int sensorId;
    // 监测指标中文名称
    private String sensorCNName;
    // 监测指标英文名称
    private String sensorENName;
    // 监测指标单位
    private String sensorUnits;
    // 比较的值
    private double value;
    // 比较操作
    private int operator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensorActionId() {
        return sensorActionId;
    }

    public void setSensorActionId(String sensorActionId) {
        this.sensorActionId = sensorActionId;
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

    public String getSensorCNName() {
        return sensorCNName;
    }

    public void setSensorCNName(String sensorCNName) {
        this.sensorCNName = sensorCNName;
    }

    public String getSensorENName() {
        return sensorENName;
    }

    public void setSensorENName(String sensorENName) {
        this.sensorENName = sensorENName;
    }

    public String getSensorUnits() {
        return sensorUnits;
    }

    public void setSensorUnits(String sensorUnits) {
        this.sensorUnits = sensorUnits;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
