package com.microwise.msp.proxy.bean;

import java.util.Map;

/**
 * 设备实时数据
 *
 * @author xubaoji
 * @date 2013-1-18
 */
public class NodeInforBean {

    /**
     * 设备编号
     */
    private String nodeId;

    /**
     * 设备电压信息 0：正常 1：低电压 2：掉电 Y=x/10(实际电压，保留小数点1位)其他情况参考协议内容
     */
    private float voltageState;

    /**
     * 设备工作状态 0：正常 1：异常
     */
    private int deviceState;

    /**
     * 设备工作模式 0：正常模式 1：巡检模式
     */
    private int workingModel;

    /**
     * 采样时间 实时数据显示时采用一组数据中时间最大值
     */
    private long sampleEvent;

    /**
     * 用来存储当前设备下监测指标的实时数据
     */
    private Map<Integer, NodeSensorBean> nodeSensorMap;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public float getVoltageState() {
        return voltageState;
    }

    public void setVoltageState(float voltageState) {
        this.voltageState = voltageState;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getWorkingModel() {
        return workingModel;
    }

    public void setWorkingModel(int workingModel) {
        this.workingModel = workingModel;
    }

    public long getSampleEvent() {
        return sampleEvent;
    }

    public void setSampleEvent(long sampleEvent) {
        this.sampleEvent = sampleEvent;
    }

    public Map<Integer, NodeSensorBean> getNodeSensorMap() {
        return nodeSensorMap;
    }

    public void setNodeSensorMap(Map<Integer, NodeSensorBean> nodeSensorMap) {
        this.nodeSensorMap = nodeSensorMap;
    }

}
