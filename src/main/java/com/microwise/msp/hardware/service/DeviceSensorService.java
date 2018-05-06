package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.vo.NodeSensor;

/**
 * @author bastengao
 * @date 14-3-25 下午3:46
 */
public interface DeviceSensorService {
    NodeSensor findByDeviceIdAndSensorId(String deviceId, int sensorId);
}
