/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 风向玫瑰图 日统计值表 - windrose
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Windrose implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	public String id;
	/**
	 * 节点 产品入网唯一标识
	 */
	public String nodeid;

	/**
	 * 北风风向统计值
	 */
	public String O_N;
	/**
	 * 北风风速平均值
	 */
	public String S_N;
	/**
	 * 东北偏北风风向统计值
	 */
	public String O_NNE;
	/**
	 * 东北偏北风风速平均值
	 */
	public String S_NNE;
	/**
	 * 东北风风向统计值
	 */
	public String O_NE;
	/**
	 * 东北风风速平均值
	 */
	public String S_NE;
	/**
	 * 东北偏东风风向统计值
	 */
	public String O_ENE;
	/**
	 * 东北偏东风风速平均值
	 */
	public String S_ENE;
	/**
	 * 东风风向统计值
	 */
	public String O_E;
	/**
	 * 东风风速平均值
	 */
	public String S_E;
	/**
	 * 东南偏东风风向统计值
	 */
	public String O_ESE;
	/**
	 * 东南偏东风风速平均值
	 */
	public String S_ESE;
	/**
	 * 东南风风向统计值
	 */
	public String O_SE;
	/**
	 * 东南风风速平均值
	 */
	public String S_SE;
	/**
	 * 东南偏南风风向统计值
	 */
	public String O_SSE;
	/**
	 * 东南偏南风风速平均值
	 */
	public String S_SSE;
	/**
	 * 南风风向统计值
	 */
	public String O_S;
	/**
	 * 南风风速平均值
	 */
	public String S_S;
	/**
	 * 西南偏南风风向统计值
	 */
	public String O_SSW;
	/**
	 * 西南偏南风风速平均值
	 */
	public String S_SSW;
	/**
	 * 西南风风向统计值
	 */
	public String O_SW;
	/**
	 * 西南风风速平均值
	 */
	public String S_SW;
	/**
	 * 西南偏西风风向统计值
	 */
	public String O_WSW;
	/**
	 * 西南偏西风风速平均值
	 */
	public String S_WSW;
	/**
	 * 西风风向统计值
	 */
	public String O_W;
	/**
	 * 西风风速平均值
	 */
	public String S_W;
	/**
	 * 西北偏西风风向统计值
	 */
	public String O_WNW;
	/**
	 * 西北偏西风风速平均值
	 */
	public String S_WNW;
	/**
	 * 西北风风向统计值
	 */
	public String O_NW;
	/**
	 * 西北风风速平均值
	 */
	public String S_NW;
	/**
	 * 西北偏北风风向统计值
	 */
	public String O_NNW;
	/**
	 * 西北偏北风风速平均值
	 */
	public String S_NNW;
	/**
	 * 静风统计值
	 */
	public String windcalm;
	/**
	 * 当天风向总数
	 */
	public int sum;
	/**
	 * 日期
	 */
	public Timestamp ms_date;
	/**
	 * 季度
	 */
	public int season;
	/**
	 * 是否更新
	 */
	public int isupdate;
	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
