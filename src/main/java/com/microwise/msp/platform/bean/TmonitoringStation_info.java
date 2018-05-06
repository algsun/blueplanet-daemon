/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;

/**
 * <pre>
 * 监测点信息表（前台要求_数据同步）
 * </pre>
 * 
 * @author heming
 * @since 2011-11-23
 */
public class TmonitoringStation_info implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 监测点id,guid
	 */
	public String monitoringStation_id;
	/**
	 * 监测点名称
	 */
	public String monitoringStation_name;
	/**
	 * 区域监测中心id
	 */
	public String region_id;
	/**
	 * 文博监测站id(博物馆id)
	 */
	public String site_id;
	/**
	 * 位置，描述监测点所处位置，2011-12-14添加
	 */
	public String postion;
	/**
	 * x轴坐标
	 */
	public int x;
	/**
	 * y轴坐标
	 */
	public int y;
	/**
	 * z轴坐标
	 */
	public int z;
	/**
	 * <pre>
	 * 数据版本号
	 * 新增时为0，大于0可修改+1，为0时删除为-2，非0时删除为-2
	 */
	public long dataVersion;
	/**
	 * 备注
	 */
	public String remark;
}
