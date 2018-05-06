package com.microwise.msp.hardware.handler.agent.command;

/**
 * 设置条件反射指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class ConditionReflCommand extends Command {

    /**
     * 路数
     */
    private int route;

    /**
     * 子节点ID
     */
    private String subNodeId;

    /**
     * 监测指标ID
     */
    private int sensorId;

    /**
     * 低阈值
     */
    private float low;

    /**
     * 高阈值
     */
    private float high;

    /**
     * 动作
     */
    private int action;

    public ConditionReflCommand(String deviceId, int commandId, int sequence, int route, String subNodeId, int sensorId,
                                float low, float high, int action) {
        super(deviceId, commandId, sequence);
        this.route = route;
        this.subNodeId = subNodeId;
        this.sensorId = sensorId;
        this.low = low;
        this.high = high;
        this.action = action;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public String getSubNodeId() {
        return subNodeId;
    }

    public void setSubNodeId(String subNodeId) {
        this.subNodeId = subNodeId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
