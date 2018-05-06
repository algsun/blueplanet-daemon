package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.vo.NodeSensor;

/**
 * 设备传感量
 *
 * @author bastengao
 * @date 14-3-25 下午3:39
 */
public interface DeviceSensorDao {
    NodeSensor findByDeviceIdAndSensorId(String deviceId, int sensorId);
}
