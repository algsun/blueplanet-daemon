package com.microwise.msp.hardware.businessbean;

import java.util.List;

/**
 * 控制开关按条件动作
 *
 * @author bastengao
 * @date 14-3-24 下午4:38
 */
public class SwitchSensorAction {
    // 与
    public static final int LOGIC_AND = 1;
    // 或
    public static final int LOGIC_OR = 2;

    // 开
    public static final int ACTIOIN_ON = 1;
    // 关
    public static final int ACTION_OFF = 0;

    // ID
    private String id;
    // 控制模块ID
    private String controlModuleId;
    // 路数
    private int route;
    // 逻辑
    private int logic;
    // 动作
    private int action;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }

    private List<SensorCondition> sensorConditions;

    public String getControlModuleId() {
        return controlModuleId;
    }

    public void setControlModuleId(String controlModuleId) {
        this.controlModuleId = controlModuleId;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<SensorCondition> getSensorConditions() {
        return sensorConditions;
    }

    public void setSensorConditions(List<SensorCondition> sensorConditions) {
        this.sensorConditions = sensorConditions;
    }
}
