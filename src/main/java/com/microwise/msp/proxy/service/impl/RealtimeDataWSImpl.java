package com.microwise.msp.proxy.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.proxy.bean.*;
import com.microwise.msp.proxy.dao.RealtimeDataDao;
import com.microwise.msp.proxy.service.RealtimeDataWS;
import com.microwise.msp.util.AppContext;
import com.microwise.msp.util.WindTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 实时数据webservice服务层实现
 * 
 * @author xubaoji
 * @date 2013-1-6
 * 
 * @check 2013-1-10 zhangpeng svn：1107 json封装方式
 */
@WebService(targetNamespace = "http://service.proxy.msp.microwise.com", endpointInterface = "com.microwise.msp.proxy.service.RealtimeDataWS")
public class RealtimeDataWSImpl implements RealtimeDataWS {

	private static Logger log = LoggerFactory.getLogger(RealtimeDataWSImpl.class);

	private RealtimeDataDao realtimeDataDao;

	@Override
	public String getRealtimeData(String siteId) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
				.create();
		GalaxyResult<List<NodeInforBean>> galaxyResult = new GalaxyResult<List<NodeInforBean>>();
		galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		if (siteId == null || "".equals(siteId)) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_PARAM);
			return gson.toJson(galaxyResult);
		}
		try {
			// 获得所有设备信息列表
			List<NodeInforBean> nodeInforlist = realtimeDataDao
					.getNodeInforList(siteId);
			if (nodeInforlist != null && nodeInforlist.size() > 0) {
				// 如果存在设备遍历设备列表 查询每个设备的检测指标的实时数据
				for (int i = 0; i < nodeInforlist.size(); i++) {
					Map<Integer, NodeSensorBean> nodesensorMap = new LinkedHashMap<Integer, NodeSensorBean>();
					NodeInforBean nodeInForBean = nodeInforlist.get(i);
					List<NodeSensorBean> nodesensorList = realtimeDataDao
							.getNodesensorList(nodeInForBean.getNodeId());
					if (nodesensorList != null && nodesensorList.size() > 0) {
						for (NodeSensorBean nodesensorBean : nodesensorList) {
							if (nodesensorBean.getShowType() == WindTools.SENSORINFO_SHOWTYPE_IS_WIND_DIRECTION) {
								nodesensorBean.setSensorValue(WindTools
										.updateWindDirection(nodesensorBean
												.getSensorValue()));
							}
							nodesensorMap.put(nodesensorBean.getSensorId(),
									nodesensorBean);
						}
						nodeInForBean.setNodeSensorMap(nodesensorMap);
						nodeInForBean.setSampleEvent((realtimeDataDao
								.getNodeInfroStamp(nodeInForBean.getNodeId()))
								.getTime());
					} else {
						nodeInforlist.remove(i);
						i -= 1;
					}
				}
				galaxyResult.setResultCount(nodeInforlist.size());
			}
			galaxyResult.setResultData(nodeInforlist);
		} catch (Exception e) {
			e.printStackTrace();
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("查询实时数据出错！");
		}
		@SuppressWarnings("serial")
		Type type = new TypeToken<GalaxyResult<List<NodeInforBean>>>() {
		}.getType();
		return gson.toJson(galaxyResult, type);
	}

	@Override
	public String getChartData(String nodeId, int sensorId) {
		int a = 0;
		int b = 0;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
				.create();
		GalaxyResult<ChartDataBean> galaxyResult = new GalaxyResult<ChartDataBean>();
		galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		if (nodeId == null || "".equals(nodeId) || sensorId == 0) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_PARAM);
			return gson.toJson(galaxyResult);
		}
		try {
			int count = realtimeDataDao.getChartDataCount(nodeId, sensorId);
			// 通过24小时数据数量过滤数据
			if (count <= 144) {
				// 不过滤
				a = 1;
				b = 0;
			} else if (144 < count && count <= 288) {
				// 隔行取出一半数据
				a = 2;
				b = 0;
			} else if (count > 288 && count <= 480) {
				// 取每四条数据中的一条
				a = 4;
				b = 0;
			} else if (count > 480 && count <= 1440) {
				// 取每十条数据中的一条
				a = 10;
				b = 0;
			} else if (count > 1440 && count <= 2880) {
				// 取每二十条数据中的一条
				a = 20;
				b = 0;
			} else if (count > 2880 && count <= 8640) {
				a = 60;
				b = 0;
			} else if (count > 8640 && count <= 14400) {
				a = 100;
				b = 0;
			} else {
				a = 200;
				b = 0;
			}

			List<Map<String, Object>> realtimeViewDatas = realtimeDataDao
					.getChartData(nodeId, sensorId, a, b);
			if (realtimeViewDatas != null && realtimeViewDatas.size() > 0) {
				ChartDataBean chartDataBean = new ChartDataBean();
				galaxyResult.setResultCount(realtimeViewDatas.size());
				Map<Long, String> resultData = new TreeMap<Long, String>();
				for (Map<String, Object> map : realtimeViewDatas) {
					resultData.put(
							((Timestamp) map.get("createtime")).getTime(),
							(String) map.get("sensorValue"));
				}
				chartDataBean.setBaseData(resultData);
				chartDataBean.setStatData(realtimeDataDao.getChartStatData(
						nodeId, sensorId, a, b));
				galaxyResult.setResultData(chartDataBean);
			}
		} catch (Exception e) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("查询实时图形数据出错！");
			e.printStackTrace();
		}
		@SuppressWarnings("serial")
		Type type = new TypeToken<GalaxyResult<ChartDataBean>>() {
		}.getType();
		return gson.toJson(galaxyResult, type);
	}

	@SuppressWarnings({ "static-access", "serial" })
	@Override
	public String getCurrentSite() {
		Gson gson = new Gson();
		GalaxyResult<SiteBean> galaxyResult = new GalaxyResult<SiteBean>();
		galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		try {
			SiteBean currentSite = realtimeDataDao.getCurrentSite();
			if (currentSite != null) {
				galaxyResult.setResultCount(galaxyResult.ONE_RESULTCOUNT);
				galaxyResult.setResultData(currentSite);
			}
		} catch (Exception e) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("获得当前站点出错");
		}
		Type type = new TypeToken<GalaxyResult<SiteBean>>() {
		}.getType();
		return gson.toJson(galaxyResult, type);
	}

	public String getSensorInfo(String siteId) {
		Gson gson = new Gson();
		GalaxyResult<List<SensorInfoBean>> galaxyResult = new GalaxyResult<List<SensorInfoBean>>();
		galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		if (siteId == null || "".equals(siteId)) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_PARAM);
			return gson.toJson(galaxyResult);
		}
		try {
			List<SensorInfoBean> sensorinforList = realtimeDataDao
					.getSensorinfoList(siteId);
			if (sensorinforList != null && sensorinforList.size() > 0) {
				galaxyResult.setResultCount(sensorinforList.size());
				galaxyResult.setResultData(sensorinforList);
			}
		} catch (Exception e) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("获得监测指标信息失败");
		}
		@SuppressWarnings("serial")
		Type type = new TypeToken<GalaxyResult<List<SensorInfoBean>>>() {
		}.getType();
		return gson.toJson(galaxyResult, type);
	}

	public String getAllGateway() {
		Gson gson = new Gson();
		GalaxyResult<List<DeviceBean>> galaxyResult = new GalaxyResult<List<DeviceBean>>();
		galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		try {
			List<DeviceBean> deviceList = realtimeDataDao.getAllGateway();
			if (deviceList != null) {
				galaxyResult.setResultCount(deviceList.size());
				galaxyResult.setResultData(deviceList);
			}
		} catch (Exception e) {
			galaxyResult.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("获得当前站点下的所有网关出错");
		}
		@SuppressWarnings("serial")
		Type type = new TypeToken<GalaxyResult<List<DeviceBean>>>() {
		}.getType();
		return gson.toJson(galaxyResult, type);
	}

	@Override
	public String polling(boolean polling, String deviceIds, int interval) {
		Gson gson = new Gson();
		GalaxyResult<String> result = new GalaxyResult<String>();
		result.setErrorCode(GalaxyResult.ERRORCODE_NORMAL);
		DcoOperateService dcoService = (DcoOperateService) AppContext
				.getInstance().getAppContext().getBean("DcoOperateService");
		@SuppressWarnings("serial")
		Type type = new TypeToken<List<String>>() {
		}.getType();
		List<String> deviceIdList = gson.fromJson(deviceIds, type);
		try {
			for (String deviceId : deviceIdList) {
				if (polling) {
					dcoService.pollingOpen(deviceId, interval);
				} else {
					dcoService.pollingClose(deviceId);
				}
			}
		} catch (RuntimeException e) {
			result.setErrorCode(GalaxyResult.ERRORCODE_FAIL);
			log.error("\n\n 巡检出错 \n\n");
		}
		return gson.toJson(result);
	}

	public void setRealtimeDataDao(RealtimeDataDao realtimeDataDao) {
		this.realtimeDataDao = realtimeDataDao;
	}

}
