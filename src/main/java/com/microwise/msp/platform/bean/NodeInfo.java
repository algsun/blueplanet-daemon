package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 节点信息表 - nodeinfo
 *    	存储节点信息(也是实时更新的)
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public class NodeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <pre>
	 * 产品入网唯一标识，
	 * 非元智的设备为guid生产的唯一标识码
	 * </pre>
	 */
	public String nodeid;
	/**
	 * 节点类型 1节点 2中继 7网关
	 */
	public int nodeType;

	/**
	 * 节点创建时间
	 */
	public Timestamp createtime;

	/**
	 * X轴坐标
	 */
	public int x;

	/**
	 * Y轴坐标
	 */
	public int y;

	/**
	 * Z轴坐标
	 */
	public int z;

	/**
	 * 文博监测站id
	 */
	public String siteId;

	/**
	 * 系统相对路径和名称
	 */
	public String deviceImage;

	/**
	 * 数据同步版本
	 */
	public long dataVersion;

	/**
	 * 绑定状态：0 未绑定 1已绑定
	 */
	public int binding;

	/**
	 * 0 无效 1 有效
	 */
	public int isActive;

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
}
