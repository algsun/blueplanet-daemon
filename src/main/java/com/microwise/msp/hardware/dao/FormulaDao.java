package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Formula;

import java.util.List;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-12-31 14:15
 */
public interface FormulaDao {

    /**
     * 根据监测指标查找对应的公式
     *
     * @param sensorId
     * @return
     */
    public Formula findBySensorId(int sensorId);

    /**
     * 根据设备ID返回自定义公式
     *
     * @param deviceId
     * @param senorId
     * @return
     */
    public Formula findByDeviceId(String deviceId, int senorId);

    /**
     * 根据监测指标ID查找对应的公式系数
     *
     * @param sensorId
     * @return
     */
    List<Map<String, String>> findParamsBySensorId(int sensorId);

    /**
     * 根据设备ID查找自定义公式系数
     *
     * @param deviceId
     * @param sensorId
     * @return
     */
    List<Map<String, String>> findParamsByDeviceId(String deviceId, int sensorId);

    /**
     * 返回某个设备所有的公式系数
     *
     * sensorId => params
     * @param deviceId
     * @return
     */
    Map<Integer, Map<String, String>> findParamsByDeviceId(String deviceId);

    /**
     * 返回一个设备的所有自定义公式
     *
     * @param deviceId
     * @return
     */
    List<Formula> findAllByDeviceId(String deviceId);

    /**
     * 删除某个设备某个监测指标自定义公式系数
     *
     * @param deviceId
     * @param sensorId
     */
    void deleteParamsByDeviceIdAndSensorId(String deviceId, int sensorId);

    /**
     * 添加某个设备某个监测指标自定义公式系数
     *
     * @param deviceId
     * @param sensorId
     * @param name
     * @param value
     */
    void saveCustomParam(String deviceId, int sensorId, String name, String value);
}
