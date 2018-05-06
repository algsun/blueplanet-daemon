package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.platform.bean.NodeInfo;

import java.util.List;

/**
 * @author gaohui
 * @date 13-9-16 13:18
 */
public interface DeviceService {

    /**
     * 返回符合类型的所有设备, 如果没有返回空的集合
     *
     * @param deviceType
     * @return
     */
    List<DeviceBean> findByType(int deviceType);

    /**
     * 根据站点ID查看整个站点下的所有设备
     *
     * @param siteId
     * @return
     */
    List<DeviceBean> findBySiteId(String siteId);

    /**
     * 根据站点ID查看整个站点下的所有设备
     *
     * @param siteId
     * @return
     */
    List<NodeInfo> findBySiteId2(String siteId);

    /**
     * 根据ID号查找设备
     *
     * @param deviceId
     * @return
     */
    DeviceBean findDeviceById(String deviceId);

    /**
     * 根据deviceId查区域名称
     *
     * @param deviceId 设备id
     * @return 区域名称
     */
    String findZoneName(String deviceId);

    /**
     * 设置湿度补偿开关
     *
     * @param flag
     * @return
     */
    boolean setHumCompensate(String deviceId, int flag);
}
