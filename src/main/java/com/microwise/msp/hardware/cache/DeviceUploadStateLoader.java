package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author gaohui
 * @date 13-8-7 16:09
 */
@Component
@Scope("prototype")
public class DeviceUploadStateLoader extends CacheLoader<String,Integer> {

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public Integer load(String deviceId) throws Exception {
        return deviceDao.findDeviceUploadState(deviceId);
    }
}
