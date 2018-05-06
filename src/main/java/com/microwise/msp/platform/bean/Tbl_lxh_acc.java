/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 日照量表 - tbl_lxh_acc
 * 		 日光照统计表
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Tbl_lxh_acc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	public String id;
	/**
	 * 节点号 产品入网唯一标识
	 */
	public String nodeid;

	/**
	 * 日照量
	 */
	public double lx_h;
	/**
	 * 记录时间
	 */
	public Timestamp ms_datetime;
	/**
	 * 季度
	 */
	public int season;
	/**
	 * 是否修改
	 */
	public int isupdate;
	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
