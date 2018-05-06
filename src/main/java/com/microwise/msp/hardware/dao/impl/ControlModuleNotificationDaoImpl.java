package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.ControlModuleNotification;
import com.microwise.msp.hardware.dao.ControlModuleNotificationDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bastengao
 * @date 14-4-28 上午11:08
 */
@Component
@Scope("prototype")
public class ControlModuleNotificationDaoImpl extends BaseDaoImpl implements ControlModuleNotificationDao {

    @Override
    public List<ControlModuleNotification> findAllBySiteId(String siteId){
        return getSqlSession().selectList("ControlModuleNotification.findAllBySiteId", siteId);
    }

    @Override
    public List<String> findDevicesByNotificationId(String notificationId){
        return getSqlSession().selectList("ControlModuleNotification.findDevicesByNotificationId", notificationId);
    }

    @Override
    public String findAlias(String deviceId) {
        return getSqlSession().selectOne("ControlModuleNotification.findAlias",deviceId);
    }

}
