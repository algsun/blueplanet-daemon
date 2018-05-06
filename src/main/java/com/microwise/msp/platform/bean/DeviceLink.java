package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xiedeng
 * @date 13-11-20
 */
public class DeviceLink implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 自增编号
     */
    public int id;

    /**
     * 产品入网唯一标识
     */

    public String nodeId;

    /**
     * 时间戳（校时）
     */
    public Timestamp stamp;


    /**
     * 节点协议版本号
     */
    public int nodeVersion;

    /**
     * 父节点ip号
     */
    public int parentIp;

    /**
     * 当前节点IP号
     */
    public int selfIp;

    /**
     * 包序列号
     */
    public int sequence;

    /**
     * 工作周期 V3前600
     */
    public int workInterval;


    /**
     * 0：正常模式 1：巡检模式
     */
    public int deviceMode;

    /**
     * 接收信号强度
     */
    public int rssi;

    /**
     * 连接质量参数
     */
    public int lqi;

    /**
     * 电压值
     */
    public int voltage;

    /**
     * 设备是否异常
     */
    public int anomaly;

    /**
     * SD卡状态：0未插卡或卡未插好 1卡已插好 2卡已写满
     */
    public int sdCardState;

    /**
     * 网关设备ip
     */
    public String remoteHost;

    /**
     * 网关数据监听端口
     */
    public int remotePort;

    /**
     * 数据同步版本
     */
    public long dataVersion;
}
