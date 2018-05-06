package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;
import java.sql.Timestamp;

public class EmptyDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String nodeid;

	private Timestamp stamp;

	private Integer gatewaySuccess;

	private Integer dataCacheSuccess;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}

	public Integer getGatewaySuccess() {
		return gatewaySuccess;
	}

	public void setGatewaySuccess(Integer gatewaySuccess) {
		this.gatewaySuccess = gatewaySuccess;
	}

	public Integer getDataCacheSuccess() {
		return dataCacheSuccess;
	}

	public void setDataCacheSuccess(Integer dataCacheSuccess) {
		this.dataCacheSuccess = dataCacheSuccess;
	}

}
