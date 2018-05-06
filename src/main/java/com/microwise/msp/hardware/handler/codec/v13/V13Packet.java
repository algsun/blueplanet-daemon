package com.microwise.msp.hardware.handler.codec.v13;

import java.util.Map;

/**
 * v1.3 协议
 *
 * @author gaohui
 * @date 13-8-18 11:00
 */
public interface V13Packet {

    public String getRemoteHost();

    public int getRemotePort();

    public byte[] getPacket();

    /**
     * 协议类型
     */
    public int getPacketType();

    /**
     * 协议版本
     */
    public int getVersion();

    /**
     * 设备类型
     */
    public int getDeviceType();

    /**
     * 包长
     */
    public int getBodyLength();

    /**
     * 子网ID
     */
    public int getNetId();

    /**
     * 路由路数
     */
    public int getJump();

    /**
     * 父节点号
     */
    public int getParentId();

    /**
     * 终端节点ID
     */
    public int getSelfId();

    /**
     * 反馈地址
     */
    public int getFeedback();

    /**
     * 序列号
     */
    public int getSequence();

    /**
     * 供电状态
     */
    public int getVoltage();

    /**
     * RSSI (-128~127)
     */
    public int getRssi();

    /**
     * LQI 正整数
     */
    public int getLqi();

    /**
     * 属性
     * @return
     */
    public Map<String, Object> getAttribute();
}
