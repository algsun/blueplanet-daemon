package com.microwise.msp.hardware.handler.codec;

import com.google.common.collect.Lists;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.businessservice.SensorService;
import com.microwise.msp.hardware.common.Defines;

import java.util.Map;

/**
 * @author gaohui
 * @date 13-8-17 21:28
 */
public class DataPacketUtil {

    /**
     * 组装原始传感属性,  不包括衍生出的监测量
     *
     * @param deviceBean
     * @param packet
     */
    public static void assembleSensor(DeviceBean deviceBean, DataPacket packet) {
        for (Map.Entry<Integer, Double> entry : packet.getSensors().entrySet()) {
            SensorPhysicalBean sensor = new SensorPhysicalBean();
            sensor.setSensor_State(Defines._Sensor_State_OK);
            sensor.setSensorPhysical_id(entry.getKey());
            sensor.setSensor_Value(entry.getValue());
            sensor.setValueStr(entry.getValue().toString());

            // 1.3 通过 1.4 协议上来还是 0xFFFF, 如果是 1.4 采样异常是 0xA005
            switch (deviceBean.version) {
                case Versions.V_1:
                    if (entry.getValue() == 0xFFFF) {
                        sensor.setSensor_State(Defines._Sensor_State_Err);
                    }
                    break;
                case Versions.V_3:
                    if (entry.getValue() == 0xFFFF && deviceBean.isDummyV3Version()) {
                        sensor.setSensor_State(Defines._Sensor_State_Err);
                    }
                    break;
            }
            deviceBean.sensorData.put(entry.getKey(), sensor);
        }
    }

    /**
     * 添加衍生的监测指标
     *
     * @param deviceBean
     * @param sensorService
     */
    public static void deriveSensor(DeviceBean deviceBean, SensorService sensorService) {
        // TODO 重构 sensorService.deriveSensor 方法，一次计算出衍生的监测指标

        for (Integer sensorId : Lists.newArrayList(deviceBean.sensorData.keySet())) {
            SensorPhysicalBean sensor = sensorService.deriveSensor(deviceBean.sensorData);
            if (sensor != null && sensor.sensorPhysical_id != 0) {
                deviceBean.sensorData.put(sensor.sensorPhysical_id, sensor);
            }
        }
    }
}
