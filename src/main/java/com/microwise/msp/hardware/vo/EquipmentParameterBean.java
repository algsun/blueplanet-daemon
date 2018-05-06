package com.microwise.msp.hardware.vo;

/**
 * 系数Bean（webService中替代Coefficient）
 * 
 * 
 * @author xuexu
 * 
 */
public class EquipmentParameterBean {
	// 枚举端口号
	private int enumport;
	// 唯一标识
	private int id;
	// 节点号（产品入网唯一标识）
	private String nodeid;
	// 计算公式系数
	private String param;
	// 传感器标识
	private int sensorid;
	// 监测点中文名
	private String sensorPhysicalCNName;
	// 监测点代号
	private String sensorPhysicalENName;
	// 传感量标识
	private int sensorPhysicalid;
	// 节点计算公式自定义系数值
	private String value;

	public int getEnumport() {
		return enumport;
	}

	public int getId() {
		return id;
	}

	public String getNodeid() {
		return nodeid;
	}

	public String getParam() {
		return param;
	}

	public int getSensorid() {
		return sensorid;
	}

	public String getSensorPhysicalCNName() {
		return sensorPhysicalCNName;
	}

	public String getSensorPhysicalENName() {
		return sensorPhysicalENName;
	}

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public String getValue() {
		return value;
	}

	public void setEnumport(int enumport) {
		this.enumport = enumport;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public void setSensorPhysicalCNName(String sensorPhysicalCNName) {
		this.sensorPhysicalCNName = sensorPhysicalCNName;
	}

	public void setSensorPhysicalENName(String sensorPhysicalENName) {
		this.sensorPhysicalENName = sensorPhysicalENName;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
