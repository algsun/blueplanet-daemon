package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.ControlModuleNotification;

import java.util.List;

/**
 * @author bastengao
 * @date 14-4-28 下午2:22
 */
public interface ControlModuleNotificationService {


    List<ControlModuleNotification> findAllBySiteId(String siteId);


    /**
     * 控制模块设置路数备注
     *
     * @param deviceId 设备id
     * @return 路数备注
     */
    String findAlias(String deviceId);
}
