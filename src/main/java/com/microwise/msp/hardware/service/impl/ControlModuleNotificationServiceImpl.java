package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.businessbean.ControlModuleNotification;
import com.microwise.msp.hardware.dao.ControlModuleNotificationDao;
import com.microwise.msp.hardware.service.ControlModuleNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bastengao
 * @date 14-4-28 下午2:22
 */
@Component
@Scope("prototype")
@Transactional
public class ControlModuleNotificationServiceImpl implements ControlModuleNotificationService {

    @Autowired
    private ControlModuleNotificationDao notificationDao;


    @Override
    public List<ControlModuleNotification> findAllBySiteId(String siteId) {
        List<ControlModuleNotification> notifications = notificationDao.findAllBySiteId(siteId);
        for(ControlModuleNotification notification: notifications){
            notification.setDeviceIds(notificationDao.findDevicesByNotificationId(notification.getId()));
        }

        return notifications;
    }

    @Override
    public String findAlias(String deviceId) {
        return notificationDao.findAlias(deviceId);
    }
}
