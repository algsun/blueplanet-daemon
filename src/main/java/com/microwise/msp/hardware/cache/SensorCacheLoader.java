package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessbean.Sensor;
import com.microwise.msp.hardware.dao.SensorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 监测指标缓存
 *
 * @author gaohui
 * @date 13-7-19 10:08
 */
@Component
@Scope("prototype")
public class SensorCacheLoader extends CacheLoader<Integer, Sensor> {

    @Autowired
    private SensorDao sensorDao;


    @Transactional(readOnly = true, noRollbackFor = IllegalArgumentException.class)
    @Override
    public Sensor load(Integer sensorPhysicalId) throws Exception {
        Sensor sensor = sensorDao.findById(sensorPhysicalId);
        if(sensor == null){
            throw new Exception("未找到对应监测指标: " + sensorPhysicalId);
        }

        return sensor;
    }
}
