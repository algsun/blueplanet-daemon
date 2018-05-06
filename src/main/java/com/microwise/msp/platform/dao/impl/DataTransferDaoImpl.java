/**
 *
 */
package com.microwise.msp.platform.dao.impl;

import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.platform.bean.*;
import com.microwise.msp.platform.dao.DataTransferDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 数据同步 持久化实现
 *
 * 2012年4月5日 添加
 * isExistNodeinfo
 * isExistNodeSensor
 * isExistNodedata
 * isExistTbl_lxh_acc
 * isExistTbl_rb_day_acc
 * isExistTbl_rb_hour_acc
 * isExistWindrose
 * </pre>
 *
 * @author heming
 * @since 2011-11-01
 */
public class DataTransferDaoImpl extends SqlMapClient2SqlSessionAdapter implements
        DataTransferDao {

    private static Logger log = LoggerFactory.getLogger(DataTransferDaoImpl.class);

    /**
     * <pre>
     * 2012年3月29日 何明明
     *
     * 由调用DataTransfer.getCurrentSiteinfo改为DataTransfer.getCurrentSite
     * </pre>
     */
    @Override
    public Siteinfo getCurrentSiteinfo() {
        Siteinfo site = null;
        try {
            site = (Siteinfo) getSqlSession().selectOne("DataTransfer.getCurrentSite");
        } catch (DataAccessException e) {
            log.error("查询 getCurrentSiteinfo 异常,获取当前运行站点信息 ", e);

        }
        return site;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataBaseObject> getDataBaseObjects(DataBaseObject object) {
        List<DataBaseObject> rlist = new ArrayList<DataBaseObject>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getDataBaseObjects", object);
        } catch (DataAccessException e) {

            log.error("查询 getDataBaseObjects 异常", e);
        }
        return rlist;
    }

    // ======================数据表，同步状态========================
    @SuppressWarnings("unchecked")
    @Override
    public List<LogTransfer> getLogTransfers() {
        List<LogTransfer> rlist = new ArrayList<LogTransfer>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getLogTransfers");
        } catch (DataAccessException e) {
            log.error("查询 getLogTransfers 异常", e);

        }
        return rlist;
    }

    /**
     * <pre>
     * 何明明 2012年5月3日   修改为queryForObject
     * 何明明 2012年6月15日  将logTransfer=null
     * </pre>
     */
    @Override
    public LogTransfer getLogTransferByTableName(String tableName) {
        // LogTransfer logTransfer = new LogTransfer();
        LogTransfer logTransfer = null;
        try {
            logTransfer = (LogTransfer) getSqlSession().selectOne("DataTransfer.getLogTransferByTableName",
                    tableName);
        } catch (DataAccessException e) {
            logTransfer = null;

            log.error("查询 getLogTransferByTableName 异常", e);
        }
        return logTransfer;
    }

    @Override
    public boolean saveLogTransfer(LogTransfer object) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveLogTransfer", object);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增数据同步日志失败", e);

            log.error("\n\n ----" + object.tableName + " " + object.dataVersion
                    + " " + object.eventTime);
        }
        return isTrue;
    }

    @Override
    public boolean updateLogTransfer(LogTransfer object) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateLogTransfer", object);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("更新数据同步日志表中数据同步版本号dataVersion失败", e);

        }
        return isTrue;
    }

    // =====================nodeinfo，节点信息=========================
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfo> getNodeinfos() {
        List<NodeInfo> rlist = new ArrayList<NodeInfo>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodeinfos");
        } catch (DataAccessException e) {
            log.error("查询 getNodeinfos 异常 ", e);

        }
        return rlist;
    }

    @Override
    public List<DeviceLink> getDeviceLinks(String nodeId, long dataVersion) {
        Map<String, Object> parmater = new HashMap<String, Object>();
        parmater.put("nodeId", nodeId);
        parmater.put("dataVersion", dataVersion);
        List<DeviceLink> deviceLinks = new ArrayList<DeviceLink>();
        try {
            deviceLinks = getSqlSession().selectList("DataTransfer.getDeviceLinks",parmater);
        } catch (DataAccessException e) {
            log.error("查询设备链路信息异常（getDeviceLinks）", e);
        }
        return deviceLinks;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfo> getNodeinfosForFirst() {
        List<NodeInfo> rlist = new ArrayList<NodeInfo>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodeinfosForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getNodeinfosForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfo> getNodeinfosForNOFirst(long lastDataVersion) {
        List<NodeInfo> rlist = new ArrayList<NodeInfo>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodeinfosForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getNodeinfosForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveNodeinfo(NodeInfo nodeInfo) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveNodeinfo", nodeInfo);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 nodeinfo失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateNodeinfo(NodeInfo nodeInfo) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodeinfo",
                    nodeInfo);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;

            log.error("修改 nodeinfo失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateNodeinfoDataVersion(NodeInfo nodeInfo) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodeinfoDataVersion", nodeInfo);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("更新 nodeInfo--dataVersion失败", e);

        }
        return isTrue;
    }

    // ======================nodeinfomemory，节点内存数据=======================
    @SuppressWarnings("unchecked")
    @Override
    public List<Nodeinfomemory> getNodeinfomemorysForFirst() {
        List<Nodeinfomemory> rlist = new ArrayList<Nodeinfomemory>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodeinfomemorysForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getNodeinfomemorysForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nodeinfomemory> getNodeinfomemorysForNOFirst(
            long lastDataVersion) {
        List<Nodeinfomemory> rlist = new ArrayList<Nodeinfomemory>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodeinfomemorysForNOFirst",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getNodeinfomemorysForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeInfoAndNodeInfoMemory> getNodeInfoAndMemoryInfoList(
            long lastDataVersion) {
        List<NodeInfoAndNodeInfoMemory> rlist = new ArrayList<NodeInfoAndNodeInfoMemory>();
        try {
            rlist = getSqlSession().selectList(
                    "DataTransfer.getNodeInfoAndMemoryInfoList",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getNodeInfoAndMemoryInfoList 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveNodeinfomemory",
                    nodeinfomemory);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 nodeinfomemory 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodeinfomemory", nodeinfomemory);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateNodeinfomemory 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateNodeinfomemoryDataVersion(Nodeinfomemory nodeinfomemory) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodeinfomemoryDataVersion",
                    nodeinfomemory);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("更新 nodeinfomemory--dataVersion失败 ", e);

        }
        return isTrue;
    }

    // ======================LogicGroup，节点内存数据=======================
    @Override
    public List<LogicGroup> getLogicGroupForFirst() {
        List<LogicGroup> rlist = new ArrayList<LogicGroup>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getLogicGroupForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getLogicGroupForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LogicGroup> getLogicGroupForNoFirst(long lastDataVersion) {
        List<LogicGroup> rlist = new ArrayList<LogicGroup>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getLogicGroupForNoFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getLogicGroupForFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveLogicGroup(LogicGroup logicGroup) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveLogicGroup",
                    logicGroup);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveLogicGroup 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateLogicGroup(LogicGroup logicGroup) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateLogicGroup", logicGroup);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateLogicGroup 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateLogicGroupDataVersion(LogicGroup logicGroup) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateLogicGroupDataVersion", logicGroup);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("更新 updateLogicGroupDataVersion 失败 ", e);

        }
        return isTrue;
    }

    // =====================Historydatacount，历史数据索引表=========================
    @SuppressWarnings("unchecked")
    @Override
    public List<Historydatacount> getHistorydatacountForFirst() {
        List<Historydatacount> rlist = new ArrayList<Historydatacount>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getHistorydatacountForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getHistorydatacountForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Historydatacount> getHistorydatacountsForNOFirst(
            long lastDataVersion) {
        List<Historydatacount> rlist = new ArrayList<Historydatacount>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getHistorydatacountsForNOFirst",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getHistorydatacountsForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveHistorydatacount(Historydatacount historydatacount) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveHistorydatacount", historydatacount);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 historydatacount 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateHistorydatacount(Historydatacount historydatacount) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateHistorydatacount", historydatacount);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateHistorydatacount 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateHistorydatacountDataVersion(
            Historydatacount historydatacount) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateHistorydatacountDataVersion", historydatacount);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("更新 updateHistorydatacountDataVersion失败 ", e);

        }
        return isTrue;
    }

    // ========================avgdata，均峰值==================================
    @SuppressWarnings("unchecked")
    @Override
    public List<Avgdata> getAvgdatasForFirst() {
        List<Avgdata> rlist = new ArrayList<Avgdata>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getAvgdatasForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getAvgdatasForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Avgdata> getAvgdatasForNOFirst(long lastDataVersion) {
        List<Avgdata> rlist = new ArrayList<Avgdata>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getAvgdatasForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("getAvgdatasForNOFirst 异常 ", e);
            log.error("getAvgdatasForNOFirst(" + lastDataVersion + ")");

        }
        return rlist;
    }

    @Override
    public boolean saveAvgdata(Avgdata avgdata) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveAvgdata",
                    avgdata);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveAvgdata 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateAvgdata(Avgdata avgdata) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateAvgdata", avgdata);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateAvgdata 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateAvgdataDataVersion(Avgdata avgdata) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateAvgdataDataVersion", avgdata);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateAvgdataDataVersion 失败", e);

        }
        return isTrue;
    }

    // ======================tab_lxh_acc，日照量==========================
    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_lxh_acc> getTbl_lxh_accsForFirst() {
        List<Tbl_lxh_acc> rlist = new ArrayList<Tbl_lxh_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_lxh_accsForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getTbl_lxh_accsForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_lxh_acc> getTbl_lxh_accsForNOFirst(long lastDataVersion) {
        List<Tbl_lxh_acc> rlist = new ArrayList<Tbl_lxh_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_lxh_accsForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getTbl_lxh_accsForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveTbl_lxh_acc",
                    tbl_lxh_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveTbl_lxh_acc 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_lxh_acc(Tbl_lxh_acc tbl_lxh_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_lxh_acc", tbl_lxh_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_lxh_acc  失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_lxh_accDataVersion(Tbl_lxh_acc tbl_lxh_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_lxh_accDataVersion", tbl_lxh_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_lxh_accDataVersion  失败", e);

        }
        return isTrue;
    }

    // =======================tbl_rb_day_acc，日降雨量===========================
    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_rb_day_acc> getTbl_rb_day_accsForFirst() {
        List<Tbl_rb_day_acc> rlist = new ArrayList<Tbl_rb_day_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_rb_day_accsForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getTbl_rb_day_accsForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_rb_day_acc> getTbl_rb_day_accsForNOFirst(
            long lastDataVersion) {
        List<Tbl_rb_day_acc> rlist = new ArrayList<Tbl_rb_day_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_rb_day_accsForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getTbl_rb_day_accsForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveTbl_rb_day_acc",
                    tbl_rb_day_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 tbl_rb_day_acc 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_rb_day_acc", tbl_rb_day_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_rb_day_acc  失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_rb_day_accDataVersion(Tbl_rb_day_acc tbl_rb_day_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_rb_day_accDataVersion",
                    tbl_rb_day_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_rb_day_accDataVersion  失败", e);

        }
        return isTrue;
    }

    // =======================tbl_rb_hour_acc，小时降雨量==============================
    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForFirst() {
        List<Tbl_rb_hour_acc> rlist = new ArrayList<Tbl_rb_hour_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_rb_hour_accsForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getTbl_rb_hour_accsForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tbl_rb_hour_acc> getTbl_rb_hour_accsForNOFirst(
            long lastDataVersion) {
        List<Tbl_rb_hour_acc> rlist = new ArrayList<Tbl_rb_hour_acc>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTbl_rb_hour_accsForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getTbl_rb_hour_accsForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveTbl_rb_hour_acc", tbl_rb_hour_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveTbl_rb_hour_acc 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_rb_hour_acc", tbl_rb_hour_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_rb_hour_acc  失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateTbl_rb_hour_accDataVersion(
            Tbl_rb_hour_acc tbl_rb_hour_acc) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTbl_rb_hour_accDataVersion",
                    tbl_rb_hour_acc);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTbl_rb_hour_accDataVersion  失败", e);

        }
        return isTrue;
    }

    // =========================Windrose,风向玫瑰图===============================
    @SuppressWarnings("unchecked")
    @Override
    public List<Windrose> getWindrosesForFirst() {
        List<Windrose> rlist = new ArrayList<Windrose>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getWindrosesForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getWindrosesForFirst 异常 ", e);

        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Windrose> getWindrosesForNOFirst(long lastDataVersion) {
        List<Windrose> rlist = new ArrayList<Windrose>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getWindrosesForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getWindrosesForNOFirst 异常 ", e);

        }
        return rlist;
    }

    @Override
    public boolean saveWindrose(Windrose windrose) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveWindrose",
                    windrose);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveWindrose 失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateWindrose(Windrose windrose) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateWindrose", windrose);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateWindrose  失败", e);

        }
        return isTrue;
    }

    @Override
    public boolean updateWindroseDataVersion(Windrose windrose) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateWindroseDataVersion", windrose);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateWindroseDataVersion  失败", e);

        }
        return isTrue;
    }

    // =========================nodesensor,节点传感信息=====================
    @SuppressWarnings("unchecked")
    @Override
    public List<Nodesensor> getNodesensorsForFirst() {
        List<Nodesensor> rlist = new ArrayList<Nodesensor>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodesensorsForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getNodesensorsForFirst 异常 ", e);
        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nodesensor> getNodesensorsForNOFirst(long lastDataVersion) {
        List<Nodesensor> rlist = new ArrayList<Nodesensor>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodesensorsForNOFirst", lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getNodesensorsForNOFirst 异常 ", e);
        }
        return rlist;
    }

    @Override
    public boolean saveNodesensor(Nodesensor nodesensor) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveNodesensor",
                    nodesensor);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveNodesensor 失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateNodesensor(Nodesensor nodesensor) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodesensor", nodesensor);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateNodesensor  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateNodesensorDataVersion(Nodesensor nodesensor) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodesensorDataVersion", nodesensor);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateNodesensorDataVersion  失败", e);
        }
        return isTrue;
    }

    // ===========================mapping_area_site,站点级别对应信息====================
    @SuppressWarnings("unchecked")
    @Override
    public List<Mapping_area_site> getMapping_area_sitesForFirst() {
        List<Mapping_area_site> rlist = new ArrayList<Mapping_area_site>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getMapping_area_sitesForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getMapping_area_sitesForFirst 异常 ", e);
        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mapping_area_site> getMapping_area_sitesForNOFirst(
            int lastDataVersion) {
        List<Mapping_area_site> rlist = new ArrayList<Mapping_area_site>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getMapping_area_sitesForNOFirst",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getMapping_area_sitesForNOFirst 异常 ", e);
        }
        return rlist;
    }

    @Override
    public boolean saveMapping_area_site(Mapping_area_site mapping_area_site) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveMapping_area_site", mapping_area_site);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveMapping_area_site  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateMapping_area_site(Mapping_area_site mapping_area_site) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateMapping_area_site", mapping_area_site);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateMapping_area_site  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateMapping_area_siteDataVersion(
            Mapping_area_site mapping_area_site) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateMapping_area_siteDataVersion",
                    mapping_area_site);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateMapping_area_siteDataVersion  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean deleteMapping_area_siteInfo() {
        boolean isTrue = false;
        try {
            getSqlSession().delete("DataTransfer.deleteMapping_area_siteInfo");
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("删除 deleteMapping_area_siteInfo  失败", e);
        }
        return isTrue;
    }

    // =====================node节点表,多个存储节点数据=================================
    @Override
    public boolean existNodeTable(String nodeid) {
        boolean isTrue = false;
        try {
            isTrue = (Boolean) getSqlSession().selectOne("DataTransfer.existNodeTable", nodeid);
        } catch (DataAccessException e) {
            log.error("  existNodeTable:" + nodeid + " 异常", e);
        }
        return isTrue;
    }

    @Override
    public boolean createNodeTable(String nodeid) {
        boolean isTrue = true;
        try {
            getSqlSession().update("DataTransfer.createNodeTable",
                    nodeid);
        } catch (DataAccessException e) {
            isTrue = false;
            log.error(" 创建 节点信息表(createNodeTable):" + nodeid + " 出错", e);
        }
        return isTrue;
    }

    @Override
    public boolean createDeviceLink(String nodeId) {
        boolean isTrue = true;
        try {
            getSqlSession().update("DataTransfer.createDeviceLink",
                    nodeId);
        } catch (DataAccessException e) {
            isTrue = false;
            log.error(" 创建 节点信息表(createDeviceLink):" + nodeId + " 出错", e);
        }
        return isTrue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeCls> getNodesForFirst(String nodeid) {
        List<NodeCls> rlist = new ArrayList<NodeCls>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodesForFirst", nodeid);
        } catch (DataAccessException e) {
            log.error("查询 getNodesForFirst:" + nodeid + " 异常 ", e);
        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeCls> getNodesForNOFirst(NodeCls node) {
        List<NodeCls> rlist = new ArrayList<NodeCls>();
//		try {
//			rlist = getSqlSession().selectList("DataTransfer.getNodesForNOFirst", node);
//		} catch (DataAccessException e) {
//			log.error("查询 getNodesForNOFirst:" + node.nodeId + " 异常 ",e);
//		}
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeCls> getNodesForNOFirst(String nodeid) {
        List<NodeCls> rlist = new ArrayList<NodeCls>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getNodesForNOFirst", nodeid);
        } catch (DataAccessException e) {
            log.error("查询 getNodesForNOFirst:" + nodeid + " 异常 ", e);
        }
        return rlist;
    }

    @Override
    public boolean saveNode(NodeCls node) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveNode", node);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("\n\n 新增 saveNode:" + node.nodeid + " 失败 \n\n", e);
        }
        return isTrue;
    }

    @Override
    public boolean saveDeviceLink(DeviceLink deviceLink){
        boolean result = true;
        try {
            getSqlSession().insert("DataTransfer.saveDeviceLink",deviceLink);
        } catch (DataAccessException e) {
            result = false;
            log.error("\n\n 新增 saveDeviceLink:" + deviceLink.nodeId + " 失败 \n\n", e);
        }
        return result;
    }

    @Override
    public boolean updateNode(NodeCls node) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNode", node);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("\n\n 修改 updateNode:" + node.nodeid + "  失败 \n\n", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateDeviceLinkDataVersion(DeviceLink deviceLink) {
        boolean result = true;
        try {
            getSqlSession().update("DataTransfer.updateDeviceLinkDataVersion", deviceLink);
        } catch (DataAccessException e) {
            result = false;
            log.error("\n 修改 updateDeviceLinkDataVersion:" + deviceLink.nodeId + "失败\n", e);
        }
        return result;
    }


    @Override
    public boolean updateNodeDataVersion(NodeCls node) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateNodeDataVersion", node);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("\n\n 修改 updateNodeDataVersion:" + node.nodeid
                    + "  失败 \n\n", e);
        }
        return isTrue;
    }

    // =====================前台，监测点monitoringStation_info==================

    @SuppressWarnings("unchecked")
    @Override
    public List<TmonitoringStation_info> getTmonitoringStation_infosForFirst() {
        List<TmonitoringStation_info> rlist = new ArrayList<TmonitoringStation_info>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTmonitoringStation_infosForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getTmonitoringStation_infosForFirst  异常 ", e);
        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TmonitoringStation_info> getTmonitoringStation_infosForNOFirst(
            long lastDataVersion) {
        List<TmonitoringStation_info> rlist = new ArrayList<TmonitoringStation_info>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTmonitoringStation_infosForNOFirst",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getTmonitoringStation_infosForNOFirst  异常 ", e);
        }
        return rlist;
    }

    @Override
    public boolean saveTmonitoringStation_info(
            TmonitoringStation_info monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveTmonitoringStation_info",
                    monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveTmonitoringStation_info  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateTmonitoringStation_info(
            TmonitoringStation_info monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTmonitoringStation_info",
                    monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTmonitoringStation_info   失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateTmonitoringStation_infoDataVersion(
            TmonitoringStation_info monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTmonitoringStation_infoDataVersion",
                    monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTmonitoringStation_infoDataVersion   失败", e);
        }
        return isTrue;
    }

    // ============= 前台，设备节点_监测点对应信息node_monitoringStation_info==========
    @SuppressWarnings("unchecked")
    @Override
    public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForFirst() {
        List<Tnode_monitoringStation_info> rlist = new ArrayList<Tnode_monitoringStation_info>();
        try {
            rlist = getSqlSession().selectList("DataTransfer.getTnode_monitoringStation_infosForFirst");
        } catch (DataAccessException e) {
            log.error("查询 getTnode_monitoringStation_infosForFirst  异常 ", e);
        }
        return rlist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tnode_monitoringStation_info> getTnode_monitoringStation_infosForNOFirst(
            long lastDataVersion) {
        List<Tnode_monitoringStation_info> rlist = new ArrayList<Tnode_monitoringStation_info>();
        try {
            rlist = getSqlSession().selectList(
                    "DataTransfer.getTnode_monitoringStation_infosForNOFirst",
                    lastDataVersion);
        } catch (DataAccessException e) {
            log.error("查询 getTnode_monitoringStation_infosForNOFirst  异常 ", e);
        }
        return rlist;
    }

    @Override
    public boolean saveTnode_monitoringStation_info(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataTransfer.saveTnode_monitoringStation_info",
                    node_monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("新增 saveTnode_monitoringStation_info  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateTnode_monitoringStation_info(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTnode_monitoringStation_info",
                    node_monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTnode_monitoringStation_info  失败", e);
        }
        return isTrue;
    }

    @Override
    public boolean updateTnode_monitoringStation_infoDataVersion(
            Tnode_monitoringStation_info node_monitoringStation_info) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataTransfer.updateTnode_monitoringStation_infoDataVersion",
                    node_monitoringStation_info);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("修改 updateTnode_monitoringStation_infoDataVersion  失败", e);
        }
        return isTrue;
    }

    /**
     * <pre>
     * ----------------------------------------------------------------------------
     * 2012年4月5日 何明明
     * 判断data在数据库中是否已经存在
     *
     * &#064;return true or false
     */
    @Override
    public boolean isExistNodeinfo(NodeInfo nodeInfo) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistNodeinfo", nodeInfo);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistNodeinfo() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistDeviceLink(DeviceLink deviceLink) {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("nodeId", "m_device_" + deviceLink.nodeId);
        parameter.put("id", String.valueOf(deviceLink.id));
        boolean result = true;
        try {
            int count = getSqlSession().<Integer>selectOne("DataTransfer.isExistDeviceLink", parameter);
            result = count > 0;
        } catch (DataAccessException e) {
            result = false;
            log.error("\n\n DataTransferDaoImpl---isExistDeviceLink \n\n", e);
        }
        return result;
    }

    @Override
    public boolean isExistNodesensor(Nodesensor nodesensor) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistNodesensor", nodesensor);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistNodesensor() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistNodeinfomemory(Nodeinfomemory nodeinfomemory) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistNodeinfomemory", nodeinfomemory);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistNodeinfomemory() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistLogicGroup(LogicGroup logicGroup) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistLogicGroup", logicGroup);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistLogicGroup() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistHistorydatacount(Historydatacount historydatacount) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne(
                    "DataTransfer.isExistHistorydatacount", historydatacount);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log
                    .error("\n\n DataTransferDaoImpl→isExistHistorydatacount() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistNodedata(NodeCls nodedata) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistNodedata", nodedata);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n 判断节点一条历史数据是否存在: isExistNodedata(" + nodedata.nodeid
                    + nodedata.sensorPhysicalid + " "
                    + nodedata.sensorPhysicalvalue + " " + nodedata.createtime
                    + ") \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistAvgdata(Avgdata avgdata) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistAvgdata", avgdata);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistAvgdata() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistTbl_lxh_acc(Tbl_lxh_acc tab_lxh_acc) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistTbl_lxh_acc", tab_lxh_acc);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistTbl_lxh_acc() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistTbl_rb_day_acc(Tbl_rb_day_acc tbl_rb_day_acc) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistTbl_rb_day_acc", tbl_rb_day_acc);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistTbl_rb_day_acc() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistTbl_rb_hour_acc(Tbl_rb_hour_acc tbl_rb_hour_acc) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistTbl_rb_hour_acc", tbl_rb_hour_acc);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistTbl_rb_hour_acc() \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean isExistWindrose(Windrose windrose) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataTransfer.isExistWindrose", windrose);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n DataTransferDaoImpl→isExistWindrose() \n\n", e);
        }
        return isExist;
    }

}
