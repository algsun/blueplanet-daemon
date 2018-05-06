package com.microwise.msp.hardware.cache;

import com.google.common.base.Function;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.service.LocationSensorService;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenyaofei
 * @date 15-12-9
 */
@Component
@Scope("prototype")
public class DeviceCacheLoader extends CacheLoader<String, DeviceBean> {
    public static final Logger log = LoggerFactory.getLogger(DeviceCacheLoader.class);

    @Autowired
    public DeviceDao deviceDao;
    @Autowired
    AppCache appCache;
    @Autowired
    LocationSensorService locationSensorService;

    @Override
    public DeviceBean load(String deviceId) throws Exception {
        DeviceBean device;
        try {
            device = deviceDao.findById(deviceId);
            if (device == null) {
                return null;
            }

            List<NodeSensor> nodeSensors = appCache.loadDeviceSensors(deviceId);
            String locationId = device.locationId;
            List<LocationSensor> locationSensors;
            if (!Strings.isEmpty(locationId)) {
                locationSensors = appCache.loadLocationSensors(device.locationId);
            } else {
                locationSensors = new ArrayList<LocationSensor>();
            }
            // 注意：返回 immutable map
            Map<Integer, NodeSensor> nodeSensorMap = Maps.uniqueIndex(nodeSensors, new Function<NodeSensor, Integer>() {
                @Override
                public Integer apply(NodeSensor nodeSensor) {
                    return nodeSensor.getSensorPhysicalid();
                }
            });

            Map<Integer, LocationSensor> LocationSensorMap = Maps.uniqueIndex(locationSensors, new Function<LocationSensor, Integer>() {
                @Override
                public Integer apply(LocationSensor locationSensor) {
                    return locationSensor.getSensorPhysicalid();
                }
            });
            // 设备监测指标
            device.setNodeSensors(new HashMap<Integer, NodeSensor>(nodeSensorMap));
            device.setLocationSensor(new HashMap<Integer, LocationSensor>(LocationSensorMap));
            // 设备信息加载至内存
            appCache.getDeviceCache().put(deviceId, device);
        } catch (Exception e) {
            log.error("初始化设备", e);
            throw e;
        }
        return device;
    }
}
