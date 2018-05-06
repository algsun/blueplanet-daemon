package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author liuzhu
 * @date 2015-6-5
 */
@Component
@Scope("prototype")
public class LocationSensorLoader extends CacheLoader<String, List<LocationSensor>> {
    @Autowired
    private AnalysisDao analysisDao;

    @Transactional(readOnly = true)
    @Override
    public List<LocationSensor> load(String locationId) throws Exception {
        return analysisDao.getLocationSensor(locationId);
    }
}
