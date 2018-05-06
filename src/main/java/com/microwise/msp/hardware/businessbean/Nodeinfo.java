/** 
 * Nodeinfo.java 
 * @author he.ming 
 * @date Jun 28, 2012
 */
package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;

/**
 * <pre>
 * 探源工程，上行数据包过程中，解析时缓存其节点信息
 * </pre>
 * 
 * @author he.ming
 * @date Jun 28, 2012
 */
public class Nodeinfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识uuid
	 */
	public String id;
	/**
	 * 节点号（子网_节点号）
	 */
	public String nodeid;
	/**
	 * 最新时间戳
	 */
	public String stamp;
	/**
	 * 工作周期
	 */
	public int i_interval = 600;
	/**
	 * 最新原始数据包
	 */
	public String packet;
}
