package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.dao.BaseDao;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.service.DeviceService;
import com.microwise.msp.platform.bean.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-9-16 13:19
 */
@Service
@Scope("prototype")
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Qualifier("BaseDao")
    @Autowired
    private BaseDao baseDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private AppCache appCache;

    @Override
    public List<DeviceBean> findByType(int deviceType) {
        List<DeviceBean> matchedDevices = new ArrayList<DeviceBean>();

        Map<String, Integer> devices = baseDao.getDeviceList();
        for (Map.Entry<String, Integer> entry : devices.entrySet()) {
            if (entry.getValue() == deviceType) {
                DeviceBean device = deviceDao.findById(entry.getKey());
                matchedDevices.add(device);
            }
        }

        return matchedDevices;
    }

    @Override
    public List<DeviceBean> findBySiteId(String siteId) {
        List<DeviceBean> matchedDevices = new ArrayList<DeviceBean>();

        Map<String, Integer> devices = baseDao.getDeviceList();
        for (Map.Entry<String, Integer> entry : devices.entrySet()) {
            if (entry.getKey().startsWith(siteId)) {
                DeviceBean device = deviceDao.findById(entry.getKey());
                matchedDevices.add(device);
            }
        }

        return matchedDevices;
    }

    public List<NodeInfo> findBySiteId2(String siteId) {
        return deviceDao.findBySiteId(siteId);
    }

    @Override
    public DeviceBean findDeviceById(String deviceId) {
        return deviceDao.findById(deviceId);
    }

    @Override
    public String findZoneName(String deviceId) {
        return deviceDao.findZoneName(deviceId);
    }

    @Override
    public boolean setHumCompensate(String deviceId, int flag) {
        boolean result = false;
        DeviceBean device = appCache.loadDevice(deviceId);
        if (device != null && deviceDao.setHumCompensate(deviceId, flag)) {
            device.setHumCompensate(flag);
            result = true;
        }
        return result;
    }
}
