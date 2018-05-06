package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.Sensor;

import java.util.List;

/**
 * 监测指标 service
 *
 * @author gaohui
 * @date 13-10-23 16:57
 */
public interface SensorService {
    List<Sensor> findAll();

    Sensor findById(int sensorPhysicalId);
}
