package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.businessbean.SwitchDailyAction;
import com.microwise.msp.hardware.businessbean.SwitchIntervalAction;
import com.microwise.msp.hardware.businessbean.SwitchSensorAction;
import com.microwise.msp.hardware.businessbean.Switches;
import com.microwise.msp.hardware.dao.SwitchDao;
import com.microwise.msp.hardware.handler.codec.v30.Switch;
import com.microwise.msp.hardware.service.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by basten on 2/20/14.
 */
@Component
@Scope("prototype")
@Transactional
public class SwitchServiceImpl implements SwitchService {

    @Autowired
    private SwitchDao switchDao;

    @Override
    public Switches findLastSwitch(String deviceId) {
        Switches switches = switchDao.findLastSwitch(deviceId);
        if (switches != null) {
            switches.parseToValues();
        }

        return switches;
    }

    @Override
    public void saveSwitchChange(String deviceId, Switches switches, boolean isSwitchCondRefl) {
        for (Switch switcH : switches.getValues()) {
            if (switcH.isChanged()) {
                switchDao.saveSwitchChange(deviceId, switches, switcH.getIndex(), switcH.isOn() ? 1 : 0, isSwitchCondRefl);
            }
        }
    }

    @Override
    public List<SwitchDailyAction> findAllDailyActions() {
        return switchDao.findAllDailyActions();
    }

    @Override
    public List<SwitchIntervalAction> findAllIntervalActions() {
        return switchDao.findAllIntervalActions();
    }

    @Override
    public List<SwitchDailyAction> findDailyActionsByDeviceId(String deviceId) {
        return switchDao.findDailyActionsByDeviceId(deviceId);
    }

    @Override
    public List<SwitchIntervalAction> findIntervalActionsByDeviceId(String deviceId) {
        return switchDao.findIntervalActionsByDeviceId(deviceId);
    }

    @Override
    public SwitchDailyAction findDailyActionById(String id) {
        return switchDao.findDailyActionById(id);
    }

    @Override
    public SwitchIntervalAction findIntervalActionById(String id) {
        return switchDao.findIntervalActionById(id);
    }

    @Override
    public boolean isDeviceUsedForSensorAction(String deviceId) {
        return switchDao.isDeviceUsedForSensorAction(deviceId);
    }

    @Override
    public List<SwitchSensorAction> findSwitchSensorActionsByDeviceId(String deviceId) {
        List<SwitchSensorAction> switchSensorActions = switchDao.findSwitchSensorActionByDeviceId(deviceId);
        for (SwitchSensorAction switchSensorAction : switchSensorActions) {
            String sensorActionId = switchSensorAction.getId();
            switchSensorAction.setSensorConditions(switchDao.findSensorConditionsBySensorActionId(sensorActionId));
        }

        return switchSensorActions;
    }
}
