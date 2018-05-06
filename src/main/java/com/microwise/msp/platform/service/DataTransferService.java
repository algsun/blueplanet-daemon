/**
 * 
 */
package com.microwise.msp.platform.service;

import com.microwise.msp.platform.bean.*;

import java.util.List;

/**
 * <pre>
 * 数据同步service 接口
 * 
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public interface DataTransferService {

	/**
	 * 获取当前siteinfo
	 * 
	 * @return
	 */
	public Siteinfo getCurrentSiteinfo();

	/**
	 * 获取数据库对象
	 * 
	 * @param object
	 *            table_schema数据库名称,table_type数据库对象类型(table,view等等)
	 * @return
	 */
	public List<DataBaseObject> getDataBaseObjects(DataBaseObject object);

	// ============================LogTransfer 数据同步日志=========
	/**
	 * 获取所有的同步日志
	 * 
	 * @return 所有的同步日志 List< LogTransfer >
	 */
	public List<LogTransfer> getLogTransfers();

	/**
	 * 获取单表对应的同步日志
	 * 
	 * @param tableName
	 *            单表表名
	 * @return 单表的同步日志 LogTransfer
	 */
	public LogTransfer getLogTransferByTableName(String tableName);

	/**
	 * Insert 单表对应的同步日志
	 * 
	 * @param object
	 *            同步日志对象
	 * @return 日志添加是否成功 true or false
	 */
	public boolean saveLogTransfer(LogTransfer object);

	/**
	 * update 单表对应的同步日志 dataVersion
	 * 
	 * @param object
	 *            同步日志对象
	 * @return 数据版本号（dataVersion）是否更新成功 true or false
	 */
	public boolean updateLogTransfer(LogTransfer object);

	// ============================Nodeinfo节点信息===================
	/**
	 * 获取当前系统中所有节点
	 * 
	 * @return List< NodeInfo >
	 */
	public List<NodeInfo> getNodeinfos();

	/**
	 * dataVersion=0 的节点信息，主要用于client端,获取Client端insert的数据
	 * 
	 * @return 数据同步版本号=0的Nodeinfo
	 */
	public List<NodeInfo> getNodeinfosForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号，主要用于client端,获取Client端update的数据
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<NodeInfo> getNodeinfosForNOFirst(long lastDataVersion);

	/**
	 * Insert nodeInfo，主要用于Server端，同步Client端insert的数据
	 * 
	 * @param nodeInfo
	 *            节点信息对象
	 * @return 添加节点信息是否成功 true or false
	 */
	public boolean saveNodeinfo(NodeInfo nodeInfo);

	/**
	 * update nodeInfo,主要用于Server端，同步Client端update的数据
	 * 
	 * @param nodeInfo
	 *            节点信息对象
	 * @return 更新节点信息是否成功 true or false
	 */
	public boolean updateNodeinfo(NodeInfo nodeInfo);

	/**
	 * update nodeInfo-dataVersion, 主要用于Client端，在获取Server端返回同步结果后，更新其单条记录数据版本号
	 * 
	 * @param nodeInfo
	 *            Server端 返回的dataVersion
	 * @return 更新nodeinfo单条记录 数据版本号是否成功 true or false
	 */
	public boolean updateNodeinfoDataVersion(NodeInfo nodeInfo);

	// ========== Historydatacount 历史数据索引表==========
	/**
	 * dataVersion=0,主要用于client端,获取Client端insert的数据
	 * 
	 * @return
	 */
	public List<Historydatacount> getHistorydatacountsForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号,主要用于client端,获取Client端update的数据
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Historydatacount> getHistorydatacountsForNOFirst(
			long lastDataVersion);

	/**
	 * Insert Historydatacount,主要用于server端,同步client端insert的数据
	 * 
	 * @param Nodeinfomemory
	 *            节点内存数据
	 * @return
	 */
	public boolean saveHistorydatacount(Historydatacount historydatacount);

	/**
	 * update Historydatacount,主要用于Server端，同步client端update的数据
	 * 
	 * @param Historydatacount
	 *            节点内存数据
	 * @return
	 */
	public boolean updateHistorydatacount(Historydatacount historydatacount);

	/**
	 * update Historydatacount-dataVersion，主要用于client端,更新单条记录数据版本号
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateHistorydatacountDataVersion(
			Historydatacount historydatacount);

	// ========== LogicGroup 站点组表==========
	/**
	 * <pre>
	 * dataVersion=0,主要用于client端,获取Client端insert的数据
	 * </pre>
	 * 
	 * @return
	 */
	public List<LogicGroup> getLogicGroupForFirst();

	/**
	 * <pre>
	 * dataVersion!=上次的数据同步版本号,主要用于client端,获取Client端update的数据
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<LogicGroup> getLogicGroupForNoFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert LogicGroup,主要用于server端,同步client端insert的数据
	 * </pre>
	 * 
	 * @param LogicGroup
	 *            节点内存数据
	 * @return
	 */
	public boolean saveLogicGroup(LogicGroup logicGroup);

	/**
	 * <pre>
	 * update LogicGroup,主要用于Server端，同步client端update的数据
	 * </pre>
	 * 
	 * @param LogicGroup
	 *            节点内存数据
	 * @return
	 */
	public boolean updateLogicGroup(LogicGroup logicGroup);

	/**
	 * <pre>
	 * update LogicGroup-dataVersion，主要用于client端,更新单条记录数据版本号
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateLogicGroupDataVersion(LogicGroup logicGroup);

	// ========== Nodeinfomemory 节点内存数据，实时数据==========
	/**
	 * dataVersion=0,主要用于client端,获取Client端insert的数据
	 * 
	 * @return
	 */
	public List<Nodeinfomemory> getNodeinfomemorysForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号,主要用于client端,获取Client端update的数据
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Nodeinfomemory> getNodeinfomemorysForNOFirst(
			long lastDataVersion);

	/**
	 * Insert Nodeinfomemory,主要用于server端,同步client端insert的数据
	 * 
	 * @param nodeinfomemory
	 *            节点内存数据
	 * @return
	 */
	public boolean saveNodeinfomemory(Nodeinfomemory nodeinfomemory);

	/**
	 * update Nodeinfomemory,主要用于Server端，同步client端update的数据
	 * 
	 * @param Nodeinfomemory
	 *            节点内存数据
	 * @return
	 */
	public boolean updateNodeinfomemory(Nodeinfomemory nodeinfomemory);

	/**
	 * update Nodeinfomemory-dataVersion，主要用于client端,更新单条记录数据版本号
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodeinfomemoryDataVersion(Nodeinfomemory nodeinfomemory);

	// ============================Avgdata 均峰值数据============================
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Avgdata> getAvgdatasForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Avgdata> getAvgdatasForNOFirst(long lastDataVersion);

	/**
	 * Insert Avgdata
	 * 
	 * @param Avgdata
	 *            均峰值
	 * @return
	 */
	public boolean saveAvgdata(Avgdata avgdata);

	/**
	 * update Avgdata,主要用于Server端 更新数据
	 * 
	 * @param Avgdata
	 *            均峰值
	 * @return
	 */
	public boolean updateAvgdata(Avgdata avgdata);

	/**
	 * update Avgdata-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateAvgdataDataVersion(Avgdata avgdata);

	// ============= ==Tbl_lxh_acc 日照量数据==========
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Tbl_lxh_acc> getTbl_lxh_accsForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_lxh_acc> getTbl_lxh_accsForNOFirst(long lastDataVersion);

	/**
	 * Insert Tbl_lxh_acc
	 * 
	 * @param Tbl_lxh_acc
	 *            日照
	 * @return
	 */
	public boolean saveTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc);

	/**
	 * update Tbl_lxh_acc,主要用于Server端 更新数据
	 * 
	 * @param Tbl_lxh_acc
	 *            日照
	 * @return
	 */
	public boolean updateTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc);

	/**
	 * update Tbl_lxh_acc-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_lxh_accDataVersion(Tbl_lxh_acc tbl_lxh_acc);

	// ======= Tbl_rb_day_acc 日降雨量=================
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Tbl_rb_day_acc> getTbl_rb_day_accsForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_rb_day_acc> getTbl_rb_day_accsForNOFirst(
			long lastDataVersion);

	/**
	 * Insert Tbl_rb_day_acc
	 * 
	 * @param Tbl_rb_day_acc
	 *            日降雨量
	 * @return
	 */
	public boolean saveTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc);

	/**
	 * update Tbl_rb_day_acc,主要用于Server端 更新数据
	 * 
	 * @param Tbl_rb_day_acc
	 *            日降雨量
	 * @return
	 */
	public boolean updateTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc);

	/**
	 * update Tbl_rb_day_acc-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_rb_day_accDataVersion(Tbl_rb_day_acc tbl_rb_day_acc);

	// ============================Tbl_rb_hour_acc
	// 小时降雨量============================
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForNOFirst(
			long lastDataVersion);

	/**
	 * Insert Tbl_rb_hour_acc
	 * 
	 * @param Tbl_rb_hour_acc
	 *            小时降雨量
	 * @return
	 */
	public boolean saveTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc);

	/**
	 * update Tbl_rb_hour_acc,主要用于Server端 更新数据
	 * 
	 * @param Tbl_rb_hour_acc
	 *            小时降雨量
	 * @return
	 */
	public boolean updateTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc);

	/**
	 * update Tbl_rb_hour_acc-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_rb_hour_accDataVersion(
			Tbl_rb_hour_acc tbl_rb_hour_acc);

	// ============================Windrose 风向玫瑰图============================
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Windrose> getWindrosesForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Windrose> getWindrosesForNOFirst(long lastDataVersion);

	/**
	 * Insert Windrose
	 * 
	 * @param Windrose
	 *            风向玫瑰图
	 * @return
	 */
	public boolean saveWindrose(Windrose windrose);

	/**
	 * update Windrose,主要用于Server端 更新数据
	 * 
	 * @param Windrose
	 *            风向玫瑰图
	 * @return
	 */
	public boolean updateWindrose(Windrose windrose);

	/**
	 * update Windrose-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateWindroseDataVersion(Windrose windrose);

	// ============================NodeSensor节点_端口_传感======
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Nodesensor> getNodesensorsForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Nodesensor> getNodesensorsForNOFirst(long lastDataVersion);

	/**
	 * Insert Nodesensor
	 * 
	 * @param Nodesensor
	 *            节点_端口_传感 信息
	 * @return
	 */
	public boolean saveNodesensor(Nodesensor nodesensor);

	/**
	 * update Nodesensor,主要用于Server端 更新数据
	 * 
	 * @param Nodesensor
	 *            节点_端口_传感 信息
	 * @return
	 */
	public boolean updateNodesensor(Nodesensor nodesensor);

	/**
	 * update Nodesensor-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodesensorDataVersion(Nodesensor nodesensor);

	// ==================mapping_area_site监测站（监测中心）级别对应信息表=====
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<Mapping_area_site> getMapping_area_sitesForFirst();

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Mapping_area_site> getMapping_area_sitesForNOFirst(
			int lastDataVersion);

	/**
	 * Insert Mapping_area_site
	 * 
	 * @param mapping_area_site
	 *            监测站（监测中心）级别 对应信息表
	 * @return
	 */
	public boolean saveMapping_area_site(Mapping_area_site mapping_area_site);

	/**
	 * update Mapping_area_site,主要用于Server端 更新数据
	 * 
	 * @param Mapping_area_site
	 *            监测站（监测中心）级别 对应信息表
	 * @return
	 */
	public boolean updateMapping_area_site(Mapping_area_site mapping_area_site);

	/**
	 * update mapping_area_site-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateMapping_area_siteDataVersion(
			Mapping_area_site mapping_area_site);

	/**
	 * 删除mapping_area_site表中的所有记录
	 * 
	 * @return
	 */
	public boolean deleteMapping_area_siteInfo();

	// =========================Node 节点表 （多个）========

	/**
	 * <pre>
	 * 判断是否存在当前节点数据表
	 * </pre>
	 * 
	 * @param nodeid
	 *            节点号--产品入网唯一标识
	 * @return
	 */
	public boolean existNodeTable(String nodeid);

	/**
	 * <pre>
	 * 创建当前节点数据表
	 * </pre>
	 * 
	 * @param nodeid
	 *            节点号--产品入网唯一标识
	 * @return
	 */
	public boolean createNodeTable(String nodeid);

    /**
     * 创建设备链路信息表
     * @param nodeId
     * @return
     */
    public boolean createDeviceLink(String nodeId);
	/**
	 * dataVersion=0
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForFirst(String nodeid);

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param node
	 *            nodeid节点号（节点数据表名）+lastDataVersion 数据表版本号
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForNOFirst(NodeCls node);

	/**
	 * dataVersion!=上次的数据同步版本号
	 * 
	 * @param nodeid节点号
	 *            （节点数据表名）
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForNOFirst(String nodeid);

	/**
	 * Insert Node
	 * 
	 * @param node
	 *            节点数据
	 * @return
	 */
	public boolean saveNode(NodeCls node);

    /**
     * 保存设备链路信息
     * @param deviceLink 设备链路信息
     * @return
     */
    public boolean savaDeviceLink(DeviceLink deviceLink);

    /**
	 * update Node,主要用于Server端 更新数据
	 * 
	 * @param node
	 *            节点数据
	 * @return
	 */
	public boolean updateNode(NodeCls node);

	/**
	 * update Node-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodeDataVersion(NodeCls node);

	// =========================TmonitoringStation_info 监测点表--前台========
	/**
	 * dataVersion=0
	 * 
	 * @return 获取dataVersion=0的所有监测点
	 */
	public List<TmonitoringStation_info> getTmonitoringStation_infosForFirst();

	/**
	 * dataVersion!=上次的数据版本号
	 * 
	 * @param lastDataVersion
	 *            lastDataVersion 数据版本号
	 * 
	 * @return 获取dataVersion!=上次的数据版本号的所有监测点
	 */
	public List<TmonitoringStation_info> getTmonitoringStation_infosForNOFirst(
			long lastDataVersion);

	/**
	 * Insert TmonitoringStation_info
	 * 
	 * @param monitoringStation_info
	 *            监测点
	 * 
	 * @return insert是否成功
	 */
	public boolean saveTmonitoringStation_info(
			TmonitoringStation_info monitoringStation_info);

    /**
     * 修改设备链路信息
     * @param deviceLink  设备链路信息
     * @return
     */
    public boolean updateDeviceLinkDataVersion(DeviceLink deviceLink);


    /**
	 * update TmonitoringStation_info,主要用于Server端 更新数据
	 * 
	 * @param monitoringStation_info
	 *            监测点
	 * 
	 * @return update是否成功
	 */
	public boolean updateTmonitoringStation_info(
			TmonitoringStation_info monitoringStation_info);

	/**
	 * update TmonitoringStation_info-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * 
	 * @return update该表的数据版本号是否成功
	 */
	public boolean updateTmonitoringStation_infoDataVersion(
			TmonitoringStation_info monitoringStation_info);

	// ======== Tnode_monitoringStation_info 设备_监测点对应表--前台========
	/**
	 * dataVersion=0
	 * 
	 * @return 获取dataVersion=0的所有监测点
	 */
	public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForFirst();

	/**
	 * dataVersion!=上次的数据版本号
	 * 
	 * @param lastDataVersion
	 *            lastDataVersion 数据版本号
	 * 
	 * @return 获取dataVersion!=上次的数据版本号的所有设备_监测点对应信息
	 */
	public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForNOFirst(
			long lastDataVersion);

	/**
	 * Insert Tnode_monitoringStation_info
	 * 
	 * @param node_monitoringStation_info
	 *            设备_监测点对应信息
	 * 
	 * @return insert是否成功
	 */
	public boolean saveTnode_monitoringStation_info(
			Tnode_monitoringStation_info node_monitoringStation_info);

	/**
	 * update Tnode_monitoringStation_info,主要用于Server端 更新数据
	 * 
	 * @param node_monitoringStation_info
	 *            设备_监测点对应信息
	 * 
	 * @return update是否成功
	 */
	public boolean updateTnode_monitoringStation_info(
			Tnode_monitoringStation_info node_monitoringStation_info);

	/**
	 * update Tnode_monitoringStation_info-dataVersion
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * 
	 * @return update该表的数据版本号是否成功
	 */
	public boolean updateTnode_monitoringStation_infoDataVersion(
			Tnode_monitoringStation_info node_monitoringStation_info);

	// ------------------------------------判断各表记录是否存在-----------------------------------------------
	/**
	 * <pre>
	 * 2012-4-5 何明明 nodeinfo是否存在
	 * </pre>
	 * 
	 * @param nodeInfo
	 *            一个节点对象
	 * @return
	 * 
	 */
	public boolean isExistNodeinfo(NodeInfo nodeInfo);

	/**
	 * <pre>
	 * 2012-4-5 何明明 nodesensor是否存在
	 * </pre>
	 * 
	 * @param nodesensor
	 *            一个节点传感信息，对象
	 * @return
	 * 
	 */
	public boolean isExistNodesensor(Nodesensor nodesensor);

	/**
	 * <pre>
	 * 2012-4-5 何明明 nodeinfomemory是否存在
	 * </pre>
	 * 
	 * @param nodeinfomemory
	 *            一个节点内存，实时数据对象
	 * @return
	 * 
	 */
	public boolean isExistNodeinfomemory(Nodeinfomemory nodeinfomemory);

	/**
	 * <pre>
	 * logicGroup是否存在
	 * </pre>
	 * 
	 * @param logicGroup
	 *            一个均峰值对象
	 * @return
	 * 
	 */
	public boolean isExistLogicGroup(LogicGroup logicGroup);

	/**
	 * <pre>
	 * 2012-4-5 谢登 historydatacount是否存在
	 * </pre>
	 * 
	 * @param historydatacount
	 *            历史数据索引，实时数据对象
	 * @return
	 * 
	 */
	public boolean isExistHistorydatacount(Historydatacount historydatacount);

	/**
	 * <pre>
	 * 2012-4-5 何明明 nodedata是否存在
	 * </pre>
	 * 
	 * @param nodedata
	 *            一个nodedata对象
	 * @return
	 * 
	 */
	public boolean isExistNodedata(NodeCls nodedata);

    /**
     * 设备链路信息是否存在
     * @param deviceLink
     * @return
     */
    public boolean isExistDeviceLink(DeviceLink deviceLink);

	/**
	 * <pre>
	 * 2012-4-5 何明明 avgdata是否存在
	 * </pre>
	 * 
	 * @param avgdata
	 *            一个均峰值对象
	 * @return
	 * 
	 */
	public boolean isExistAvgdata(Avgdata avgdata);

	/**
	 * <pre>
	 * 2012-4-5 何明明 Tbl_lxh_acc是否存在
	 * </pre>
	 * 
	 * @param tab_lxh_acc
	 *            一个日照量对象
	 * @return
	 * 
	 */
	public boolean isExistTbl_lxh_acc(Tbl_lxh_acc tab_lxh_acc);

	/**
	 * <pre>
	 * 2012-4-5 何明明 Tbl_rb_day_acc是否存在
	 * </pre>
	 * 
	 * @param tab_lxh_acc
	 *            一个日降雨量对象
	 * @return
	 * 
	 */
	public boolean isExistTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc);

	/**
	 * <pre>
	 * 2012-4-5 何明明 tbl_rb_hour_acc是否存在
	 * </pre>
	 * 
	 * @param tbl_rb_hour_acc
	 *            一个小时降雨量对象
	 * @return
	 * 
	 */
	public boolean isExistTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc);

	/**
	 * <pre>
	 * 2012-4-5 何明明 windrose是否存在
	 * </pre>
	 * 
	 * @param windrose
	 *            一个风向玫瑰对象
	 * @return
	 * 
	 */
	public boolean isExistWindrose(Windrose windrose);

	/**
	 * <pre>
	 * dataVersion != 上次的数据版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            lastDataVersion 数据版本号
	 * 
	 * @return 获取dataVersion!=上次的数据版本号的设备的实时数据信息
	 */
	public List<NodeInfoAndNodeInfoMemory> getNodeInfoAndMemoryInfoList(
			long lastDataVersion);


    /**
     * 获取设备链路信息表
     * @param nodeId          设备编号
     * @param dataVersion    数据版本号
     * @return                链路信息
     */
    public List<DeviceLink> getDeviceLinks(String nodeId, long dataVersion);
}
