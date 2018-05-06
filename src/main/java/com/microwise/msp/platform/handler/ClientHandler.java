/**
 *
 */
package com.microwise.msp.platform.handler;

import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.platform.bean.*;
import com.microwise.msp.platform.service.DataTransferService;
import com.microwise.msp.util.AppContext;
import com.microwise.msp.util.StringUtil;
import com.microwise.msp.util.Tables;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <pre>
 * 数据同步Client端 Handler【业务处理器】
 * </pre>
 *
 * @author heming
 * @since 2011-11-01
 */
public class ClientHandler extends IoHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(ClientHandler.class);

    /**
     * 数据表同步的状态信息
     */
    private final ConcurrentMap<String, LogTransfer> logMap = new ConcurrentHashMap<String, LogTransfer>();

    /**
     * 系统当中所有节点Node,代表所有节点数据表
     */
    private List<NodeInfo> itemNodeInfoList = new ArrayList<NodeInfo>();

    /**
     * 未同步的节点号集合
     */
    private final List<String> noLoadedNodeInfoList = new ArrayList<String>();

    /**
     * 未同步的设备信息表集合
     */
    private final List<String> noLoadedDeviceLinks = new ArrayList<String>();


    private DataTransferService dataTransfer;

    private int count = 1;

    public ClientHandler() {
    }

    /**
     * <pre>
     * 同步时：
     * ①加载所有表的同步状态，封装为 Map[ tableName,LogTransfer ]
     * ②加载待同步的节点List[ NodeInfo ]
     * </pre>
     *
     * @author heming
     * @since 2011-11-05
     */
    public void logsToMap() {
        count = 1;
        // 初始化Service
        dataTransfer = (DataTransferService) AppContext.getInstance()
                .getAppContext().getBean("DataTransferService");

        // ①加载各表同步状态
        List<LogTransfer> list = dataTransfer.getLogTransfers();
        for (Iterator<LogTransfer> iterator = list.iterator(); iterator
                .hasNext(); ) {
            LogTransfer logTransfer = iterator.next();
            logMap.put(logTransfer.tableName, logTransfer);
        }

        log.info(" \n ================加载  【表同步日志】==================== \n");
        Iterator<LogTransfer> itemMap = logMap.values().iterator();
        while (itemMap.hasNext()) {
            LogTransfer temp = itemMap.next();
            log.info("---------加载同步日志： 表--" + temp.tableName + "  版本--"
                    + temp.dataVersion + "  时间戳--" + temp.eventTime);
        }

        // ②加载待同步的所有节点历史数据表名nodeid
        log.info(" \n =============加载待同步的 【节点历史数据表-表名nodeid】==== \n");
        itemNodeInfoList = dataTransfer.getNodeinfos();
        noLoadedNodeInfoList.clear();
        noLoadedDeviceLinks.clear();
        for (int i = 0; i < itemNodeInfoList.size(); i++) {
            NodeInfo temp = itemNodeInfoList.get(i);
            noLoadedDeviceLinks.add("m_device_" + temp.nodeid);
            log.info("---------需要同步的节点历史数据表：" + temp.nodeid);
            // 把节点数据添加到需要同步的历史数据内存中
            if (temp.nodeType == Defines.DEVICE_NODE
                    || temp.nodeType == Defines.DEVICE_NODE_MASTER
                    || temp.nodeType == Defines.DEVICE_NODE_SLAVE) {
                noLoadedNodeInfoList.add(temp.nodeid);
            }
        }
    }

    /**
     * 连接打开,开始写入数据
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logsToMap(); // 加载同步日志
        session.write(nodeInfoAndNodeInfoMemorySend());
    }

    /**
     * 设备信息+实时状态发送
     *
     * @return
     * @author he.ming
     * @since Apr 16, 2013
     */
    public TransferObject nodeInfoAndNodeInfoMemorySend() {
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.nodeinfomemory);
        long version = isExist ? logMap.get(Tables.nodeinfomemory).dataVersion
                : 0; // 表版本

        sendMessage.dataVersion = version;
        sendMessage.step = 1;
        sendMessage.tableName = Tables.nodeinfomemory;
        sendMessage.data = dataTransfer.getNodeInfoAndMemoryInfoList(version);

        if (!isExist) { // 首次同步
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.nodeinfomemory;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object); // 初始化该表同步状态
        }
        return sendMessage;
    }

    public TransferObject deviceLinkSend() {
        TransferObject sendMessage = new TransferObject();
        String nodeId = noLoadedDeviceLinks.get(0);
        boolean isExist = logMap.containsKey(nodeId);
        long version = isExist ? logMap.get(nodeId).dataVersion
                : 0; // 表版本
        sendMessage.dataVersion = version;
        sendMessage.step = 1;
        sendMessage.tableName = nodeId;
        sendMessage.data = dataTransfer.getDeviceLinks(nodeId, version);

        if (!isExist) { // 首次同步
            LogTransfer object = new LogTransfer();
            object.tableName = nodeId;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object); // 初始化该表同步状态
        }
        return sendMessage;
    }


    /**
     * <pre>
     * nodeinfomemory处理
     * </pre>
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean nodeinfomemoryProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n========== 更新 nodeinfomemory 数据版本  ==========\n ");
        List<NodeInfoAndNodeInfoMemory> list = (List<NodeInfoAndNodeInfoMemory>) acceptMessage.data;
        Iterator<NodeInfoAndNodeInfoMemory> itemNodeinfoIterator = list
                .iterator();

        // 更新记录版本
        while (itemNodeinfoIterator.hasNext()) {
            NodeInfoAndNodeInfoMemory object = itemNodeinfoIterator.next();
            Nodeinfomemory nodeinfomemory = new Nodeinfomemory();
            nodeinfomemory.nodeid = object.nodeid;
            nodeinfomemory.dataVersion = object.dataVersion;
            dataTransfer.updateNodeinfomemoryDataVersion(nodeinfomemory);
        }
        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);
        log.info(" \n========== 更新 nodeinfomemory 表版本为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==========\n ");
        return isTrue;
    }

    /**
     * nodesensor发送
     *
     * @return TransferObject
     */
    public TransferObject nodesensorSend() {
        log.info(" \n========== 发送 nodesensor  ==========\n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.nodesensor);
        long t_version = isExist ? logMap.get(Tables.nodesensor).dataVersion
                : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.nodesensor;
            sendMessage.data = dataTransfer.getNodesensorsForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.nodesensor;
            sendMessage.data = dataTransfer.getNodesensorsForFirst();

            LogTransfer object = new LogTransfer();
            object.tableName = Tables.nodesensor;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object); // 初始化该表同步日志
        }
        return sendMessage;
    }

    /**
     * nodesensor处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean nodesensorProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n========== 更新 nodesensor 数据版本 ==========\n ");
        List<Nodesensor> list = (List<Nodesensor>) acceptMessage.data;
        Iterator<Nodesensor> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Nodesensor object = item.next();
            dataTransfer.updateNodesensorDataVersion(object);
        }
        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);
        log.info(" \n========== 更新 nodesensor 表版本为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==========\n ");
        return isTrue;
    }

    /**
     * <pre>
     * avgdata发送
     * </pre>
     *
     * @return TransferObject
     */
    public TransferObject avgdataSend() {
        log.info(" \n========== 发送(均峰值)avgdata  ==========\n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.avgdata);
        long t_version = isExist ? logMap.get(Tables.avgdata).dataVersion : 0;
        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.avgdata;
            sendMessage.data = dataTransfer.getAvgdatasForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.avgdata;
            sendMessage.data = dataTransfer.getAvgdatasForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.avgdata;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * <pre>
     * avgdata处理
     * </pre>
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean avgdataProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n========== 更新(均峰值)avgdata 数据版本  ==========\n ");
        List<Avgdata> list = (List<Avgdata>) acceptMessage.data;
        Iterator<Avgdata> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Avgdata object = item.next();
            dataTransfer.updateAvgdataDataVersion(object);
        }

        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer); // 更新同步状态dataVersion
        log.info(" \n========== 更新(均峰值)avgdata 数据版本 为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==========\n ");
        return isTrue;
    }

    /**
     * <pre>
     * windrose发送
     * </pre>
     *
     * @return TransferObject
     */
    public TransferObject windroseSend() {
        log.info(" \n========== 发送(风向玫瑰)windrose  ==========\n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.windrose);
        long t_version = isExist ? logMap.get(Tables.windrose).dataVersion : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.windrose;
            sendMessage.data = dataTransfer.getWindrosesForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.windrose;
            sendMessage.data = dataTransfer.getWindrosesForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.windrose;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * windrose处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean windroseProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n========== 更新(风向玫瑰)windrose 数据版本  ==========\n ");
        List<Windrose> list = (List<Windrose>) acceptMessage.data;
        Iterator<Windrose> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Windrose object = item.next();
            dataTransfer.updateWindroseDataVersion(object);
        }
        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);

        log.info(" \n========== 更新(风向玫瑰)windrose 表版本为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==========\n ");
        return isTrue;
    }

    /**
     * tbl_rb_day_acc(日降雨量)发送
     *
     * @return TransferObject
     */
    public TransferObject tbl_rb_day_accSend() {
        log.info(" \n========== 发送(日降雨量)tbl_rb_day_acc  ==========\n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.tbl_rb_day_acc);
        long t_version = isExist ? logMap.get(Tables.tbl_rb_day_acc).dataVersion
                : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_rb_day_acc;
            sendMessage.data = dataTransfer
                    .getTbl_rb_day_accsForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_rb_day_acc;
            sendMessage.data = dataTransfer.getTbl_rb_day_accsForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.tbl_rb_day_acc;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * tbl_rb_day_acc(日降雨量)处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean tbl_rb_day_accProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n====  更新(日降雨量)tbl_rb_day_acc 数据版本  ==== \n ");
        List<Tbl_rb_day_acc> list = (List<Tbl_rb_day_acc>) acceptMessage.data;
        Iterator<Tbl_rb_day_acc> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Tbl_rb_day_acc object = item.next();
            dataTransfer.updateTbl_rb_day_accDataVersion(object);
        }

        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);

        log.info(" \n====  更新(日降雨量)tbl_rb_day_acc 数据版本 为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==== \n ");
        return isTrue;
    }

    /**
     * tbl_rb_hour_acc(小时降雨量)发送
     *
     * @return TransferObject
     */
    public TransferObject tbl_rb_hour_accSend() {
        log.info(" \n====  发送(小时降雨量)tbl_rb_hour_acc  ==== \n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.tbl_rb_hour_acc);
        long t_version = isExist ? logMap.get(Tables.tbl_rb_hour_acc).dataVersion
                : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_rb_hour_acc;
            sendMessage.data = dataTransfer
                    .getTbl_rb_hour_accsForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_rb_hour_acc;
            sendMessage.data = dataTransfer.getTbl_rb_hour_accsForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.tbl_rb_hour_acc;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * tbl_rb_hour_acc(小时降雨量)处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean tbl_rb_hour_accProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n====  更新(小时降雨量)tbl_rb_hour_acc 数据版本 ==== \n ");
        List<Tbl_rb_hour_acc> list = (List<Tbl_rb_hour_acc>) acceptMessage.data;
        Iterator<Tbl_rb_hour_acc> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Tbl_rb_hour_acc object = item.next();
            dataTransfer.updateTbl_rb_hour_accDataVersion(object);
        }

        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);

        log.info(" \n====  更新(小时降雨量)tbl_rb_hour_acc 数据版本 为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==== \n ");
        return isTrue;
    }

    /**
     * tbl_lxh_acc(日照量)发送
     *
     * @return TransferObject
     */
    public TransferObject tbl_lxh_accSend() {
        log.info(" \n==== 发送(日照量)tbl_lxh_acc  ===== \n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.tbl_lxh_acc);
        long t_version = isExist ? logMap.get(Tables.tbl_lxh_acc).dataVersion
                : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_lxh_acc;
            sendMessage.data = dataTransfer
                    .getTbl_lxh_accsForNOFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.tbl_lxh_acc;
            sendMessage.data = dataTransfer.getTbl_lxh_accsForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.tbl_lxh_acc;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * tbl_lxh_acc(日照量)处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean tbl_lxh_accProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n===== 更新(日照量)tbl_lxh_acc 数据版本 ==== \n ");

        List<Tbl_lxh_acc> list = (List<Tbl_lxh_acc>) acceptMessage.data;
        Iterator<Tbl_lxh_acc> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            Tbl_lxh_acc object = item.next();
            dataTransfer.updateTbl_lxh_accDataVersion(object);
        }

        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);

        log.info(" \n=====  更新(日照量)tbl_lxh_acc 数据版本 为："
                + logTransfer.dataVersion + "[" + isTrue + "] ==== \n ");
        return isTrue;
    }

    /**
     * 发送单个(节点历史数据表)
     *
     * @return TransferObject
     */
    public TransferObject singleNodeSend() {
        TransferObject sendMessage = new TransferObject();
        // 获取单个节点号
        String nodeid = noLoadedNodeInfoList.get(0);
        boolean isExist = logMap.containsKey(nodeid);
        long t_version = isExist ? logMap.get(nodeid).dataVersion : 0; // 表版本

        log.info(" \n==== 发送历史数据(" + nodeid + ")  ==== \n ");

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = nodeid;
            sendMessage.data = dataTransfer.getNodesForNOFirst(nodeid);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = nodeid;
            sendMessage.data = dataTransfer.getNodesForFirst(nodeid);

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = nodeid;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }


    public void deviceProcess(TransferObject receiveMessage) {
        log.info(" \n==== 更新(" + receiveMessage.tableName+ ")数据版本  ==== \n ");
        List<DeviceLink> deviceLinks = (List<DeviceLink>) receiveMessage.data;
        // 更新记录版本
        for(DeviceLink deviceLink : deviceLinks){
            dataTransfer.updateDeviceLinkDataVersion(deviceLink);
            log.debug(" \n==== 更新" + deviceLink.nodeId + " " + deviceLink.stamp
                    + deviceLink.dataVersion + "==== \n ");
        }
        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = receiveMessage.tableName;
        logTransfer.dataVersion = receiveMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        dataTransfer.updateLogTransfer(logTransfer);
        log.info("\n ====更新" + logTransfer.tableName + "的版本号为：" + logTransfer.dataVersion + " \n");
    }

    /**
     * node 处理
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public void nodeProcess(Object message) {
        TransferObject acceptMessage = (TransferObject) message;

        log.info(" \n==== 更新(" + acceptMessage.tableName
                + ")数据版本  ==== \n ");

        // ===================处理单个node同步结果=====================
        List<NodeCls> list = (List<NodeCls>) acceptMessage.data;
        Iterator<NodeCls> item = list.iterator(); // Server回送结果
        // 更新记录版本
        while (item.hasNext()) {
            NodeCls object = item.next();
            dataTransfer.updateNodeDataVersion(object);
            log.debug(" \n==== 更新" + object.nodeid + " " + object.createtime
                    + " " + " " + object.sensorPhysicalid + " "
                    + object.sensorPhysicalvalue + " 版本为: "
                    + object.dataVersion + "==== \n ");
        }
        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        dataTransfer.updateLogTransfer(logTransfer);
        log.info("\n ====更新" + logTransfer.tableName + "的状态为："
                + logTransfer.dataVersion + " \n");
        // ===================处理单个node同步结果=====================
    }

    /**
     * <pre>
     * 发送(逻辑站点)logicGroup
     * </pre>
     *
     * @return TransferObject
     */
    public TransferObject logicGruopSend() {
        log.info(" \n==== 发送(逻辑站点)logicGroup  ==== \n ");
        TransferObject sendMessage = new TransferObject();
        boolean isExist = logMap.containsKey(Tables.logicGroup);
        long t_version = isExist ? logMap.get(Tables.logicGroup).dataVersion
                : 0; // 表版本

        if (isExist && t_version > 0) { // 非首次同步
            sendMessage.dataVersion = t_version;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.logicGroup;
            sendMessage.data = dataTransfer.getLogicGroupForNoFirst(t_version);
        } else { // 首次同步
            sendMessage.dataVersion = 0;
            sendMessage.step = 1;
            sendMessage.tableName = Tables.logicGroup;
            sendMessage.data = dataTransfer.getLogicGroupForFirst();

            // 初始化该表同步日志
            LogTransfer object = new LogTransfer();
            object.tableName = Tables.logicGroup;
            object.dataVersion = 0;
            object.eventTime = StringUtil.nowTimestamp();
            dataTransfer.saveLogTransfer(object);
        }
        return sendMessage;
    }

    /**
     * logicGroup处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean logicGruopProcess(TransferObject acceptMessage) {
        boolean isTrue = false;
        log.info(" \n==== 更新logicgroup ==== \n ");
        List<LogicGroup> list = (List<LogicGroup>) acceptMessage.data;
        Iterator<LogicGroup> item = list.iterator();

        // 更新记录版本
        while (item.hasNext()) {
            LogicGroup object = item.next();
            dataTransfer.updateLogicGroupDataVersion(object);
        }

        // 更新表版本
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.tableName = acceptMessage.tableName;
        logTransfer.dataVersion = acceptMessage.dataVersion;
        logTransfer.eventTime = StringUtil.nowTimestamp();
        isTrue = dataTransfer.updateLogTransfer(logTransfer);
        log.info(" \n==== 更新logicgroup ==== " + logTransfer.dataVersion + "["
                + isTrue + "] ==== \n ");
        return isTrue;
    }

    /**
     * 消息业务处理
     *
     * @param session
     * @param message
     */
    public void msgHandler(IoSession session, Object message) {
        if (message instanceof TransferObject) {
            TransferObject acceptMessage = (TransferObject) message;
            if (Tables.nodeinfomemory.equals(acceptMessage.tableName)) { // 处理nodeinfo,发送nodesensor
                nodeinfomemoryProcess(acceptMessage);
                count++;
                session.write(nodesensorSend());
            } else if (Tables.nodesensor.equals(acceptMessage.tableName)) { // 处理nodesensor,发送nodeinfomemory
                nodesensorProcess(acceptMessage);
                count++;
                session.write(avgdataSend());
            } else if (Tables.avgdata.equals(acceptMessage.tableName)) { // 处理avgdata,发送windrose
                avgdataProcess(acceptMessage);
                count++;
                session.write(windroseSend());
            } else if (Tables.windrose.equals(acceptMessage.tableName)) { // 处理windrose,发送tbl_rb_day_acc
                windroseProcess(acceptMessage);
                count++;
                session.write(tbl_rb_day_accSend());
            } else if (Tables.tbl_rb_day_acc.equals(acceptMessage.tableName)) { // 处理tbl_rb_day_acc,发送tbl_rb_hour_acc
                tbl_rb_day_accProcess(acceptMessage);
                count++;
                session.write(tbl_rb_hour_accSend());
            } else if (Tables.tbl_rb_hour_acc.equals(acceptMessage.tableName)) { // 处理tbl_rb_hour_acc,发送tbl_lxh_acc
                tbl_rb_hour_accProcess(acceptMessage);
                count++;
                session.write(tbl_lxh_accSend());
            } else if (Tables.tbl_lxh_acc.equals(acceptMessage.tableName)) { // 处理tbl_lxh_acc
                tbl_lxh_accProcess(acceptMessage);
                count++;
                session.write(logicGruopSend());
            } else if (Tables.logicGroup.equals(acceptMessage.tableName)) {
                logicGruopProcess(acceptMessage);
                count++;
                session.write(deviceLinkSend());
            } else if (Tables.deviceLink.equals(acceptMessage.tableName.substring(0, 9))) {
                deviceProcess(acceptMessage); // 处理节点历史数据同步结果
                if (!noLoadedDeviceLinks.isEmpty()) { // 处理节点历史数据一系列nodeid表
                    count++;
                    session.write(deviceLinkSend()); // 发送单个历史数据
                    noLoadedDeviceLinks.remove(0); // 删除已经同步的节点
                }else{
                    session.write(singleNodeSend());
                }
            } else {
                nodeProcess(message); // 处理节点历史数据同步结果
                if (!noLoadedNodeInfoList.isEmpty()) { // 处理节点历史数据一系列nodeid表
                    count++;
                    session.write(singleNodeSend()); // 发送单个历史数据
                    noLoadedNodeInfoList.remove(0); // 删除已经同步的节点
                } else {
                    count = 1; // 重置标识
                    session.write("exit"); // 发送断开连接标识
                    log.info(" \n==== 将标识(exit)发往tcpServer  \n ");
                    session.close(false); // 关闭IoSession，该操作时异步的，true为立即关闭，false为所有写操作都flush后关闭
                    // session.getService().dispose(); //
                    // IoSession.close()仅仅是关闭TCP连接通道，并未关闭Client端程序
                }
            }
        }
    }

    /**
     * 接收到message时触发
     */
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        log.info(" \n-----第(" + count + ")次 调用msgHandler(),开始业务处理--- \n ");
        msgHandler(session, message);
    }

    public DataTransferService getDataTransfer() {
        return dataTransfer;
    }

    public void setDataTransfer(DataTransferService dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

}
