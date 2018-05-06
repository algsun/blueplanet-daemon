package com.microwise.msp.proxy.dao;

import com.microwise.msp.proxy.bean.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 实时数据 查询dao层 接口
 * 
 * @author xubaoji
 * @date 2013-1-6
 * 
 * @check 2013-1-10 zhangpeng svn：1107 命名问题
 */
public interface RealtimeDataDao {

	/**
	 * 获得一个站点下所有设备的实时状态
	 * 
	 * @param siteId
	 *            站点编号
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return List<NodeInforBean> 设备实时数据列表（不包括设备监测指标的实时数据）
	 */
	public List<NodeInforBean> getNodeInforList(String siteId) throws Exception;

	/**
	 * 获得一个设备下的所有监测指标的实时数据
	 * 
	 * @param nodeId
	 *            设备编号
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return List<NodesensorBean> 设备下监测指标实时数据信息
	 */
	public List<NodeSensorBean> getNodesensorList(String nodeId)
			throws Exception;

	/**
	 * 获得设备采样时间用于实时数据显示（设备实时表中一组时间中的最大时间）
	 * 
	 * @param nodeId
	 *            设备编号
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return Date 设备实时数据中最大值
	 */
	public Date getNodeInfroStamp(String nodeId) throws Exception;

	/**
	 * 获得一个设备的指定监测指标的24小时数据
	 * 
	 * @param nodeId
	 *            设备编号
	 * @param sensorPhysicalid
	 *            检测指标
	 * @param a
	 *            用来过滤数据的参数
	 * @param b
	 *            用来过滤数据的参数
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return List<Map<String, Date>> 监测指标的24 小时数据 map列表 健是监测值 值是 当前值的采集时间
	 */
	public List<Map<String, Object>> getChartData(String nodeId,
			int sensorPhysicalid, int a, int b) throws Exception;

	/**
	 * 获得一个设备的指定监测指标的24小时数据总数量
	 * 
	 * @param nodeId
	 *            设备编号
	 * @param sensorPhysicalid
	 *            检测指标
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return int 数据量
	 */
	public int getChartDataCount(String nodeId, int sensorPhysicalid)
			throws Exception;

	/**
	 * 获得一个设备指定监测指标的24小时数据的统计信息（最大值，最小值，平均值）
	 * 
	 * @param nodeId
	 *            设备编号
	 * @param sensorPhysicalid
	 *            监测指标标识
	 * @param a
	 *            用来过滤数据的参数
	 * @param b
	 *            用来过滤数据的参数
	 * 
	 * @author 许保吉
	 * @date 2013-1-29
	 * 
	 * @return
	 */
	public Map<String, String> getChartStatData(String nodeId,
			int sensorPhysicalid, int a, int b);

	/**
	 * 获得当前站点下设备拥有的所有已经激活的监测指标（用于实时数据列头展示）
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * @param siteId
	 *            站点编号
	 * @return list<SensorinfoBean> 传感量对象列表
	 */
	public List<SensorInfoBean> getSensorinfoList(String siteId)
			throws Exception;

	/**
	 * 获得当前站点
	 * 
	 * @author 许保吉
	 * @date 2013-1-7
	 * 
	 * @return Map<String, String> map key 是站点编号 value 是站点名称
	 * @throws Exception
	 */
	public SiteBean getCurrentSite() throws Exception;

	/**
	 * 获得当前站点下的所有网关
	 * 
	 * @author 许保吉
	 * @date 2013-2-6
	 * 
	 * @return
	 */
	public List<DeviceBean> getAllGateway();

}
