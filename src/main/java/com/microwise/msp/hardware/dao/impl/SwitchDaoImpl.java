package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.dao.SwitchDao;
import com.microwise.msp.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by basten on 2/20/14.
 */
@Component
@Scope("prototype")
public class SwitchDaoImpl extends BaseDaoImpl implements SwitchDao {

    @Override
    public void saveSwitches(DeviceBean deviceBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", StringUtil.uuid());
        params.put("node_id", deviceBean.deviceid);
        params.put("enable", deviceBean.getSwitchEnable());
        params.put("switch", deviceBean.getSwitchStatus());
        params.put("create_time", deviceBean.timeStamp);

        getSqlSession().insert("Switch.saveSwitches", params);
    }

    @Override
    public Switches findLastSwitch(String deviceId) {
        return getSqlSession().selectOne("Switch.findLastSwitchByDeviceId", deviceId);
    }

    @Override
    public void saveSwitchChange(String deviceId, Switches switches, int route, int action, boolean isSwitchCondRefl) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", StringUtil.uuid());
        params.put("deviceId", deviceId);
        params.put("before", switches);
        params.put("after", switches);
        params.put("route", route);
        params.put("action", action);
        params.put("actionDriver", isSwitchCondRefl ? 1 : 0);
        getSqlSession().insert("Switch.saveSwitchChange", params);
    }

    @Override
    public List<SwitchDailyAction> findAllDailyActions() {
        return getSqlSession().selectList("Switch.findDailyActions", null);
    }

    @Override
    public SwitchDailyAction findDailyActionById(String id) {
        return getSqlSession().selectOne("Switch.findDailyActionById", id);
    }

    @Override
    public List<SwitchDailyAction> findDailyActionsByDeviceId(String deviceId) {
        return getSqlSession().selectList("Switch.findDailyActionsByDeviceId", deviceId);
    }

    @Override
    public List<SwitchIntervalAction> findAllIntervalActions() {
        return getSqlSession().selectList("Switch.findIntervalActions", null);
    }

    @Override
    public SwitchIntervalAction findIntervalActionById(String id) {
        return getSqlSession().selectOne("Switch.findIntervalActionById", id);
    }

    @Override
    public List<SwitchIntervalAction> findIntervalActionsByDeviceId(String deviceId) {
        return getSqlSession().selectList("Switch.findIntervalActionsByDeviceId", deviceId);
    }

    @Override
    public boolean isDeviceUsedForSensorAction(String deviceId) {
        int count = getSqlSession().<Integer>selectOne("Switch.isDeviceUsedForSensorAction", deviceId);
        return count != 0;
    }

    @Override
    public List<SwitchSensorAction> findSwitchSensorActionByDeviceId(String deviceId){
        return getSqlSession().selectList("Switch.findSensorActionByDeviceId", deviceId);
    }

    @Override
    public List<SensorCondition> findSensorConditionsBySensorActionId(String sensorActionId) {
        return getSqlSession().selectList("Switch.findSensorConditionByDeviceId", sensorActionId);
    }
}
