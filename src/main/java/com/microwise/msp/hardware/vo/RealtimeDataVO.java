package com.microwise.msp.hardware.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Map;

/**
 * 实时数据vo对象
 *
 * @author li.jianfei
 * @date 2014-07-29
 */
public class RealtimeDataVO {

    /**
     * 位置点编号
     */
    private String locationId;

    /**
     * 位置点名称
     */
    private String locationName;

    /**
     * 区域名称
     */
    private String zoneName;

    /**
     * 设备编号
     */
    private String nodeId;

    /**
     * 设备电压状态 0：正常 1：低电压 2：掉电 Y=x/10(实际电压，保留小数点1位) 其他情况参考协议内容
     */
    private float lowvoltage;

    /**
     * 设备工作状态 0：正常 1：异常 （是否按工作周期采集数据）
     */
    private int anomaly;

    /**
     * 设备工作模式 0：正常模式 1：巡检模式
     */
    private int deviceMode;

    /**
     * 设备接收信号强度
     */
    private Integer rssi;

    /**
     * 设备链路质量
     */
    private Integer lqi;

    /**
     * 设备实时数据采样时间 采用一组数据中时间最大值
     */
    private Date stamp;

    /**
     * 位置点下监测指标的实时数据信息
     */
    private Map<Integer, LocationSensor> sensorinfoMap;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public float getLowvoltage() {
        return lowvoltage;
    }

    public void setLowvoltage(float lowvoltage) {
        this.lowvoltage = lowvoltage;
    }

    public int getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(int anomaly) {
        this.anomaly = anomaly;
    }

    public int getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(int deviceMode) {
        this.deviceMode = deviceMode;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getLqi() {
        return lqi;
    }

    public void setLqi(Integer lqi) {
        this.lqi = lqi;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public Map<Integer, LocationSensor> getSensorinfoMap() {
        return sensorinfoMap;
    }

    public void setSensorinfoMap(Map<Integer, LocationSensor> sensorinfoMap) {
        this.sensorinfoMap = sensorinfoMap;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @Override
    public String toString() {
        return "RealtimeDataVO{" +
                "nodeId='" + nodeId + '\'' +
                ", lowvoltage=" + lowvoltage +
                ", anomaly=" + anomaly +
                ", deviceMode=" + deviceMode +
                ", rssi=" + rssi +
                ", lqi=" + lqi +
                ", stamp=" + stamp +
                ", sensorinfoMap=" + sensorinfoMap +
                '}';
    }
}
