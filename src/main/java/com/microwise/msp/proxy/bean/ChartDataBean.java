package com.microwise.msp.proxy.bean;

import java.util.Map;

/**
 * 图形数据对象
 * 
 * @author xubaoji
 * @date 2013-1-29
 */
public class ChartDataBean {

	/** 用于图表呈现的基础数据 */
	private Map<Long, String> baseData;

	/** 用于图表中显示的统计数据 map的key 为 min ，max ，avg */
	private Map<String, String> statData;

	public Map<Long, String> getBaseData() {
		return baseData;
	}

	public void setBaseData(Map<Long, String> baseData) {
		this.baseData = baseData;
	}

	public Map<String, String> getStatData() {
		return statData;
	}

	public void setStatData(Map<String, String> statData) {
		this.statData = statData;
	}

}
