package com.microwise.msp.hardware.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 位置点传感信息类
 * 
 * @author liuzhu
 * @date 2014-12-11
 */
public class LocationSensor implements Serializable {

	private static final long serialVersionUID = 1L;

	/** UUID */
	private String id;
	/** 节点号 */
	private String locationId;
	/** 传感标识 */
	private int sensorPhysicalid;
	/** 传感值 */
	private double sensorPhysicalValue;
	/**
	 * <pre>
	 * 传感量状态: 
	 * 0：采样失败(0xFFFF为采样失败) 1：采样正常 2：低于低阀值 3：超过高阀值 4：空数据
	 * </pre>
	 */
	private int state;
	/** 实时数据时间戳 */
	private Timestamp stamp;
	/** 数据版本号 */
	private int dataVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}

	public double getSensorPhysicalValue() {
		return sensorPhysicalValue;
	}

	public void setSensorPhysicalValue(double sensorPhysicalValue) {
		this.sensorPhysicalValue = sensorPhysicalValue;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}

	public int getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(int dataVersion) {
		this.dataVersion = dataVersion;
	}

}
