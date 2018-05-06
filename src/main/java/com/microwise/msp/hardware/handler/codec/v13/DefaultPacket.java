package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * v1.3 常规包
 *
 * @author gaohui
 * @date 13-8-13 10:13
 */
public abstract class DefaultPacket extends Packet implements V13Packet {
    public DefaultPacket() {
    }

    public DefaultPacket(Packet packet) {
        super(packet);
    }

    /** 子网ID */
    protected int netId;
    /** 路由路数 */
    protected int jump;
    /** 父节点号 */
    private int parentId;
    /** 终端节点ID */
    protected int selfId;
    /** 反馈地址 */
    protected int feedback;
    /** 序列号 */
    protected int sequence;
    /** 供电状态 */
    protected int voltage;
    /** RSSI (-128~127) */
    protected int rssi;
    /** LQI 正整数 */
    protected int lqi;

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSelfId() {
        return selfId;
    }

    public void setSelfId(int selfId) {
        this.selfId = selfId;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getLqi() {
        return lqi;
    }

    public void setLqi(int lqi) {
        this.lqi = lqi;
    }
}
