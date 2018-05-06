/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 节点_枚举端口_传感 对应表 - nodesensor
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public class Nodesensor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 前台为Guid
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
	 * 传感量值
	 */
	public String sensorPhysicalValue;

	/**
	 * 时间戳
	 */
	public Timestamp stamp;

	/**
	 * 传感量状态
	 */
	public int state;

	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
