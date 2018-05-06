/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 节点信息内存表 - nodeinfomemory
 * 		记录  节点_端口号_传感器标识_传感量标识_传感量值 等最新信息
 * 		用于实时数据展现
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Nodeinfomemory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	public String id;

	/**
	 * 产品入网唯一标识
	 */

	public String nodeid;

	/**
	 * 节点协议版本号
	 */
	public int nodeVersion;

	/**
	 * 跳数
	 */
	public int isControl;

	/**
	 * 父节点ip号
	 */
	public int parentIP;

	/**
	 * 当前节点IP号
	 */
	public int childIP;

	/**
	 * 反馈地址IP号
	 */
	public int feedbackIP;

	/**
	 * 包序列号
	 */
	public int sequence;

	/**
	 * 时间戳（校时）
	 */
	public Timestamp stamp;

	/**
	 * 时间戳（空数据）
	 */
	public Timestamp emptyStamp;

	/**
	 * 工作周期 V3前600
	 */
	public int interval_i;

	/**
	 * 接收信号强度
	 */
	public int rssi;

	/**
	 * 连接质量参数
	 */
	public int lqi;

	/**
	 * 是否低电压
	 */
	public int lowvoltage;

	/**
	 * 设备是否异常
	 */
	public int anomaly;

	/**
	 * 0：正常模式 1：巡检模式
	 */
	public int deviceMode;

	/**
	 * 网关设备ip
	 */
	public String remoteIp;

	/**
	 * 网关数据监听端口
	 */
	public int remotePort;

	/**
	 * SD卡状态：0未插卡或卡未插好 1卡已插好 2卡已写满
	 */
	public int sdCardState;

	/**
	 * 数据同步版本
	 */
	public long dataVersion;
}
