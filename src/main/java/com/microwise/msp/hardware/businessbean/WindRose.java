package com.microwise.msp.hardware.businessbean;

public class WindRose {
	
	public float windmark;							//风向标识
	public int windDirectionCount;				    //风向总数
	public float windSpeedAvg;						//风速平均值
	
	public float getWindmark() {
		return windmark;
	}
	public void setWindmark(float windmark) {
		this.windmark = windmark;
	}
	public int getWindDirectionCount() {
		return windDirectionCount;
	}
	public void setWindDirectionCount(int windDirectionCount) {
		this.windDirectionCount = windDirectionCount;
	}
	public float getWindSpeedAvg() {
		return windSpeedAvg;
	}
	public void setWindSpeedAvg(float windSpeedAvg) {
		this.windSpeedAvg = windSpeedAvg;
	}
	
	

}
