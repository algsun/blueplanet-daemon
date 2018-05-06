/**
 * @author he.ming
 * @since 2013-1-15
 */
package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.DataPacket;
import com.microwise.msp.hardware.businessbean.DeviceBean;

import java.util.Date;
import java.util.List;

/**
 * 
 * 原始数据包dao接口
 * 
 * @author he.ming
 * @since 2013-1-15
 */
public interface DataPacketDao extends BaseDao {

	/**
	 * 创建节点数据包缓存表
	 * 
	 * @author he.ming
	 * @since 2013-1-15
	 * @param nodeId
	 */
	public boolean createPacketTable(String nodeId);

	/**
	 * 判断当前数据包在库中是否已经存在
	 * 
	 * @author he.ming
	 * @since 2013-1-15
	 * @param deviceBean
	 * @return
	 */
	public boolean isExistPacket(DeviceBean deviceBean);

	/**
	 * 保存数据包
	 * 
	 * @author he.ming
	 * @since 2013-1-15
	 * @param deviceBean
	 */
	public boolean savePacket(DeviceBean deviceBean);

	/**
	 * 数据回补 查询接口
	 * 
	 * @param deviceId
	 *            节点Id
	 * @param receiveTime
	 *            时间
	 * @return
	 * @author he.ming
	 * @date May 29, 2012
	 */
	public List<DataPacket> findPackets(String deviceId, Date receiveTime);
}
