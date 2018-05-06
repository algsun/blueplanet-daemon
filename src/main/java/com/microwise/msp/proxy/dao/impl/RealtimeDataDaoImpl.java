package com.microwise.msp.proxy.dao.impl;

import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.proxy.bean.*;
import com.microwise.msp.proxy.dao.RealtimeDataDao;
import com.microwise.msp.util.DateUtils;

import java.util.*;

/**
 * 实时数据 查询dao层 实现
 * 
 * @author xubaoji
 * @date 2013-1-6
 * 
 * @check 2013-1-10 zhangpeng svn：1107 实时图形ibatis查询map
 */
public class RealtimeDataDaoImpl extends SqlMapClient2SqlSessionAdapter implements
		RealtimeDataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeInforBean> getNodeInforList(String siteId) throws Exception {
		List<NodeInforBean> nodeInforList = new ArrayList<NodeInforBean>();
		nodeInforList = getSqlSession().selectList("proxy.getNodeInforListBySiteId", siteId);
		return nodeInforList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeSensorBean> getNodesensorList(String nodeId)
			throws Exception {
		List<NodeSensorBean> nodesensorList = new ArrayList<NodeSensorBean>();
		nodesensorList = getSqlSession().selectList("proxy.getNodesensorListByNodeId", nodeId);
		return nodesensorList;
	}

	@Override
	public Date getNodeInfroStamp(String nodeId) throws Exception {
		return (Date) getSqlSession().selectOne("proxy.getRealtimeDataStampByNodeId", nodeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getChartData(String nodeId,
			int sensorPhysicalid, int a, int b) throws Exception {
		List<Map<String, Object>> RealtimeViewDatas = new ArrayList<Map<String, Object>>();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("nodeId", nodeId);
		parameterMap.put("sensorPhysicalid", sensorPhysicalid);
		parameterMap.put("queryTime", DateUtils.changeDate(new Date(), -1));
		parameterMap.put("a", a);
		parameterMap.put("b", b);
		RealtimeViewDatas = getSqlSession().selectList("proxy.getChartData", parameterMap);
		return RealtimeViewDatas;
	}

	@Override
	public int getChartDataCount(String nodeId, int sensorPhysicalid)
			throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("nodeId", nodeId);
		parameterMap.put("sensorPhysicalid", sensorPhysicalid);
		parameterMap.put("queryTime", DateUtils.changeDate(new Date(), -1));
		return (Integer) getSqlSession().selectOne("proxy.getChartDataCount", parameterMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getChartStatData(String nodeId,
			int sensorPhysicalid, int a, int b) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("nodeId", nodeId);
		parameterMap.put("sensorPhysicalid", sensorPhysicalid);
		parameterMap.put("queryTime", DateUtils.changeDate(new Date(), -1));
		parameterMap.put("a", a);
		parameterMap.put("b", b);
		return (Map<String, String>)  getSqlSession().selectOne("proxy.getChartStatData", parameterMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SensorInfoBean> getSensorinfoList(String siteId)
			throws Exception {
		List<SensorInfoBean> sensorinfoList = new ArrayList<SensorInfoBean>();
		sensorinfoList = getSqlSession().selectList("proxy.getSensorInforList", siteId);
		return sensorinfoList;
	}

	@Override
	public SiteBean getCurrentSite() throws Exception {
		return (SiteBean) getSqlSession().selectOne("proxy.getCurrentSite");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceBean> getAllGateway() {
		return getSqlSession().selectList("proxy.getAllGateway");
	}

}
