package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.FloatSensor;
import com.microwise.msp.hardware.dao.FloatSensorDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuzhu
 * @date 2016-5-23
 */
@Repository
@Scope("prototype")
public class FloatSensorDaoImpl extends BaseDaoImpl implements FloatSensorDao {


    @Override
    public List<FloatSensor> floatSensorList(String deviceId) {
        return getSqlSession().selectList("FloatSensor.floatSensorList", deviceId);
    }
}
