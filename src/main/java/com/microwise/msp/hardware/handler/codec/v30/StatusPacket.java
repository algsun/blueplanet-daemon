package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.businessbean.Threshold;
import com.microwise.msp.hardware.handler.codec.Packet;

import java.util.List;

/**
 * @author gaohui
 * @date 14-1-17 13:19
 */
public class StatusPacket extends Packet {

    /**
     * 父节点号
     */
    protected int parentId;
    /**
     * 终端节点ID
     */
    protected int selfId;
    /**
     * 序列号
     */
    protected int sequence;
    /**
     * 供电状态
     */
    protected int voltage;
    /**
     * RSSI (-128~127)
     */
    protected int rssi;
    /**
     * LQI 正整数
     */
    protected int lqi;
    /**
     * 工作周期
     */
    protected int interval;
    /**
     * 工作模式(0: 正常, 1：巡检)
     */
    protected int workMode;
    /**
     * 是否可反控
     * 0. 可反控
     * 1.不可反控制
     * <p>
     * v1.3 无此字段，默认为不可控
     */
    protected int isControl;
    /**
     * 产品序列号
     */
    protected String serialNumber;

    /**
     * 标定状态
     * 1.标定
     * 2.非标定
     */
    protected int demarcate;
    /**
     * 站点ID
     */
    protected String siteId;
    /**
     * 搜网次数
     */
    protected int connectionCount;

    protected List<Threshold> thresholdList;

    /**
     * 阈值报警启用状态
     * <p>
     * 0-禁用
     * 1-启用
     * <p>
     */
    protected int isThresholdAlarm;

    /**
     * 设备属性 用后两个bit 标识：1：生产阶段（默认）2：试用阶段
     */
    protected int deviceProperty;

    public StatusPacket() {
    }

    public StatusPacket(Packet packet) {
        super(packet);
    }

    public int getDeviceProperty() {
        return deviceProperty;
    }

    public void setDeviceProperty(int deviceProperty) {
        this.deviceProperty = deviceProperty;
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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getControl() {
        return isControl;
    }

    public void setControl(int isControl) {
        this.isControl = isControl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDemarcate() {
        return demarcate;
    }

    public void setDemarcate(int demarcate) {
        this.demarcate = demarcate;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public int getIsControl() {
        return isControl;
    }

    public void setIsControl(int isControl) {
        this.isControl = isControl;
    }

    public int getIsThresholdAlarm() {
        return isThresholdAlarm;
    }

    public void setIsThresholdAlarm(int isThresholdAlarm) {
        this.isThresholdAlarm = isThresholdAlarm;
    }

    public void setThresholdList(List<Threshold> thresholdList) {
        this.thresholdList = thresholdList;
    }

    @Override
    public String toString() {
        return "StatusPacket{" +
                "parentId=" + parentId +
                ", selfId=" + selfId +
                ", sequence=" + sequence +
                ", voltage=" + voltage +
                ", rssi=" + rssi +
                ", lqi=" + lqi +
                ", interval=" + interval +
                ", workMode=" + workMode +
                ", isControl=" + isControl +
                ", serialNumber=" + serialNumber +
                ", siteId='" + siteId + '\'' +
                ", connectionCount=" + connectionCount +
                ", isThresholdAlarm=" + isThresholdAlarm +
                '}';
    }
}
