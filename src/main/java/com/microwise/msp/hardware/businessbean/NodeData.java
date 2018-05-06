package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xiedeng
 * @date 14-10-16
 */
public class NodeData implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String nodeId;

    private int sensorPhysicalId;

    private float sensorPhysicalValue;

    private float voltage;

    private Timestamp createTime;

    private int state;

    private int anomaly;

    public float getSensorPhysicalValue() {
        return sensorPhysicalValue;
    }

    public void setSensorPhysicalValue(float sensorPhysicalValue) {
        this.sensorPhysicalValue = sensorPhysicalValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getSensorPhysicalId() {
        return sensorPhysicalId;
    }

    public void setSensorPhysicalId(int sensorPhysicalId) {
        this.sensorPhysicalId = sensorPhysicalId;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(int anomaly) {
        this.anomaly = anomaly;
    }
}