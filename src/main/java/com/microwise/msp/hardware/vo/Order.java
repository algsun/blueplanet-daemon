package com.microwise.msp.hardware.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 下行命令
 * 
 * @author xuexu
 * @since 2011-12-09
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 空数据回补的小时时间戳 */
	private Timestamp datagramStamp;
	/** 命令id，命令对应的数据库id */
	private int orderId;
	/** 终端（节点）Id */
	private String nodeId;
	/** 远程IP */
	private String remoteAddress;
	/** 远程端口 */
	private int remotePort;
	/** 序列号 */
	private String serialNum;
	/** 提交时间 */
	private Timestamp submitTime;
	/** 提交次数 */
	private int submitCount;

	/** 发送时间 */
	private Timestamp sendTime;

	/** 命令执行的反馈码[01成功/02失败/03送达成功/FF路径不通] */
	private int currentState;
	/** 是否有效 */
	private int isValid;

	/** 指令编号 */
	private int orderCode;
	/** 指令参数 */
	private String orderValue;

	/**
	 * 指令标识(目标ID[2byte]+包序列号[1byte]+指令编号[2byte]+指令参数)字符组合
	 */
	private String orderSerial;

	/** 序列化后的下行命令包 */
	private String orderStr;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Timestamp getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}

	public int getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public String getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public Timestamp getDatagramStamp() {
		return datagramStamp;
	}

	public void setDatagramStamp(Timestamp datagramStamp) {
		this.datagramStamp = datagramStamp;
	}

}
