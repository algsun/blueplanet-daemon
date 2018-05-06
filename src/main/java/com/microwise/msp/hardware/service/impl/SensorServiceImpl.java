package com.microwise.msp.hardware.service.impl;


import com.microwise.msp.hardware.businessbean.Sensor;
import com.microwise.msp.hardware.dao.SensorDao;
import com.microwise.msp.hardware.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gaohui
 * @date 13-10-23 16:57
 */
@Component
@Scope("prototype")
@Transactional
public class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorDao sensorDao;

    @Override
    public Sensor findById(int sensorPhysicalId) {
        return sensorDao.findById(sensorPhysicalId);
    }

    @Override
    public List<Sensor> findAll() {
        return sensorDao.findByAll();
    }
}
