package com.microwise.msp.proxy.bean;

/**
 * 监测指标数据
 * 
 * @author xubaoji
 * @date 2013-1-18
 */
public class NodeSensorBean {

	/** 传感量 标识 如 32：表示温度 ..... */
	private int sensorId;

	/** 传感量 的值 */
	private String sensorValue;

	/** 传感量 值状态 0：采样失败 1：采样正常 2：低于低阀值 3：超过高阀值 4：空数据（前台暂不处理） */
	private int valueState;

	/** 0默认，1风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识 */
	private int showType;

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorValue() {
		return sensorValue;
	}

	public void setSensorValue(String sensorValue) {
		this.sensorValue = sensorValue;
	}

	public int getValueState() {
		return valueState;
	}

	public void setValueState(int valueState) {
		this.valueState = valueState;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

}
