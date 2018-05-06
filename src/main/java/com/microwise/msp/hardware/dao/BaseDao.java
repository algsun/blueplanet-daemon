/**
 * 
 */
package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 数据持久化基础类接口
 * </pre>
 * 
 * @since 2011-09-22
 * @author heming
 * 
 */
public interface BaseDao {

	/**
	 * <pre>
	 * 得到当前节点列表
	 * 获取设备列表Map[deviceid, type]
	 * </pre>
	 * 
	 * @return 节点列表
	 */
	public Map<String, Integer> getDeviceList();

	/**
	 * <pre>
	 * 获取节点的传感信息(为了解决同一节点传感信息发生变更的需求)
	 * </pre>
	 * 
	 * @param deviceId
	 *            节点号
	 * @return List[NodeSensor]
	 */
	public List<NodeSensor> getDeviceSensor(String deviceId);

    /**
     * <pre>
     * 获取节点的传感信息(为了解决同一节点传感信息发生变更的需求)
     * </pre>
     *
     * @param locationId
     *            节点号
     * @return List[LocationSensor]
     */
    public List<LocationSensor> getLocationSensor(String locationId);

	/**
	 * <pre>
	 * 判断表是否存在
	 * </pre>
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public boolean isExistTable(String tableName);

}
