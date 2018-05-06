package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author gaohui
 * @date 13-8-7 16:09
 */
@Component
@Scope("prototype")
public class DeviceSensorLoader extends CacheLoader<String, List<NodeSensor>> {
    @Autowired
    private AnalysisDao analysisDao;

    @Transactional(readOnly = true)
    @Override
    public List<NodeSensor> load(String deviceId) throws Exception {
        return analysisDao.getDeviceSensor(deviceId);
    }
}
