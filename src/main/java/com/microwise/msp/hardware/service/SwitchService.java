package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.SwitchDailyAction;
import com.microwise.msp.hardware.businessbean.SwitchIntervalAction;
import com.microwise.msp.hardware.businessbean.SwitchSensorAction;
import com.microwise.msp.hardware.businessbean.Switches;

import java.util.List;

/**
 * 控制模块开关状态查询
 *
 * Created by basten on 2/20/14.
 */
public interface SwitchService {

    /**
     * 查询最新开关状态
     * TODO
     *
     * @param deviceId
     * @return
     */
    Switches findLastSwitch(String deviceId);

    /**
     * 保存开关状态变化
     *
     * @param deviceId
     * @param switches
     * @param isSwitchCondRefl
     */
    void saveSwitchChange(String deviceId, Switches switches,boolean isSwitchCondRefl);

    /**
     * 返回所有的每日动作
     *
     * @return
     */
    List<SwitchDailyAction> findAllDailyActions();

    /**
     * 返回所有周期动作
     *
     * @return
     */
    List<SwitchIntervalAction> findAllIntervalActions();

    /**
     * 返回设备对应的每日动作
     *
     * @param deviceId
     * @return
     */
    List<SwitchDailyAction> findDailyActionsByDeviceId(String deviceId);

    /**
     * 返回设备对应的周期动作
     *
     * @param deviceId
     * @return
     */
    List<SwitchIntervalAction> findIntervalActionsByDeviceId(String deviceId);

    SwitchDailyAction findDailyActionById(String id);

    SwitchIntervalAction findIntervalActionById(String id);

    /**
     * 判断此设备是否被用作监测指标动作
     *
     * @param deviceId
     * @return
     */
    boolean isDeviceUsedForSensorAction(String deviceId);

    List<SwitchSensorAction> findSwitchSensorActionsByDeviceId(String deviceId);
}
