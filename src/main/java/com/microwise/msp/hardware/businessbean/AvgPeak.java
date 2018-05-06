package com.microwise.msp.hardware.businessbean;

import java.util.Date;

public class AvgPeak {
	
	private int sensorPhysicalid;
	
	private double maxValue;
	
	private double minValue;
	
	private double avgValue;
	
	private double waveValue;
	
	private Date maxTime;
	
	private Date minTime;
	
	private Date avgTime;

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(double avgValue) {
		this.avgValue = avgValue;
	}

	public double getWaveValue() {
		return waveValue;
	}

	public void setWaveValue(double waveValue) {
		this.waveValue = waveValue;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	public Date getMinTime() {
		return minTime;
	}

	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	public Date getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(Date avgTime) {
		this.avgTime = avgTime;
	}

	
}
