/**
 *<pre> 
 *</pre> 
 * @author  he.ming
 * @date    May 25, 2012 10:00:30 AM
 */
package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 数据包
 * 
 * @author he.ming
 * @date 2012-5-25
 */
public class DataPacketBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nodeid;
	/**
	 * 时间戳
	 */
	private Timestamp stamp;
	/**
	 * 原始数据包
	 */
	private String packet;

	/**
	 * 查询的开始时间戳
	 */
	private Timestamp startTimestamp;
	/**
	 * 查询的结束时间戳
	 */
	private Timestamp endTimestamp;

	public DataPacketBean() {
		super();
	}

	/**
	 * 原始数据包 构造函数
	 * 
	 * @param subnetId
	 *            子网id
	 * @param deviceId
	 *            节点id
	 * @param stamp
	 *            时间戳
	 * @param packet
	 *            原始数据包
	 */
	public DataPacketBean(String nodeId, Timestamp stamp, String packet) {
		super();
		this.stamp = stamp;
		this.packet = packet;
		this.nodeid = nodeId;
	}

	/**
	 * 
	 * @param subnetId
	 *            子网Id
	 * @param deviceId
	 *            节点Id
	 * @param startTimestamp
	 * @param endTimestamp
	 */
	public DataPacketBean(String nodeId, Timestamp startTimestamp,
			Timestamp endTimestamp) {
		super();
		this.nodeid = nodeId;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}

	public String getPacket() {
		return packet;
	}

	public void setPacket(String packet) {
		this.packet = packet;
	}

	public Timestamp getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Timestamp startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

}
