package com.microwise.msp.hardware.vo;

import java.util.Date;

/**
 * 数据回补辅助Vo
 * 
 * @author xuexu
 * @deprecated TODO 无用，删除之 @gaohui 2013-08-18
 */
public class DataSupplement {

	/**
	 * 时间
	 */
	private Date datetime;
	/**
	 * 设备id
	 */
	private String deviceid = "";
	/**
	 * 端口号
	 */
	private int enumPort;
	/**
	 * 传感器id
	 */
	private int sensorid;
	/**
	 * 传感量id
	 */
	private int sensorPhysicalid;

	public Date getDatetime() {
		return datetime;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public int getEnumPort() {
		return enumPort;
	}

	public int getSensorid() {
		return sensorid;
	}

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public void setEnumPort(int enumPort) {
		this.enumPort = enumPort;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}
}
