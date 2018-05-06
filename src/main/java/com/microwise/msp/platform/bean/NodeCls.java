/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * n个节点的节点数据表结构  --生成的每一张节点数据表
 *    该表用于存放节点数据
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public class NodeCls implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int id;
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
	public String sensorPhysicalvalue;
	/**
	 * 是否低电压
	 */
	public int lowvoltage;

	/**
	 * 状态
	 */
	public float anomaly;

	/**
	 * 收到数据包的时间
	 */
	public Timestamp createtime;
	/**
	 * 传感量状态
	 */
	public int state;

	/**
	 * 数据版本号
	 */
	public long dataVersion;

}
