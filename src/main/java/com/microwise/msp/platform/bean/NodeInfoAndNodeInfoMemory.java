package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @version
 * @author 谢登 Copyright 2011 MicroWise System Co.,Ltd.
 * @Date:2013-4-2
 */

public class NodeInfoAndNodeInfoMemory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;
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
	 * 绑定状态：0 未绑定 1已绑定
	 */
	public int binding;

	/**
	 * 0 无效 1 有效
	 */
	public int isActive;

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
	 * 数据版本
	 */
	public long dataVersion;

}
