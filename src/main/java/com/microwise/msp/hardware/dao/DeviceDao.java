package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.ConditionRefl;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.platform.bean.NodeInfo;

import java.util.Date;
import java.util.List;

/**
 * 设备
 *
 * @author gaohui
 * @date 13-11-8 11:14
 */
public interface DeviceDao extends BaseDao {

    /**
     * 更新设备类型
     *
     * @param deviceId
     * @param deviceType
     */
    void updateDeviceType(String deviceId, int deviceType);

    /**
     * 更新产品序列号
     *
     * @param deviceId
     * @param sn
     */
    public void updateDeviceSn(String deviceId, String sn);

    /**
     * 更新上传状态
     *
     * @param deviceId
     */
    public void updateUploadState(String deviceId);

    /**
     * 根据设备ID返回设备
     *
     * @param deviceId
     * @return
     */
    DeviceBean findById(String deviceId);

    /**
     * 查询站点下所有设备
     * @siteId 站点ID
     */
    public List<NodeInfo> findBySiteId(String siteId);

    /**
     * 返回所有的设备
     *
     * @return
     */
    List<DeviceBean> findAll();

    /**
     * 查询设备上传状态
     *
     * @param deviceId 设备id
     * @return
     */
    Integer findDeviceUploadState(String deviceId);

    /**
     * 创建设备信息表
     *
     * @param deviceId
     */
    void createDeviceStatusTable(String deviceId);

    /**
     * 添加设备信息
     *
     * @param deviceBean
     */
    void addDeviceStatusHistory(DeviceBean deviceBean);

    /**
     * 保存设备条件反射参数
     *
     * @param deviceId
     * @param conditionRefl
     */
    ConditionRefl saveConditionRefl(String deviceId, ConditionRefl conditionRefl);

    /**
     * 更新设备条件反射参数
     *
     * @param id
     * @param conditionRefl
     */
    ConditionRefl updateConditionRefl(String id, ConditionRefl conditionRefl);

    /**
     * 根据设备ID和路数查找条件反射参数
     *
     * @param deviceId
     * @param route
     * @return
     */
    ConditionRefl findConditionRefl(String deviceId, int route);

    /**
     * 根据deviceId查区域名称
     *
     * @param deviceId 设备id
     * @return 区域名称
     */
    String findZoneName(String deviceId);

    /**
     * 修改位置点的绑定关系
     *
     * @param locationId
     * @param deviceId
     */
    void addLocationBindRelationShip(String locationId, String deviceId, Date startTime, Date endTime);

    /**
     * 根据主模块设备id查询从模块列表
     *
     * @param masterModuleDeviceId 主模块设备id
     * @return List<DeviceBean> 从模块设备vo对象列表
     * @author chenyaofei
     * @date 2016-10-20
     */
    public List<DeviceBean> findSlaveModule(String masterModuleDeviceId);


    boolean setHumCompensate(String deviceId, int flag);
}
