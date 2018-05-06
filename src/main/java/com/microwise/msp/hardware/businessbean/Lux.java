package com.microwise.msp.hardware.businessbean;

import java.util.Date;

public class Lux {
	
	/**
	 * 光照值
	 */
	private double lux;
	
	/**
	 * 时间
	 */
	private Date datetime;

	public double getLux() {
		return lux;
	}

	public void setLux(double lux) {
		this.lux = lux;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	

}
