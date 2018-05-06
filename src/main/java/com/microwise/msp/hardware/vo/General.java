package com.microwise.msp.hardware.vo;

/**
 * 常规设置Vo
 * 
 * 
 * @author xuexu
 * @deprecated TODO 无用，删除之 @gaohui 2013-08-18
 */
public class General {

	private String avgPeakTime; // 执行均峰值时间
	private int enumPort; // 端口号
	private int intervalForClient; // 客户端获取数据的时间间隔
	private String nodeid; // 节点号
	private int sensorid; // 传感器标识
	private int sensorPhysicalid; // 传感量标识

	public String getAvgPeakTime() {
		return avgPeakTime;
	}

	public int getEnumPort() {
		return enumPort;
	}

	public int getIntervalForClient() {
		return intervalForClient;
	}

	public String getNodeid() {
		return nodeid;
	}

	public int getSensorid() {
		return sensorid;
	}

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setAvgPeakTime(String avgPeakTime) {
		this.avgPeakTime = avgPeakTime;
	}

	public void setEnumPort(int enumPort) {
		this.enumPort = enumPort;
	}

	public void setIntervalForClient(int intervalForClient) {
		this.intervalForClient = intervalForClient;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}
}
