package com.microwise.msp.hardware.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 节点表对应类
 * 
 * @author xuexu
 * 
 */
public class NodeVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // uuid
    private String locationId; // 位置点编号
	private String nodeid; // 设备id
	private int nodeType; // 设备类型
	private int jump;
	private int parentid; // 父ip
	private int selfid; // 自身ip
	private int rssi;
	private int lqi;
	private float lowvoltage; // 电压
	private int sequence; // 包序列号
	private int state; // 节点状态
	private Timestamp time; // 节点创建时间

	private int sensorid; // 传感器id
	private int enumPort; // 端口枚举号

	private int sensorPhysicalid; // 传感id
	private String sensorvalue; // 传感值
	private Date dateTime; // 记录时间

	private int groupid;// 数据的组id
	private int dataVersion;// 数据同步版本号
	private int anomaly; // 状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getJump() {
		return jump;
	}

	public void setJump(int jump) {
		this.jump = jump;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getSelfid() {
		return selfid;
	}

	public void setSelfid(int selfid) {
		this.selfid = selfid;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public int getLqi() {
		return lqi;
	}

	public void setLqi(int lqi) {
		this.lqi = lqi;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getSensorid() {
		return sensorid;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public int getEnumPort() {
		return enumPort;
	}

	public void setEnumPort(int enumPort) {
		this.enumPort = enumPort;
	}

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}

	public String getSensorvalue() {
		return sensorvalue;
	}

	public void setSensorvalue(String sensorvalue) {
		this.sensorvalue = sensorvalue;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(int dataVersion) {
		this.dataVersion = dataVersion;
	}

	public int getAnomaly() {
		return anomaly;
	}

	public void setAnomaly(int anomaly) {
		this.anomaly = anomaly;
	}

	public float getLowvoltage() {
		return lowvoltage;
	}

	public void setLowvoltage(float lowvoltage) {
		this.lowvoltage = lowvoltage;
	}

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
