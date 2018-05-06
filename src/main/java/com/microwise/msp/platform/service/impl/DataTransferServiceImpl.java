/**
 *
 */
package com.microwise.msp.platform.service.impl;

import com.microwise.msp.platform.bean.*;
import com.microwise.msp.platform.dao.DataTransferDao;
import com.microwise.msp.platform.service.DataTransferService;

import java.util.List;

/**
 * <pre>
 * 数据同步service实现
 * </pre>
 *
 * @author heming
 * @since 2011-11-01
 */
public class DataTransferServiceImpl implements DataTransferService {

    private DataTransferDao dao;

    public DataTransferDao getDao() {
        return dao;
    }

    public void setDao(DataTransferDao dao) {
        this.dao = dao;
    }

    @Override
    public Siteinfo getCurrentSiteinfo() {
        return dao.getCurrentSiteinfo();
    }

    @Override
    public List<DataBaseObject> getDataBaseObjects(DataBaseObject object) {
        return dao.getDataBaseObjects(object);
    }

    @Override
    public List<LogTransfer> getLogTransfers() {
        return dao.getLogTransfers();
    }

    @Override
    public LogTransfer getLogTransferByTableName(String tableName) {
        return dao.getLogTransferByTableName(tableName);
    }

    @Override
    public boolean saveLogTransfer(LogTransfer object) {
        boolean isTrue = false;
        if (dao.getLogTransferByTableName(object.tableName) == null) { // 不存在时，可新增
            isTrue = dao.saveLogTransfer(object);
        }
        return isTrue;
    }

    @Override
    public boolean updateLogTransfer(LogTransfer object) {
        boolean isTrue = false;
        if (dao.getLogTransferByTableName(object.tableName) != null) { // 存在时，可更新
            isTrue = dao.updateLogTransfer(object);
        }
        return isTrue;
    }

    @Override
    public List<NodeInfo> getNodeinfos() {
        return dao.getNodeinfos();
    }

    @Override
    public List<NodeInfo> getNodeinfosForFirst() {
        return dao.getNodeinfosForFirst();
    }

    @Override
    public List<NodeInfo> getNodeinfosForNOFirst(long lastDataVersion) {
        return dao.getNodeinfosForNOFirst(lastDataVersion);
    }

    @Override
    public List<NodeInfoAndNodeInfoMemory> getNodeInfoAndMemoryInfoList(
            long lastDataVersion) {
        return dao.getNodeInfoAndMemoryInfoList(lastDataVersion);
    }

    @Override
    public List<DeviceLink> getDeviceLinks(String nodeId, long dataVersion) {
        return dao.getDeviceLinks(nodeId, dataVersion);
    }

    @Override
    public boolean saveNodeinfo(NodeInfo nodeInfo) {
        return dao.saveNodeinfo(nodeInfo);
    }

    @Override
    public boolean updateNodeinfo(NodeInfo nodeInfo) {
        return dao.updateNodeinfo(nodeInfo);
    }

    @Override
    public boolean updateNodeinfoDataVersion(NodeInfo nodeInfo) {
        return dao.updateNodeinfoDataVersion(nodeInfo);
    }

    @Override
    public List<Nodeinfomemory> getNodeinfomemorysForFirst() {
        return dao.getNodeinfomemorysForFirst();
    }

    @Override
    public List<Nodeinfomemory> getNodeinfomemorysForNOFirst(
            long lastDataVersion) {
        return dao.getNodeinfomemorysForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        return dao.saveNodeinfomemory(nodeinfomemory);
    }

    @Override
    public boolean updateNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        return dao.updateNodeinfomemory(nodeinfomemory);
    }

    @Override
    public boolean updateNodeinfomemoryDataVersion(Nodeinfomemory nodeinfomemory) {
        return dao.updateNodeinfomemoryDataVersion(nodeinfomemory);
    }

    @Override
    public List<Historydatacount> getHistorydatacountsForFirst() {
        return dao.getHistorydatacountForFirst();
    }

    @Override
    public List<Historydatacount> getHistorydatacountsForNOFirst(
            long lastDataVersion) {
        return dao.getHistorydatacountsForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveHistorydatacount(Historydatacount historydatacount) {
        return dao.saveHistorydatacount(historydatacount);
    }

    @Override
    public boolean updateHistorydatacount(Historydatacount historydatacount) {
        return dao.updateHistorydatacount(historydatacount);
    }

    @Override
    public boolean updateHistorydatacountDataVersion(
            Historydatacount historydatacount) {
        return dao.updateHistorydatacountDataVersion(historydatacount);
    }

    @Override
    public List<Avgdata> getAvgdatasForFirst() {
        return dao.getAvgdatasForFirst();
    }

    @Override
    public List<Avgdata> getAvgdatasForNOFirst(long lastDataVersion) {
        return dao.getAvgdatasForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveAvgdata(Avgdata avgdata) {
        return dao.saveAvgdata(avgdata);
    }

    @Override
    public boolean updateAvgdata(Avgdata avgdata) {
        return dao.updateAvgdata(avgdata);
    }

    @Override
    public boolean updateAvgdataDataVersion(Avgdata avgdata) {
        return dao.updateAvgdataDataVersion(avgdata);
    }

    @Override
    public List<Tbl_lxh_acc> getTbl_lxh_accsForFirst() {
        return dao.getTbl_lxh_accsForFirst();
    }

    @Override
    public List<Tbl_lxh_acc> getTbl_lxh_accsForNOFirst(long lastDataVersion) {
        return dao.getTbl_lxh_accsForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc) {
        return dao.saveTbl_lxh_acc(tbl_lxh_acc);
    }

    @Override
    public boolean updateTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc) {
        return dao.updateTbl_lxh_acc(tbl_lxh_acc);
    }

    @Override
    public boolean updateTbl_lxh_accDataVersion(Tbl_lxh_acc tbl_lxh_acc) {
        return dao.updateTbl_lxh_accDataVersion(tbl_lxh_acc);
    }

    @Override
    public List<Tbl_rb_day_acc> getTbl_rb_day_accsForFirst() {
        return dao.getTbl_rb_day_accsForFirst();
    }

    @Override
    public List<Tbl_rb_day_acc> getTbl_rb_day_accsForNOFirst(
            long lastDataVersion) {
        return dao.getTbl_rb_day_accsForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        return dao.saveTbl_rb_day_acc(tbl_rb_day_acc);
    }

    @Override
    public boolean updateTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        return dao.updateTbl_rb_day_acc(tbl_rb_day_acc);
    }

    @Override
    public boolean updateTbl_rb_day_accDataVersion(Tbl_rb_day_acc tbl_rb_day_acc) {
        return dao.updateTbl_rb_day_accDataVersion(tbl_rb_day_acc);
    }

    @Override
    public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForFirst() {
        return dao.getTbl_rb_hour_accsForFirst();
    }

    @Override
    public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForNOFirst(
            long lastDataVersion) {
        return dao.getTbl_rb_hour_accsForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        return dao.saveTbl_rb_hour_acc(tbl_rb_hour_acc);
    }

    @Override
    public boolean updateTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        return dao.updateTbl_rb_hour_acc(tbl_rb_hour_acc);
    }

    @Override
    public boolean updateTbl_rb_hour_accDataVersion(
            Tbl_rb_hour_acc tbl_rb_hour_acc) {
        return dao.updateTbl_rb_hour_accDataVersion(tbl_rb_hour_acc);
    }

    @Override
    public List<Windrose> getWindrosesForFirst() {
        return dao.getWindrosesForFirst();
    }

    @Override
    public List<Windrose> getWindrosesForNOFirst(long lastDataVersion) {
        return dao.getWindrosesForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveWindrose(Windrose windrose) {
        return dao.saveWindrose(windrose);
    }

    @Override
    public boolean updateWindrose(Windrose windrose) {
        return dao.updateWindrose(windrose);
    }

    @Override
    public boolean updateWindroseDataVersion(Windrose windrose) {
        return dao.updateWindroseDataVersion(windrose);
    }

    @Override
    public List<Nodesensor> getNodesensorsForFirst() {
        return dao.getNodesensorsForFirst();
    }

    @Override
    public List<Nodesensor> getNodesensorsForNOFirst(long lastDataVersion) {
        return dao.getNodesensorsForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveNodesensor(Nodesensor nodesensor) {
        return dao.saveNodesensor(nodesensor);
    }

    @Override
    public boolean updateNodesensor(Nodesensor nodesensor) {
        return dao.updateNodesensor(nodesensor);
    }

    @Override
    public boolean updateNodesensorDataVersion(Nodesensor nodesensor) {
        return dao.updateNodesensorDataVersion(nodesensor);
    }

    @Override
    public List<Mapping_area_site> getMapping_area_sitesForFirst() {
        return dao.getMapping_area_sitesForFirst();
    }

    @Override
    public List<Mapping_area_site> getMapping_area_sitesForNOFirst(
            int lastDataVersion) {
        return dao.getMapping_area_sitesForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveMapping_area_site(Mapping_area_site mapping_area_site) {
        return dao.saveMapping_area_site(mapping_area_site);
    }

    @Override
    public boolean updateMapping_area_site(Mapping_area_site mapping_area_site) {
        return dao.updateMapping_area_site(mapping_area_site);
    }

    @Override
    public boolean updateMapping_area_siteDataVersion(
            Mapping_area_site mapping_area_site) {
        return dao.updateMapping_area_siteDataVersion(mapping_area_site);
    }

    @Override
    public boolean deleteMapping_area_siteInfo() {
        return dao.deleteMapping_area_siteInfo();
    }

    @Override
    public boolean existNodeTable(String nodeid) {
        return dao.existNodeTable(nodeid);
    }

    @Override
    public boolean createNodeTable(String nodeid) {
        return dao.createNodeTable(nodeid);
    }

    @Override
    public boolean createDeviceLink(String nodeId){
        return dao.createDeviceLink(nodeId);
    }

    @Override
    public List<NodeCls> getNodesForFirst(String nodeid) {
        return dao.getNodesForFirst(nodeid);
    }

    @Override
    public List<NodeCls> getNodesForNOFirst(NodeCls node) {
        return dao.getNodesForNOFirst(node);
    }

    @Override
    public List<NodeCls> getNodesForNOFirst(String nodeid) {
        return dao.getNodesForNOFirst(nodeid);
    }

    @Override
    public boolean saveNode(NodeCls node) {
        return dao.saveNode(node);
    }

    @Override
    public boolean savaDeviceLink(DeviceLink deviceLink) {
        return dao.saveDeviceLink(deviceLink);
    }

    @Override
    public boolean updateNode(NodeCls node) {
        return dao.updateNode(node);
    }

    @Override
    public boolean updateNodeDataVersion(NodeCls node) {
        return dao.updateNodeDataVersion(node);
    }

    @Override
    public List<TmonitoringStation_info> getTmonitoringStation_infosForFirst() {
        return dao.getTmonitoringStation_infosForFirst();
    }

    @Override
    public List<TmonitoringStation_info> getTmonitoringStation_infosForNOFirst(
            long lastDataVersion) {
        return dao.getTmonitoringStation_infosForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveTmonitoringStation_info(
            TmonitoringStation_info monitoringStation_info) {
        return dao.saveTmonitoringStation_info(monitoringStation_info);
    }

    public boolean updateDeviceLinkDataVersion(DeviceLink deviceLink) {
        return dao.updateDeviceLinkDataVersion(deviceLink);
    }

    @Override
    public boolean updateTmonitoringStation_info(
            TmonitoringStation_info monitoringStation_info) {
        return dao.updateTmonitoringStation_info(monitoringStation_info);
    }

    @Override
    public boolean updateTmonitoringStation_infoDataVersion(
            TmonitoringStation_info monitoringStation_info) {
        return dao
                .updateTmonitoringStation_infoDataVersion(monitoringStation_info);
    }

    @Override
    public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForFirst() {
        return dao.getTnode_monitoringStation_infosForFirst();
    }

    @Override
    public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForNOFirst(
            long lastDataVersion) {
        return dao.getTnode_monitoringStation_infosForNOFirst(lastDataVersion);
    }

    @Override
    public boolean saveTnode_monitoringStation_info(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        return dao
                .saveTnode_monitoringStation_info(node_monitoringStation_info);
    }

    @Override
    public boolean updateTnode_monitoringStation_info(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        return dao
                .updateTnode_monitoringStation_info(node_monitoringStation_info);
    }

    @Override
    public boolean updateTnode_monitoringStation_infoDataVersion(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        return dao
                .updateTnode_monitoringStation_infoDataVersion(node_monitoringStation_info);
    }

    // ------------------------------------判断各表记录是否存在-----------------------------------------------

    @Override
    public boolean isExistNodeinfo(NodeInfo nodeInfo) {
        return dao.isExistNodeinfo(nodeInfo);
    }

    @Override
    public boolean isExistNodesensor(Nodesensor nodesensor) {
        return dao.isExistNodesensor(nodesensor);
    }

    @Override
    public boolean isExistNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        return dao.isExistNodeinfomemory(nodeinfomemory);
    }

    @Override
    public boolean isExistLogicGroup(LogicGroup logicGroup) {
        return dao.isExistLogicGroup(logicGroup);
    }

    @Override
    public boolean isExistHistorydatacount(Historydatacount historydatacount) {
        return dao.isExistHistorydatacount(historydatacount);
    }

    @Override
    public boolean isExistAvgdata(Avgdata avgdata) {
        return dao.isExistAvgdata(avgdata);
    }

    @Override
    public boolean isExistTbl_lxh_acc(Tbl_lxh_acc tab_lxh_acc) {
        return dao.isExistTbl_lxh_acc(tab_lxh_acc);
    }

    @Override
    public boolean isExistTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        return dao.isExistTbl_rb_day_acc(tbl_rb_day_acc);
    }

    @Override
    public boolean isExistTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        return dao.isExistTbl_rb_hour_acc(tbl_rb_hour_acc);
    }

    @Override
    public boolean isExistWindrose(Windrose windrose) {
        return dao.isExistWindrose(windrose);
    }

    @Override
    public boolean isExistNodedata(NodeCls nodedata) {
        return dao.isExistNodedata(nodedata);
    }

    @Override
    public boolean isExistDeviceLink(DeviceLink deviceLink) {
        return dao.isExistDeviceLink(deviceLink);
    }

    @Override
    public List<LogicGroup> getLogicGroupForFirst() {
        return dao.getLogicGroupForFirst();
    }

    @Override
    public List<LogicGroup> getLogicGroupForNoFirst(long lastDataVersion) {
        return dao.getLogicGroupForNoFirst(lastDataVersion);
    }

    @Override
    public boolean saveLogicGroup(LogicGroup logicGroup) {
        // 设置logicGroup为未激活和同步
        logicGroup.activeState = 1;
        logicGroup.logicGroupType = 2;
        return dao.saveLogicGroup(logicGroup);
    }

    @Override
    public boolean updateLogicGroup(LogicGroup logicGroup) {
        // 设置logicGroup为未激活和同步
        logicGroup.activeState = 1;
        logicGroup.logicGroupType = 2;
        return dao.updateLogicGroup(logicGroup);
    }

    @Override
    public boolean updateLogicGroupDataVersion(LogicGroup logicGroup) {
        return dao.updateLogicGroupDataVersion(logicGroup);
    }

}
