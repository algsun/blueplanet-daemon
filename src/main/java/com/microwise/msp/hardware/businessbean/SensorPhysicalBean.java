package com.microwise.msp.hardware.businessbean;

import com.microwise.msp.hardware.common.Defines;

import java.io.Serializable;

/**
 * <pre>
 * 传感量包装类[标识、值、状态]
 * </pre>
 * 
 * @author heming
 * @since 2011-10-24
 */
public class SensorPhysicalBean implements Serializable {

	private static final long serialVersionUID = 1L;

    // 采样异常情况下错误类型覆盖枚举
    // 默认(如 原始值为 0xFFFF)
    public static final int ERROR_TYPE_DEFAULT = 0;
    // 套公式计算错误
    public static final int ERROR_TYPE_COMPUTE = 1;
    // 计算结果超出范围
    public static final int ERROR_TYPE_OUT_RANGE = 2;

	/** 传感标识 */
	public int sensorPhysical_id;

	/** 传感值 */
	public double sensor_Value;

    /** 传感值 string 类型(用于入库) */
    public String valueStr;

	/** 传感状态 */
	public int sensor_State;

    /**
     *  采样错误类型
     *  <p>
     *      注意：此属性暂时内部使用，不入库
     *  </p>
     */
    private int errorType = ERROR_TYPE_DEFAULT;

    public SensorPhysicalBean() {
    }

    public SensorPhysicalBean(int sensorPhysical_id, double sensor_Value) {
        this.sensorPhysical_id = sensorPhysical_id;
        this.sensor_Value = sensor_Value;
        this.valueStr = String.valueOf(sensor_Value);
        this.sensor_State = Defines._Sensor_State_OK;
    }

    public SensorPhysicalBean(int sensorPhysical_id, String valueStr) {
        this.sensorPhysical_id = sensorPhysical_id;
        this.valueStr = valueStr;
        this.sensor_State = Defines._Sensor_State_OK;
    }

    public int getSensorPhysical_id() {
        return sensorPhysical_id;
    }

    public void setSensorPhysical_id(int sensorPhysical_id) {
        this.sensorPhysical_id = sensorPhysical_id;
    }

    public double getSensor_Value() {
        return sensor_Value;
    }

    public void setSensor_Value(double sensor_Value) {
        this.sensor_Value = sensor_Value;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public int getSensor_State() {
        return sensor_State;
    }

    public void setSensor_State(int sensor_State) {
        this.sensor_State = sensor_State;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
}
