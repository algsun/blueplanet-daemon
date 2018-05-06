package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.dao.LocationSensorDao;
import com.microwise.msp.hardware.service.LocationSensorService;
import com.microwise.msp.hardware.vo.LocationSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by amin on 14-12-11.
 */
@Service
@Scope("prototype")
@Transactional
public class LocationSensorServiceImpl implements LocationSensorService {

    @Autowired
    private LocationSensorDao locationSensorDao;

    @Override
    public boolean initLocationSensor(List<LocationSensor> sensorList) {
        return locationSensorDao.initLocationSensor(sensorList);
    }

    @Override
    public List<LocationSensor> findLocationSensor(String locationId) {
        return locationSensorDao.findLocationSensor(locationId);
    }
}
