package com.microwise.msp.proxy.service;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 实时数据webservice服务层接口
 * 
 * @author xubaoji
 * @date 2013-1-6
 * 
 * @check 2013-1-10 zhangpeng svn：1107 未运行测试
 */
@WebService
public interface RealtimeDataWS {

	/**
	 * 获得一个站点下所有设备的实时数据
	 * 
	 * @param siteId
	 *            站点编号
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return String
	 */
	public String getRealtimeData(@WebParam(name = "siteId") String siteId);

	/**
	 * 获得一个设备指定检测指标的24小时数据 用于呈现实时图形
	 * 
	 * @param nodeId
	 *            设备编号
	 * @param sensorId
	 *            检测指标标识
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return String
	 */
	public String getChartData(@WebParam(name = "nodeId") String nodeId,
			@WebParam(name = "sensorId") int sensorId);

	/**
	 * 获得当前站点 （这里所说的站点指代的是site 与logicGroup无关）
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * 
	 * @return String
	 */
	public String getCurrentSite();

	/**
	 * 获得当前站点下设备拥有的已经激活的监测指标（用于实时数据列头显示）
	 * 
	 * @author 许保吉
	 * @date 2013-1-6
	 * @param siteId
	 *            站点编号
	 * @return String
	 */
	public String getSensorInfo(@WebParam(name = "siteId") String siteId);

	/**
	 * 获得当前站点下的所有网关
	 * 
	 * @author 许保吉
	 * @date 2013-2-6
	 * 
	 * @return
	 */
	public String getAllGateway();

	/**
	 * 巡检接口
	 * 
	 * @param polling
	 *            开true,关false
	 * @param deviceIds
	 *            网关IdList
	 * @param interval
	 *            工作周期
	 * @author he.ming
	 * @since Feb 7, 2013
	 */
	public String polling(@WebParam(name = "polling") boolean polling,
			@WebParam(name = "deviceIds") String deviceIds,
			@WebParam(name = "interval") int interval);

}
