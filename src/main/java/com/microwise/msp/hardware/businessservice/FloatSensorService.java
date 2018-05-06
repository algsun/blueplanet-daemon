package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.FloatSensor;
import com.microwise.msp.hardware.dao.FloatSensorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuzhu
 * @date 2016-5-16
 */
@Service
@Scope("prototype")
@Transactional
public class FloatSensorService {

    @Autowired
    private FloatSensorDao floatSensorDao;

    public List<FloatSensor> floatSensorList(String deviceId) {
        return floatSensorDao.floatSensorList(deviceId);
    }
}
