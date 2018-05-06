package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Sensor;

import java.util.List;

/**
 * @author gaohui
 * @date 13-7-19 11:09
 */
public interface SensorDao {
    /**
     * 根据 sensorPhysicalId 查找监测指标
     *
     * @param sensorPhysicalId
     * @return
     */
    Sensor findById(int sensorPhysicalId);

    /**
     * 返回所有监测指标
     *
     * @return
     */
    List<Sensor> findByAll();
}
