package com.microwise.msp.hardware.businessbean;

/**
 * 数据回补--原始数据包
 * 
 * @author he.ming
 * @date May 29, 2012
 * @deprecated 建议使用新的 packet 类, 在 codec 包下 @gaohui 2013-08-13
 */
public class DataPacket {

	/**
	 * nodeId[子网id_节点id]
	 */
	public String nodeid;
	/**
	 * 时间戳
	 */
	public String stamp;
	/**
	 * 原始数据包
	 */
	public String packet;

}
