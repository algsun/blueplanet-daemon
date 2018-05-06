package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.DeviceSensorDao;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-3-25 下午3:40
 */
@Component
@Scope("prototype")
public class DeviceSensorDaoImpl extends BaseDaoImpl implements DeviceSensorDao {


    @Override
    public NodeSensor findByDeviceIdAndSensorId(String deviceId, int sensorId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceId", deviceId);
        params.put("sensorId", sensorId);
        return getSqlSession().selectOne("DeviceSensor.findByDeviceIdAndSensorId", params);
    }

}
