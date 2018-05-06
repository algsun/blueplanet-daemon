package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.ControlModuleNotification;

import java.util.List;

/**
 * @author bastengao
 * @date 14-4-28 上午11:07
 */
public interface ControlModuleNotificationDao {

    List<ControlModuleNotification> findAllBySiteId(String siteId);

    List<String> findDevicesByNotificationId(String notificationId);

    /**
     * 控制模块设置路数备注
     *
     * @param deviceId 设备id
     * @return 路数备注
     */
    String findAlias(String deviceId);
}
