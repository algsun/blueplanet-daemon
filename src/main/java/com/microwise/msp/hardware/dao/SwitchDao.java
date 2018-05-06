package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.*;

import java.util.List;

/**
 * Created by basten on 2/20/14.
 */
public interface SwitchDao {

    void saveSwitches(DeviceBean deviceBean);

    /**
     * 返回最后一次开关状态
     *
     * @param deviceId
     * @return
     */
    Switches findLastSwitch(String deviceId);

    /**
     * 保存开关状态变化
     *
     * @param deviceId
     * @param route
     * @param action
     * @param isSwitchCondRefl
     */
    void saveSwitchChange(String deviceId, Switches switches, int route, int action, boolean isSwitchCondRefl);

    /**
     * 返回所有的每日动作
     *
     * @return
     */
    List<SwitchDailyAction> findAllDailyActions();

    SwitchDailyAction findDailyActionById(String id);

    /**
     * 返回设备对应的每日动作
     *
     * @param deviceId
     * @return
     */
    List<SwitchDailyAction> findDailyActionsByDeviceId(String deviceId);

    /**
     * 返回所有周期动作
     *
     * @return
     */
    List<SwitchIntervalAction> findAllIntervalActions();

    SwitchIntervalAction findIntervalActionById(String id);

    /**
     * 返回设备对应的周期动作
     *
     * @param deviceId
     * @return
     */
    List<SwitchIntervalAction> findIntervalActionsByDeviceId(String deviceId);

    /**
     * 判断此设备是否被用作监测指标动作
     *
     * @param deviceId
     * @return
     */
    boolean isDeviceUsedForSensorAction(String deviceId);

    List<SwitchSensorAction> findSwitchSensorActionByDeviceId(String deviceId);

    /**
     * 查询设备参与的所有监测指标动作
     *
     * @param sensorActionId
     * @return
     */
    List<SensorCondition> findSensorConditionsBySensorActionId(String sensorActionId);
}
