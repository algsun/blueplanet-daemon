package com.microwise.msp.hardware.handler.codec;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 上行数据包
 *
 * @author gaohui
 * @date 13-8-6 14:50
 */
public class DataPacket extends Packet {
    public DataPacket() {
    }

    public DataPacket(Packet packet) {
        super(packet);
    }

    /**
     * 父节点号
     */
    private int parentId;
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
     * 参数区
     * <p/>
     * v1.3 只包含监测指标与值
     * <p/>
     * v3.0 包含监测指标参数与终端参数
     */
    protected Map<Integer, Double> sensors;

    /**
     * 是否可反控
     * 0. 可反控
     * 1.不可反控制
     * <p/>
     * v1.3 无此字段，默认为不可控
     */
    protected int isControl;

    /**
     * 站点ID
     */
    protected String siteId;
    /**
     * 设备ID
     */
    protected String deviceId;
    /**
     * 工作周期
     */
    protected int interval;
    /**
     * 时间戳
     */
    protected Timestamp timestamp;
    /**
     * 工作模式(0: 正常, 1：巡检)
     */
    protected int workMode;


    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSelfId() {
        return selfId;
    }

    public void setSelfId(int selfId) {
        this.selfId = selfId;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    public void setControl(int control) {
        isControl = control;
    }

    public Map<Integer, Double> getSensors() {
        return sensors;
    }

    public void setSensors(Map<Integer, Double> sensors) {
        this.sensors = sensors;
    }
}
