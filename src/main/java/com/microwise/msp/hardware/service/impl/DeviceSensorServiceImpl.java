package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.dao.DeviceSensorDao;
import com.microwise.msp.hardware.service.DeviceSensorService;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bastengao
 * @date 14-3-25 下午3:46
 */
@Component
@Scope("prototype")
@Transactional
public class DeviceSensorServiceImpl implements DeviceSensorService {

    @Autowired
    private DeviceSensorDao deviceSensorDao;


    @Override
    public NodeSensor findByDeviceIdAndSensorId(String deviceId, int sensorId) {
        return deviceSensorDao.findByDeviceIdAndSensorId(deviceId, sensorId);
    }
}
