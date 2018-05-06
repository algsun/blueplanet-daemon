package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.vo.LocationSensor;

import java.util.List;

/**
 * Created by amin on 14-12-11.
 */
public interface LocationSensorService {

    /**
     * 初始化位置点传感信息，location[批量]
     *
     * @param sensorList 传感List
     * @author liuzhu
     * @since 2014-12-11
     */
    public boolean initLocationSensor(List<LocationSensor> sensorList);

    /**
     * 根据位置点id查LocationSensor
     *
     * @param locationId 位置点id
     * @return LocationSensor集合
     */
    public List<LocationSensor> findLocationSensor(String locationId);

}
