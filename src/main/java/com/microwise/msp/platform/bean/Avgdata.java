/**
 *
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <pre>
 * 均峰值数据表 - avgdata
 *      记录均峰值信息
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Avgdata implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	public String id;

	/**
	 * 产品入网唯一标识
	 */
	public String nodeid;
	/**
	 * 传感量标识
	 */
	public int sensorPhysicalid;

	/**
	 * 最大值
	 */
	public String maxvalue;
	/**
	 * 最小值
	 */
	public String minvalue;
	/**
	 * 平均值
	 */
	public String avgvalue;

	/**
	 * 波值
	 */
	public String wavevalue;

    /**
     *最大值时间
     */
    public Date maxTime;

    /**
     * 最小值时间
     */
    public Date minTime;

	/**
	 * 日期值
	 */
	public Timestamp ms_date;
	/**
	 * 状态
	 */
	public int state;

	/**
	 * 是否更新
	 */
	public int isupdate;
	/**
	 * 数据同步版本号
	 */
	public long dataVersion;

}
