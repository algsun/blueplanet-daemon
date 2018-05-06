package com.microwise.msp.hardware.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.dao.LocationDao;
import com.microwise.msp.hardware.dao.ThresholdDao;
import com.microwise.msp.hardware.dao.ZoneDao;
import com.microwise.msp.hardware.service.ThresholdService;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bastengao
 * @date 14-3-19 下午2:54
 */
@Component("thresholdAlarmService2")
@Scope("prototype")
@Transactional
public class ThresholdServiceImpl implements ThresholdService {

    @Autowired
    private AppCache appCache;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private ThresholdDao thresholdDao;

    @Autowired
    private ZoneDao zoneDao;

    public List<Threshold> findThresholds(String deviceId) {
        // TODO 位置点只设置了一个监测指标报警条件,默认存在10条,优先使用默认报警条件?代码倒序
        DeviceBean device = appCache.loadDevice(deviceId);
        String locationId = device.locationId;

        // 如果当前设备未绑定位置点直接退出
        if (Strings.isNullOrEmpty(locationId)) return null;

        // 查询位置点报警条件
        List<Threshold> thresholds = thresholdDao.findThresholds(locationId);

        // 如果位置点报警条件为空,查询区域报警条件
//        if (thresholds.isEmpty()) {
//            // 如果位置点报警条件为空,使用默认报警条件
//            // 查询监测指标默认达标条件
//            thresholds = Lists.newArrayList();
//
//            for (NodeSensor nodeSensor : appCache.loadDeviceSensors(deviceId)) {
//                Sensor sensor = appCache.loadSensor(nodeSensor.getSensorPhysicalid());
//                if (null != sensor) {
//                    // conditionType == 0 标识未设置达标条件
//                    if (sensor.getConditionType() != 0) {
//                        Threshold threshold = new Threshold();
//                        threshold.setSensorId(sensor.getPhysicalId());
//                        threshold.setConditionType(sensor.getConditionType());
//                        threshold.setTarget(sensor.getTarget());
//                        threshold.setFloating(sensor.getFloating());
//                        thresholds.add(threshold);
//                    }
//                }
//            }
//        }
        return thresholds;
    }


    @Override
    public void addAlarmHistory(ThresholdAlarmHistory history) {
        thresholdDao.appendAlarmHistory(history);

//        List<Measure> measures = thresholdDao.findMeasuresByZoneId(history.getLocationId());
//        for (Measure measure : measures) {
//            thresholdDao.appendAlarmHistoryMeasure(history.getId(), measure.getId());
//        }
    }

}
