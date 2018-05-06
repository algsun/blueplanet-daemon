package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DeviceService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.service.LocationSensorService;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置点绑定设备
 *
 * @author li.jianfei
 * @date 2014-06-16
 */
@Component
@Scope("prototype")
@Route("/struts")
public class ConfigureLocationController {

    public static final Logger log = LoggerFactory.getLogger(ConfigureLocationController.class);

    @Autowired
    @Qualifier("DeviceService")
    private DeviceService deviceService;

    @Autowired
    private LocationSensorService locationSensorService;

    @Autowired
    private AppCache appCache;

    /**
     * 节点ID
     */
    private String deviceId;

    @Route(value = "devices/{deviceId}/reload-device-cache")
    public String reloadDeviceCache() {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DeviceBean deviceBean = deviceService.findById(deviceId);
            List<NodeSensor> nodeSensors = appCache.loadDeviceSensors(deviceId);
            // 注意：返回 immutable map
            Map<Integer, NodeSensor> nodeSensorMap = Maps.uniqueIndex(nodeSensors, new Function<NodeSensor, Integer>() {
                @Override
                public Integer apply(NodeSensor nodeSensor) {
                    return nodeSensor.getSensorPhysicalid();
                }
            });
            // 设备监测指标
            deviceBean.setNodeSensors(new HashMap<Integer, NodeSensor>(nodeSensorMap));
            appCache.getDeviceCache().put(deviceBean.deviceid, deviceBean);
            if (!Strings.isNullOrEmpty(deviceBean.locationId)) {
                //locationSensor中以后得监测指标
                List<LocationSensor> sensorList = locationSensorService.findLocationSensor(deviceBean.locationId);


                List<LocationSensor> locationSensors = new ArrayList<LocationSensor>();
                for (NodeSensor sensor : nodeSensors) {
                    boolean existFlag = false;
                    //位置点切换设备时，可绑定不同监测指标的设备
                    for (LocationSensor existSensor : sensorList) {
                        int existSensorId = existSensor.getSensorPhysicalid();
                        int sensorId = sensor.getSensorPhysicalid();
                        if (sensorId == existSensorId) {
                            existFlag = true;
                            break;
                        }
                    }
                    //如果m_location_sensor表中不存在，则初始化监测指标
                    if (!existFlag) {
                        LocationSensor locationSensor = new LocationSensor();
                        locationSensor.setId(StringUtil.uuid());
                        locationSensor.setLocationId(deviceBean.locationId);
                        locationSensor.setDataVersion(Defines._dataVersion_Default);
                        locationSensor.setSensorPhysicalid(sensor.getSensorPhysicalid());
                        locationSensor.setSensorPhysicalValue(Defines._Sensor_Value_Default);
                        locationSensor.setStamp(StringUtil.nowTimestamp());
                        locationSensor.setState(Defines._Sensor_State_OK);
                        locationSensors.add(locationSensor);
                    }
                }
                if(locationSensors.size()>0){
                    locationSensorService.initLocationSensor(locationSensors);

                    // 将位置点的监测指标加入到缓存中
                    Map<Integer, LocationSensor> locationSensorMap = Maps.uniqueIndex(locationSensors, new Function<LocationSensor, Integer>() {
                        @Override
                        public Integer apply(LocationSensor locationSensor) {
                            return locationSensor.getSensorPhysicalid();
                        }
                    });
                    deviceBean.setLocationSensor(new HashMap<Integer, LocationSensor>(locationSensorMap));
                    appCache.getDeviceCache().put(deviceBean.deviceid, deviceBean);
                }
            }
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(result).done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
