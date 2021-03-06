/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 日降雨量表 - tbl_rd_day_acc
 * 		 日降水量信息统计表
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Tbl_rb_day_acc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * UUID
	 */
	public String id;
	/**
	 * 节点号 产品入网唯一标识
	 */
	public String nodeid;

	/**
	 * 降雨量
	 */
	public double rb;
	/**
	 * 记录日期
	 */
	public Timestamp ms_date;
	/**
	 * 是否修改
	 */
	public int isupdate;
	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
