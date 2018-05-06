package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.util.Date;

public class Historydatacount implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增列
	 */
	public int id;

	/**
	 * 产品入网唯一标识
	 */
	public String nodeid;

	/**
	 * 最小组id
	 */
	public int mingroupid;

	/**
	 * 时间戳（天）
	 */
	public Date date;

	/**
	 * 数据同步版本号
	 */
	public long dataVersion;

}
