package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.Sensor;
import com.microwise.msp.hardware.dao.SensorDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监测指标
 *
 * @author gaohui
 * @date 13-7-19 11:10
 */
@Component
@Scope("prototype")
public class SensorDaoImpl extends BaseDaoImpl implements SensorDao {

    @Override
    public Sensor findById(int sensorPhysicalId) {
        return (Sensor) getSqlSession().selectOne("Sensor.findById", sensorPhysicalId);
    }

    @Override
    public List<Sensor> findByAll(){
        return getSqlSession().selectList("Sensor.findAll");
    }
}
