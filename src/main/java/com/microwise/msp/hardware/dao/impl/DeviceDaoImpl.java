package com.microwise.msp.hardware.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.ConditionRefl;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.platform.bean.NodeInfo;
import com.microwise.msp.util.JsonUtil;
import com.microwise.msp.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-11-8 11:14
 */
@Repository
@Scope("prototype")
public class DeviceDaoImpl extends BaseDaoImpl implements DeviceDao {

    @Override
    public DeviceBean findById(String deviceId) {
        return getSqlSession().selectOne("Device.findDeviceById", deviceId);
    }

    public List<NodeInfo> findBySiteId(String siteId) {
        return getSqlSession().selectList("Device.findBySiteId", siteId);
    }

    @Override
    public List<DeviceBean> findAll() {
        return getSqlSession().selectList("Device.findDeviceAll");
    }

    @Override
    public Integer findDeviceUploadState(String deviceId) {
        return getSqlSession().selectOne("Device.findUploadState", deviceId);
    }

    @Override
    public void updateDeviceType(String deviceId, int deviceType) {
        DeviceBean deviceBean = new DeviceBean();
        deviceBean.deviceid = deviceId;
        deviceBean.deviceType = deviceType;
        getSqlSession().update("Device.updateDeviceType", deviceBean);
    }

    public void updateDeviceSn(String deviceId, String sn) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("deviceId", deviceId);
        param.put("sn", sn);
        getSqlSession().update("Device.updateDeviceSn", param);
    }

    public void updateUploadState(String deviceId) {
        getSqlSession().update("Device.updateUploadState", deviceId);
    }


    @Override
    public void createDeviceStatusTable(String deviceId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(deviceId), "设备ID不能为null或者空");

        getSqlSession().update("Device.createDeviceStatusTable", deviceId);
    }

    @Override
    public void addDeviceStatusHistory(DeviceBean deviceBean) {
        String content = JsonUtil.toJson(deviceBean.getDeviceProperties());
        if (!content.trim().equals("{}")) {
            deviceBean.setContent(content);
        }
        getSqlSession().update("Device.addDeviceStatusHistory", deviceBean);
    }

    @Override
    public ConditionRefl saveConditionRefl(String deviceId, ConditionRefl conditionRefl) {
        Map<String, Object> params = new HashMap<String, Object>();
        String uuid = StringUtil.uuid();
        params.put("id", StringUtil.uuid());
        params.put("deviceId", deviceId);
        params.put("refl", conditionRefl);

        conditionRefl.setId(uuid);
        conditionRefl.setDeviceId(deviceId);

        getSqlSession().insert("Device.saveConditionRefl", params);

        return conditionRefl;
    }

    @Override
    public ConditionRefl updateConditionRefl(String id, ConditionRefl conditionRefl) {
        conditionRefl.setId(id);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("refl", conditionRefl);

        getSqlSession().update("Device.updateConditionRefl", params);

        return conditionRefl;
    }

    @Override
    public ConditionRefl findConditionRefl(String deviceId, int route) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceId", deviceId);
        params.put("route", route);
        return getSqlSession().selectOne("Device.findConditionRefl", params);
    }

    @Override
    public String findZoneName(String deviceId) {
        return getSqlSession().selectOne("Device.findZoneName", deviceId);
    }

    @Override
    public void addLocationBindRelationShip(String locationId, String deviceId, Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nodeId", deviceId);
        params.put("locationId", locationId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        getSqlSession().update("Device.addLocationBindRelationShip", params);
    }

    @Override
    public List<DeviceBean> findSlaveModule(String masterModuleDeviceId) {
        return getSqlSession().selectList("Device.findSlaveModule", masterModuleDeviceId);
    }

    @Override
    public boolean setHumCompensate(String deviceId, int flag) {
        boolean result = false;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("deviceId", deviceId);
            params.put("flag", flag);
            result = (getSqlSession().update("Device.setHumCompensate", params) == 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
