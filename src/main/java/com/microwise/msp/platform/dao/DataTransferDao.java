/**
 * 
 */
package com.microwise.msp.platform.dao;

import com.microwise.msp.platform.bean.*;

import java.util.List;

/**
 * <pre>
 * 数据同步--持久化接口 
 * 
 * LogTransfer(同步日志)			
 * 需要同步的： 							
 * ①NodeInfo           节点信息                                 1.2
 * ②NodeSensor         节点_端口_传感对应表                      1.4
 * ③Nodeinfomemory     节点内存(实时数据)信息                    1.5
 * ④++++Node           节点历史数据表 （多个）                   1.1
 * ⑤Avgdata            均峰值                                   1.3
 * ⑥Tbl_rb_day_acc     日降雨量                                 1.7
 * ⑦Tbl_rb_hour_acc	    小时降雨量                               1.8 
 * ⑧Tbl_lxh_acc	        日照量                                  1.9
 * ⑨Windrose           风向玫瑰图                               1.6
 * 
 * mapping_area_site    监测站（监测中心）级别   对应信息表        1.10  应该是由上级同步到下级
 * 前台的2个：
 * monitoringStation_info		监测点
 * node_monitoringStation_info  设备_监测点对应信息
 * 
 * 动态的View:
 * 数据天视图：       v_data_nodeid_当前日期
 * 数据最近一天视图： v_recentdata_nodeid
 * 
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public interface DataTransferDao {

	/**
	 * <pre>
	 * 获取当前siteinfo
	 * </pre>
	 * 
	 * @return
	 */
	public Siteinfo getCurrentSiteinfo();

	/**
	 * <pre>
	 * 获取数据库对象
	 * </pre>
	 * 
	 * @param object
	 *            table_schema数据库名称,table_type数据库对象类型(table,view等等)
	 * @return
	 */
	public List<DataBaseObject> getDataBaseObjects(DataBaseObject object);

	// ============================LogTransfer，数据同步日志=========
	/**
	 * <pre>
	 * 获取所有的同步日志
	 * </pre>
	 * 
	 * @return 所有的同步日志 List< LogTransfer >
	 */
	public List<LogTransfer> getLogTransfers();

	/**
	 * <pre>
	 * 获取单表对应的同步日志
	 * </pre>
	 * 
	 * @param tableName
	 *            单表表名
	 * @return 单表的同步日志 LogTransfer
	 */
	public LogTransfer getLogTransferByTableName(String tableName);

	/**
	 * <pre>
	 * Insert 单表对应的同步日志
	 * </pre>
	 * 
	 * @param object
	 *            同步日志对象
	 * @return 日志添加是否成功 true or false
	 */
	public boolean saveLogTransfer(LogTransfer object);

	/**
	 * <pre>
	 * update 单表对应的同步日志 dataVersion
	 * </pre>
	 * 
	 * @param object
	 *            同步日志对象
	 * @return 数据版本号（dataVersion）是否更新成功 true or false
	 */
	public boolean updateLogTransfer(LogTransfer object);

	// ============================NodeInfo，节点信息===================
	/**
	 * <pre>
	 * 获取当前系统中所有节点
	 * </pre>
	 * 
	 * @return List< NodeInfo >
	 */
	public List<NodeInfo> getNodeinfos();

    /**
     * 获取设备链路信息
     * @return
     */
    public List<DeviceLink> getDeviceLinks(String nodeId,long dataVersion);

	/**
	 * <pre>
	 * dataVersion=0 的节点信息，主要用于client端,获取Client端insert的数据
	 * </pre>
	 * 
	 * @return 数据同步版本号=0的Nodeinfo
	 */
	public List<NodeInfo> getNodeinfosForFirst();

	/**
	 * <pre>
	 * dataVersion!=上次的数据同步版本号，主要用于client端,获取Client端update的数据
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<NodeInfo> getNodeinfosForNOFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert nodeInfo，主要用于Server端，同步Client端insert的数据
	 * </pre>
	 * 
	 * @param nodeInfo
	 *            节点信息对象
	 * @return 添加节点信息是否成功 true or false
	 */
	public boolean saveNodeinfo(NodeInfo nodeInfo);

	/**
	 * <pre>
	 * update nodeInfo,主要用于Server端，同步Client端update的数据
	 * </pre>
	 * 
	 * @param nodeInfo
	 *            节点信息对象
	 * @return 更新节点信息是否成功 true or false
	 */
	public boolean updateNodeinfo(NodeInfo nodeInfo);

	/**
	 * <pre>
	 * update nodeInfo-dataVersion, 主要用于Client端，在获取Server端返回同步结果后，更新其单条记录数据版本号
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return 更新nodeinfo单条记录 数据版本号是否成功 true or false
	 */
	public boolean updateNodeinfoDataVersion(NodeInfo nodeInfo);

	// ============================Nodeinfomemory，节点内存数据，实时数据============================

	/**
	 * <pre>
	 * dataVersion=0,主要用于client端,获取Client端insert的数据
	 * </pre>
	 * 
	 * @return
	 */
	public List<Nodeinfomemory> getNodeinfomemorysForFirst();

	/**
	 * <pre>
	 * dataVersion!=上次的数据同步版本号,主要用于client端,获取Client端update的数据
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Nodeinfomemory> getNodeinfomemorysForNOFirst(
			long lastDataVersion);

	/**
	 * <pre>
	 * Insert Nodeinfomemory,主要用于server端,同步client端insert的数据
	 * </pre>
	 * 
	 * @param Nodeinfomemory
	 *            节点内存数据
	 * @return
	 */
	public boolean saveNodeinfomemory(Nodeinfomemory nodeinfomemory);

	/**
	 * <pre>
	 * update Nodeinfomemory,主要用于Server端，同步client端update的数据
	 * </pre>
	 * 
	 * @param Nodeinfomemory
	 *            节点内存数据
	 * @return
	 */
	public boolean updateNodeinfomemory(Nodeinfomemory nodeinfomemory);

	/**
	 * <pre>
	 * update Nodeinfomemory-dataVersion，主要用于client端,更新单条记录数据版本号
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodeinfomemoryDataVersion(Nodeinfomemory nodeinfomemory);

	// ============================logicGroup，站点组表============================

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

	// ============================Historydatacount，历史数据索引============================

	/**
	 * <pre>
	 * dataVersion=0,主要用于client端,获取Client端insert的数据
	 * </pre>
	 * 
	 * @return
	 */
	public List<Historydatacount> getHistorydatacountForFirst();

	/**
	 * <pre>
	 * dataVersion!=上次的数据同步版本号,主要用于client端,获取Client端update的数据
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Historydatacount> getHistorydatacountsForNOFirst(
			long lastDataVersion);

	/**
	 * <pre>
	 * Insert Historydatacount,主要用于server端,同步client端insert的数据
	 * </pre>
	 * 
	 * @param Historydatacount
	 *            节点内存数据
	 * @return
	 */
	public boolean saveHistorydatacount(Historydatacount historydatacount);

	/**
	 * <pre>
	 * update Historydatacount,主要用于Server端，同步client端update的数据
	 * </pre>
	 * 
	 * @param Historydatacount
	 *            节点内存数据
	 * @return
	 */
	public boolean updateHistorydatacount(Historydatacount historydatacount);

	/**
	 * <pre>
	 * update Historydatacount-dataVersion，主要用于client端,更新单条记录数据版本号
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateHistorydatacountDataVersion(
			Historydatacount historydatacount);

	// ============================Avgdata，均峰值数据============================

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Avgdata> getAvgdatasForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Avgdata> getAvgdatasForNOFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert Avgdata
	 * </pre>
	 * 
	 * @param Avgdata
	 *            均峰值
	 * @return
	 */
	public boolean saveAvgdata(Avgdata avgdata);

	/**
	 * <pre>
	 * update Avgdata,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Avgdata
	 *            均峰值
	 * @return
	 */
	public boolean updateAvgdata(Avgdata avgdata);

	/**
	 * <pre>
	 * update Avgdata-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateAvgdataDataVersion(Avgdata avgdata);

	// ============================Tbl_lxh_acc，日照量数据============================

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Tbl_lxh_acc> getTbl_lxh_accsForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_lxh_acc> getTbl_lxh_accsForNOFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert Tbl_lxh_acc
	 * </pre>
	 * 
	 * @param Tbl_lxh_acc
	 *            日照
	 * @return
	 */
	public boolean saveTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc);

	/**
	 * <pre>
	 * update Tbl_lxh_acc,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Tbl_lxh_acc
	 *            日照
	 * @return
	 */
	public boolean updateTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc);

	/**
	 * <pre>
	 * update Tbl_lxh_acc-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_lxh_accDataVersion(Tbl_lxh_acc tbl_lxh_acc);

	// ============================Tbl_rb_day_acc，日降雨量============================

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Tbl_rb_day_acc> getTbl_rb_day_accsForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_rb_day_acc> getTbl_rb_day_accsForNOFirst(
			long lastDataVersion);

	/**
	 * <pre>
	 * Insert Tbl_rb_day_acc
	 * </pre>
	 * 
	 * @param Tbl_rb_day_acc
	 *            日降雨量
	 * @return
	 */
	public boolean saveTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc);

	/**
	 * <pre>
	 * update Tbl_rb_day_acc,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Tbl_rb_day_acc
	 *            日降雨量
	 * @return
	 */
	public boolean updateTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc);

	/**
	 * <pre>
	 * update Tbl_rb_day_acc-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_rb_day_accDataVersion(Tbl_rb_day_acc tbl_rb_day_acc);

	// ============================Tbl_rb_hour_acc,小时降雨量============================

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForNOFirst(
			long lastDataVersion);

	/**
	 * <pre>
	 * Insert Tbl_rb_hour_acc
	 * </pre>
	 * 
	 * @param Tbl_rb_hour_acc
	 *            小时降雨量
	 * @return
	 */
	public boolean saveTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc);

	/**
	 * <pre>
	 * update Tbl_rb_hour_acc,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Tbl_rb_hour_acc
	 *            小时降雨量
	 * @return
	 */
	public boolean updateTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc);

	/**
	 * <pre>
	 * update Tbl_rb_hour_acc-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateTbl_rb_hour_accDataVersion(
			Tbl_rb_hour_acc tbl_rb_hour_acc);

	// ============================Windrose,风向玫瑰图============================

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Windrose> getWindrosesForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Windrose> getWindrosesForNOFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert Windrose
	 * </pre>
	 * 
	 * @param Windrose
	 *            风向玫瑰图
	 * @return
	 */
	public boolean saveWindrose(Windrose windrose);

	/**
	 * <pre>
	 * update Windrose,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Windrose
	 *            风向玫瑰图
	 * @return
	 */
	public boolean updateWindrose(Windrose windrose);

	/**
	 * <pre>
	 * update Windrose-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateWindroseDataVersion(Windrose windrose);

	// ============================NodeSensor，节点_端口_传感======

	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Nodesensor> getNodesensorsForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Nodesensor> getNodesensorsForNOFirst(long lastDataVersion);

	/**
	 * <pre>
	 * Insert Nodesensor
	 * </pre>
	 * 
	 * @param Nodesensor
	 *            节点_端口_传感 信息
	 * @return
	 */
	public boolean saveNodesensor(Nodesensor nodesensor);

	/**
	 * <pre>
	 * update Nodesensor,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Nodesensor
	 *            节点_端口_传感 信息
	 * @return
	 */
	public boolean updateNodesensor(Nodesensor nodesensor);

	/**
	 * <pre>
	 * update Nodesensor-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodesensorDataVersion(Nodesensor nodesensor);

	// ==================mapping_area_site，监测站（监测中心）级别对应信息表=====
	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return
	 */
	public List<Mapping_area_site> getMapping_area_sitesForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据同步版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            数据同步版本号
	 * @return
	 */
	public List<Mapping_area_site> getMapping_area_sitesForNOFirst(
			int lastDataVersion);

	/**
	 * <pre>
	 * Insert Mapping_area_site
	 * </pre>
	 * 
	 * @param mapping_area_site
	 *            监测站（监测中心）级别 对应信息表
	 * @return
	 */
	public boolean saveMapping_area_site(Mapping_area_site mapping_area_site);

	/**
	 * <pre>
	 * update Mapping_area_site,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param Mapping_area_site
	 *            监测站（监测中心）级别 对应信息表
	 * @return
	 */
	public boolean updateMapping_area_site(Mapping_area_site mapping_area_site);

	/**
	 * <pre>
	 * update mapping_area_site-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateMapping_area_siteDataVersion(
			Mapping_area_site mapping_area_site);

	/**
	 * <pre>
	 * 删除mapping_area_site表中的所有记录
	 * </pre>
	 * 
	 * @return
	 */
	public boolean deleteMapping_area_siteInfo();

	// =========================Node,节点表（多个）========
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
	 * dataVersion = 0
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForFirst(String nodeid);

	/**
	 * dataVersion != 上次的数据同步版本号
	 * 
	 * @param node
	 *            nodeid节点号（节点数据表名）+lastDataVersion 数据表版本号
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForNOFirst(NodeCls nodeid);

	/**
	 * dataVersion != 上次的数据同步版本号
	 * 
	 * @param nodeid节点号
	 *            （节点数据表名）
	 * 
	 * @return
	 */
	public List<NodeCls> getNodesForNOFirst(String nodeid);

	/**
	 * <pre>
	 * Insert Node
	 * </pre>
	 * 
	 * @param node
	 *            节点数据
	 * @return
	 */
	public boolean saveNode(NodeCls node);

    /**
     * 保存设备链路信息
     * @param deviceLink
     * @return
     */
    public boolean saveDeviceLink(DeviceLink deviceLink);

	/**
	 * <pre>
	 * update Node,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param node
	 *            节点数据
	 * @return
	 */
	public boolean updateNode(NodeCls node);

	/**
	 * <pre>
	 * update Node-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * @return
	 */
	public boolean updateNodeDataVersion(NodeCls node);

    /**
     * 修改设备链路信息表
     * @param deviceLink  设备链路信息
     * @return
     */
    public boolean updateDeviceLinkDataVersion(DeviceLink deviceLink);

    /**
     * 是否存在设备链路信息
     * @param deviceLink  设备链路信息
     * @return
     */
    public boolean isExistDeviceLink(DeviceLink deviceLink);

	// =========================TmonitoringStation_info,监测点表--前台========
	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return 获取dataVersion=0的所有监测点
	 */
	public List<TmonitoringStation_info> getTmonitoringStation_infosForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            lastDataVersion 数据版本号
	 * 
	 * @return 获取dataVersion!=上次的数据版本号的所有监测点
	 */
	public List<TmonitoringStation_info> getTmonitoringStation_infosForNOFirst(
			long lastDataVersion);

	/**
	 * <pre>
	 * Insert TmonitoringStation_info
	 * </pre>
	 * 
	 * @param monitoringStation_info
	 *            监测点
	 * 
	 * @return insert是否成功
	 */
	public boolean saveTmonitoringStation_info(
			TmonitoringStation_info monitoringStation_info);

	/**
	 * <pre>
	 * update TmonitoringStation_info,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param monitoringStation_info
	 *            监测点
	 * 
	 * @return update是否成功
	 */
	public boolean updateTmonitoringStation_info(
			TmonitoringStation_info monitoringStation_info);

	/**
	 * <pre>
	 * update TmonitoringStation_info-dataVersion
	 * </pre>
	 * 
	 * @param dataVersion
	 *            Server端 返回的dataVersion
	 * 
	 * @return update该表的数据版本号是否成功
	 */
	public boolean updateTmonitoringStation_infoDataVersion(
			TmonitoringStation_info monitoringStation_info);

	// ======== Tnode_monitoringStation_info,设备_监测点对应表--前台========
	/**
	 * <pre>
	 * dataVersion = 0
	 * </pre>
	 * 
	 * @return 获取dataVersion=0的所有监测点
	 */
	public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForFirst();

	/**
	 * <pre>
	 * dataVersion != 上次的数据版本号
	 * </pre>
	 * 
	 * @param lastDataVersion
	 *            lastDataVersion 数据版本号
	 * 
	 * @return 获取dataVersion!=上次的数据版本号的所有设备_监测点对应信息
	 */
	public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForNOFirst(
			long lastDataVersion);

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
	 * <pre>
	 * Insert Tnode_monitoringStation_info
	 * </pre>
	 * 
	 * @param node_monitoringStation_info
	 *            设备_监测点对应信息
	 * 
	 * @return insert是否成功
	 */
	public boolean saveTnode_monitoringStation_info(
			Tnode_monitoringStation_info node_monitoringStation_info);

	/**
	 * <pre>
	 * update Tnode_monitoringStation_info,主要用于Server端 更新数据
	 * </pre>
	 * 
	 * @param node_monitoringStation_info
	 *            设备_监测点对应信息
	 * 
	 * @return update是否成功
	 */
	public boolean updateTnode_monitoringStation_info(
			Tnode_monitoringStation_info node_monitoringStation_info);

	/**
	 * <pre>
	 * update Tnode_monitoringStation_info-dataVersion
	 * </pre>
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
	 * 2012-09-28 谢登 historydatacount是否存在
	 * </pre>
	 * 
	 * @param historydatacount
	 *            一个节点内存，实时数据对象
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
	 * logicGroup是否存在
	 * </pre>
	 * 
	 * @param logicGroup
	 *            一个站点组对象
	 * @return
	 * 
	 */
	public boolean isExistLogicGroup(LogicGroup logicGroup);

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

}
