package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.FloatSensor;

import java.util.List;

/**
 * @author liuzhu
 * @date 2016-5-23
 */
public interface FloatSensorDao {

    /**
     * 根据监测指标查找对应的公式
     *
     * @param deviceId
     * @return
     */
    public List<FloatSensor> floatSensorList(String deviceId);

}
