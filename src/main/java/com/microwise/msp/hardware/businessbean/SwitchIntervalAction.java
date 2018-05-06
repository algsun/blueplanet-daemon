package com.microwise.msp.hardware.businessbean;

/**
 * 控制模块周期动作对象
 *
 * @since 2014-03-05
 */
public class SwitchIntervalAction extends SwitchAction {

    /**
     * 间隔时间 单位:秒
     */
    private int intervalTime;

    /**
     * 执行时间 单位:秒
     */
    private int executionTime;

    /**
     * 执行动作 1开 0关
     */
    private int action;

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}