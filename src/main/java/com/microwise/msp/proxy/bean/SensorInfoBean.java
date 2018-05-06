package com.microwise.msp.proxy.bean;

/**
 * 监测指标信息对象
 * 
 * @author xubaoji
 * @date 2013-1-18
 */
public class SensorInfoBean {

	/** 监测指标标识 */
	private int sensorId;

	/** 监测指标中文名 */
	private String cnName;

	/** 计量单位 */
	private String units;

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

}
