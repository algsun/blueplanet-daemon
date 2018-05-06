/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 设备_监测点 对应信息(记录设备和监测点对应信息)
 * node_monitoringStation_info
 * </pre>
 * 
 * @author heming
 * @since 2011-11-23
 */
public class Tnode_monitoringStation_info implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * guid
	 */
	public String id;
	/**
	 * 设备id,节点id
	 */
	public String nodeid;
	/**
	 * 监测点id
	 */
	public String monitoringStation_id;
	/**
	 * 开始时间
	 */
	public Timestamp startTime;
	/**
	 * 结束时间
	 */
	public Timestamp endTime;
	/**
	 * 文博监测站id(博物馆id)
	 */
	public String site_id;
	/**
	 * 区域监测中心id
	 */
	public String region_id;
	/**
	 * 数据版本号
	 */
	public long dataVersion;

}
