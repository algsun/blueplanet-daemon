package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.vo.LocationSensor;

import java.util.Date;
import java.util.List;

/**
 * 位置点sensorDao
 *
 * @author liuzhu
 * @date 2014-12-11
 */
public interface LocationSensorDao {

    /**
     * 根据位置点id查LocationSensor
     *
     * @param locationId 位置点id
     * @return LocationSensor集合
     */
    public List<LocationSensor> findLocationSensor(String locationId);


    /**
     * 初始化位置点传感信息，location[批量]
     *
     * @param sensorList 传感List
     * @author liuzhu
     * @since 2014-12-11
     */
    public boolean initLocationSensor(List<LocationSensor> sensorList);

    /**
     * 更新实时数据，nodesensor
     *
     * @param device             设备信息
     * @param sensorPhysicalBean 传感信息
     * @author liuzhu
     * @date 2014-12-11
     */
    public void updateLocationSensorMemory(DeviceBean device, SensorPhysicalBean sensorPhysicalBean);


    /**
     * 获取位置点监测指标的值
     *
     * @param locationId 位置点id
     * @param sensorId 监测指标id
     * @return
     *
     * @author liuzhu
     * @date 2015-12-21
     */
    public LocationSensor findLocationSensorData(String locationId, int sensorId);

    /**
     * 删除超时的添加的异常数据
     *
     * @param locationId 位置点id
     * @param stamp 时间戳
     *
     * @author liuzhu
     * @date 2016-9-8
     */
    public void deleteInsertData(String locationId,Date stamp);
}
