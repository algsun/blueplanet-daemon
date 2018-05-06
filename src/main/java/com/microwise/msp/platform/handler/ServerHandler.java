package com.microwise.msp.platform.handler;

import com.microwise.msp.platform.bean.*;
import com.microwise.msp.platform.service.DataTransferService;
import com.microwise.msp.util.AppContext;
import com.microwise.msp.util.Tables;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 数据同步Server端 Handler【业务处理器】
 * </pre>
 *
 * @author heming
 * @since 2011-11-01
 */
public class ServerHandler extends IoHandlerAdapter implements IoHandler {
    private static Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private DataTransferService dataTransfer;

    private int count = 1;

    /**
     * 创建连接，先于sessionOpened()触发
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        count = 1;
        dataTransfer = (DataTransferService) AppContext.getInstance()
                .getAppContext().getBean("DataTransferService");
    }

    /**
     * <pre>
     * 当连接后打开时触发 ，sessionCreated()之后
     * 一般此方法与 sessionCreated 同时触发, 此时可 session.write(new Date());
     * </pre>
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info(" \n----- sessionOpened  连接打开:"
                + session.getRemoteAddress() + " ---------------- \n");
        // session.write("11");
    }

    /**
     * <pre>
     * nodeInfo,nodeInfoMemory处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject nodeInfoAndNodeInfoMemoryProcess(
            TransferObject acceptMessage) {

        log.info(" \n==========同步(节点信息)nodeinfo ==========\n ");
        List<NodeInfoAndNodeInfoMemory> rList = new ArrayList<NodeInfoAndNodeInfoMemory>();

        List<NodeInfoAndNodeInfoMemory> list = (List<NodeInfoAndNodeInfoMemory>) acceptMessage.data;

        long t_version = acceptMessage.dataVersion;

        // 拆分nodeInfo表和nodeInfomemory表中的数据
        for (NodeInfoAndNodeInfoMemory temp : list) {
            // 提取nodeInfo表的数据
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.nodeid = temp.nodeid;
            nodeInfo.nodeType = temp.nodeType;
            nodeInfo.createtime = temp.createtime;
            nodeInfo.dataVersion = 0;
            nodeInfo.deviceImage = temp.deviceImage;
            nodeInfo.isActive = temp.isActive;
            nodeInfo.binding = temp.binding;
            nodeInfo.siteId = temp.siteId;
            nodeInfo.x = temp.x;
            nodeInfo.y = temp.y;
            nodeInfo.z = temp.z;

            // 提取nodeInfomemory 表中的数据
            Nodeinfomemory nodeinfomemory = new Nodeinfomemory();
            nodeinfomemory.id = temp.id;
            nodeinfomemory.dataVersion = temp.dataVersion;
            nodeinfomemory.interval_i = temp.interval_i;
            nodeinfomemory.anomaly = temp.anomaly;
            nodeinfomemory.childIP = temp.childIP;
            nodeinfomemory.deviceMode = temp.deviceMode;
            nodeinfomemory.feedbackIP = temp.feedbackIP;
            nodeinfomemory.isControl = temp.isControl;
            nodeinfomemory.lowvoltage = temp.lowvoltage;
            nodeinfomemory.lqi = temp.lqi;
            nodeinfomemory.nodeVersion = temp.nodeVersion;
            nodeinfomemory.nodeid = temp.nodeid;
            nodeinfomemory.parentIP = temp.parentIP;
            nodeinfomemory.remoteIp = temp.remoteIp;
            nodeinfomemory.remotePort = temp.remotePort;
            nodeinfomemory.rssi = temp.rssi;
            nodeinfomemory.sdCardState = temp.sdCardState;
            nodeinfomemory.sequence = temp.sequence;
            nodeinfomemory.stamp = temp.stamp;
            nodeinfomemory.emptyStamp = temp.emptyStamp;

            boolean isExistNodeinfo = dataTransfer.isExistNodeinfo(nodeInfo);
            boolean isSaveNodeinfo = false;
            boolean isExistNodeInfoMemory = false;
            boolean isSaveNodeInfoMemory = false;

            if (!isExistNodeinfo) {
                isSaveNodeinfo = dataTransfer.saveNodeinfo(nodeInfo);
            } else {
                isSaveNodeinfo = true;
            }
            if (isSaveNodeinfo) {
                isExistNodeInfoMemory = dataTransfer
                        .isExistNodeinfomemory(nodeinfomemory);
                if (!isExistNodeInfoMemory) {
                    isSaveNodeInfoMemory = dataTransfer
                            .saveNodeinfomemory(nodeinfomemory);
                } else {
                    isSaveNodeInfoMemory = dataTransfer
                            .updateNodeinfomemory(nodeinfomemory);
                }
            }

            if (isSaveNodeInfoMemory) { // 判断nodeinfomemory成功与否
                temp.dataVersion = t_version + 1;
            } else if (temp.dataVersion > 0) {
                temp.dataVersion = t_version + 2;
            }
            rList.add(temp);
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.tableName = acceptMessage.tableName;
        sendMessage.step = acceptMessage.step + 1;
        return sendMessage;
    }

    /**
     * <pre>
     * nodeinfo处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     * @deprecated 2013-4-16
     */
    @SuppressWarnings("unchecked")
    public TransferObject nodeinfoProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(节点信息)nodeinfo ==========\n ");
        List<NodeInfo> rList = new ArrayList<NodeInfo>(); // 定义需要返回给Client的数据
        List<NodeInfo> list = (List<NodeInfo>) acceptMessage.data;
        Iterator<NodeInfo> item = list.iterator(); // 接收到client的数据

        long version = acceptMessage.dataVersion;
        if (version == 0) { // 首次同步
            while (item.hasNext()) {
                NodeInfo temp = item.next();
                boolean isExist = dataTransfer.isExistNodeinfo(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveNodeinfo(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1;
                        rList.add(temp);
                    }
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                NodeInfo temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistNodeinfo(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveNodeinfo(temp);
                        if (isSave) {
                            temp.dataVersion = version + 1;
                            rList.add(temp);
                        }
                    }
                } else if (temp.dataVersion > 0) { // 旧记录,设计方案：temp.dataVersion=-1时为删除标记
                    boolean isSave = dataTransfer.updateNodeinfo(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1; // update过的旧记录将数据版本号置为跟表数据版本号一致
                        rList.add(temp);
                    }
                }
            }
        }
        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList; // 加入返回对象
        sendMessage.dataVersion = version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * nodeinfomemory处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     * @deprecated 2013-4-16
     */
    @SuppressWarnings("unchecked")
    public TransferObject nodeinfomemoryProcess(TransferObject acceptMessage) {
        log
                .info(" \n==========同步(节点实时数据)nodeinfomemoryProcess ==========\n ");
        List<Nodeinfomemory> rList = new ArrayList<Nodeinfomemory>(); // 定义需要返回给Client的数据
        List<Nodeinfomemory> list = (List<Nodeinfomemory>) acceptMessage.data;
        Iterator<Nodeinfomemory> item = list.iterator(); // 接收到client的数据

        long version = acceptMessage.dataVersion;
        if (version == 0) { // 首次同步
            while (item.hasNext()) {
                Nodeinfomemory temp = item.next();
                boolean isExist = dataTransfer.isExistNodeinfomemory(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveNodeinfomemory(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1;
                        rList.add(temp);
                    }
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Nodeinfomemory temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistNodeinfomemory(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveNodeinfomemory(temp);
                        if (isSave) {
                            temp.dataVersion = version + 1;
                            rList.add(temp);
                        }
                    }
                } else if (temp.dataVersion > 0) { // 旧记录,设计方案：temp.dataVersion=-1时为删除标记
                    boolean isSave = dataTransfer.updateNodeinfomemory(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1; // update过的旧记录将数据版本号置为跟表数据版本号一致
                        rList.add(temp);
                    }
                }
            }
        }
        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList; // 加入返回对象
        sendMessage.dataVersion = version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * nodesensor处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject nodesensorProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步 nodesensor  ==========\n ");
        List<Nodesensor> rList = new ArrayList<Nodesensor>(); // 定义需要返回给Client的数据
        List<Nodesensor> list = (List<Nodesensor>) acceptMessage.data;
        Iterator<Nodesensor> item = list.iterator(); // 接收到client的数据

        long t_version = acceptMessage.dataVersion;

        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Nodesensor temp = item.next();
                boolean isExist = dataTransfer.isExistNodesensor(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveNodesensor(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Nodesensor temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistNodesensor(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveNodesensor(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateNodesensor(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * logicGroup处理
     *
     * @param acceptMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    public TransferObject logicGroupProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(站点组数据)logicGroupProcess ==========\n ");
        List<LogicGroup> rList = new ArrayList<LogicGroup>(); // 定义需要返回给Client的数据
        List<LogicGroup> list = (List<LogicGroup>) acceptMessage.data;
        Iterator<LogicGroup> item = list.iterator(); // 接收到client的数据

        long version = acceptMessage.dataVersion;
        if (version == 0) { // 首次同步
            while (item.hasNext()) {
                LogicGroup temp = item.next();
                boolean isExist = dataTransfer.isExistLogicGroup(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveLogicGroup(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1;
                        rList.add(temp);
                    }
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                LogicGroup temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistLogicGroup(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveLogicGroup(temp);
                        if (isSave) {
                            temp.dataVersion = version + 1;
                            rList.add(temp);
                        }
                    }
                } else if (temp.dataVersion > 0) { // 旧记录,设计方案：temp.dataVersion=-1时为删除标记
                    boolean isSave = dataTransfer.updateLogicGroup(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1; // update过的旧记录将数据版本号置为跟表数据版本号一致
                        rList.add(temp);
                    }
                }
            }
        }
        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList; // 加入返回对象
        sendMessage.dataVersion = version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * historydatacount处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject historydatacountProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(历史数据索引)historydatacount ==========\n ");
        List<Historydatacount> rList = new ArrayList<Historydatacount>(); // 定义需要返回给Client的数据
        List<Historydatacount> list = (List<Historydatacount>) acceptMessage.data;
        Iterator<Historydatacount> item = list.iterator(); // 接收到client的数据

        long version = acceptMessage.dataVersion;
        if (version == 0) { // 首次同步
            while (item.hasNext()) {
                Historydatacount temp = item.next();
                boolean isExist = dataTransfer.isExistHistorydatacount(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveHistorydatacount(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1;
                        rList.add(temp);
                    }
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Historydatacount temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer
                            .isExistHistorydatacount(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer
                                .saveHistorydatacount(temp);
                        if (isSave) {
                            temp.dataVersion = version + 1;
                            rList.add(temp);
                        }
                    }
                } else if (temp.dataVersion > 0) { // 旧记录,设计方案：temp.dataVersion=-1时为删除标记
                    boolean isSave = dataTransfer.updateHistorydatacount(temp);
                    if (isSave) {
                        temp.dataVersion = version + 1; // update过的旧记录将数据版本号置为跟表数据版本号一致
                        rList.add(temp);
                    }
                }
            }
        }
        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList; // 加入返回对象
        sendMessage.dataVersion = version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * avgdata处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject avgdataProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(均峰值)avgdata ==========\n ");
        List<Avgdata> rList = new ArrayList<Avgdata>(); // 定义需要返回给Client的数据
        List<Avgdata> list = (List<Avgdata>) acceptMessage.data;
        Iterator<Avgdata> item = list.iterator();

        long t_version = acceptMessage.dataVersion;
        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Avgdata temp = item.next();
                boolean isExist = dataTransfer.isExistAvgdata(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveAvgdata(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Avgdata temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistAvgdata(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveAvgdata(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateAvgdata(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * windrose处理
     * </pre>
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject windroseProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(风向玫瑰)windrose ==========\n ");
        List<Windrose> rList = new ArrayList<Windrose>(); // 定义需要返回给Client的数据
        List<Windrose> list = (List<Windrose>) acceptMessage.data;
        Iterator<Windrose> item = list.iterator(); // 接收到client的数据

        long t_version = acceptMessage.dataVersion;
        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Windrose temp = item.next();
                boolean isExist = dataTransfer.isExistWindrose(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveWindrose(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Windrose temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistWindrose(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveWindrose(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateWindrose(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * tbl_rb_day_acc处理
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject tbl_rb_day_accProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(日降雨量)tbl_rb_day_acc   ==========\n ");
        List<Tbl_rb_day_acc> rList = new ArrayList<Tbl_rb_day_acc>(); // 定义需要返回给Client的数据
        List<Tbl_rb_day_acc> list = (List<Tbl_rb_day_acc>) acceptMessage.data;
        Iterator<Tbl_rb_day_acc> item = list.iterator(); // 接收到client的数据

        long t_version = acceptMessage.dataVersion;
        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Tbl_rb_day_acc temp = item.next();
                boolean isExist = dataTransfer.isExistTbl_rb_day_acc(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveTbl_rb_day_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Tbl_rb_day_acc temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistTbl_rb_day_acc(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveTbl_rb_day_acc(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateTbl_rb_day_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * tbl_rb_hour_acc处理
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject tbl_rb_hour_accProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(小时降雨量)tbl_rb_hour_acc ==========\n ");
        List<Tbl_rb_hour_acc> rList = new ArrayList<Tbl_rb_hour_acc>(); // 定义需要返回给Client的数据
        List<Tbl_rb_hour_acc> list = (List<Tbl_rb_hour_acc>) acceptMessage.data;
        Iterator<Tbl_rb_hour_acc> item = list.iterator(); // 接收到client的数据

        long t_version = acceptMessage.dataVersion;
        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Tbl_rb_hour_acc temp = item.next();
                boolean isExist = dataTransfer.isExistTbl_rb_hour_acc(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveTbl_rb_hour_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Tbl_rb_hour_acc temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistTbl_rb_hour_acc(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveTbl_rb_hour_acc(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateTbl_rb_hour_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * tbl_lxh_acc处理
     *
     * @param acceptMessage
     * @return TransferObject
     */
    @SuppressWarnings("unchecked")
    public TransferObject tbl_lxh_accProcess(TransferObject acceptMessage) {
        log.info(" \n==========同步(日照量)tbl_lxh_acc ==========\n ");
        List<Tbl_lxh_acc> rList = new ArrayList<Tbl_lxh_acc>(); // 定义需要返回给Client的数据
        List<Tbl_lxh_acc> list = (List<Tbl_lxh_acc>) acceptMessage.data;
        Iterator<Tbl_lxh_acc> item = list.iterator(); // 接收到client的数据

        long t_version = acceptMessage.dataVersion;

        if (t_version == 0) { // 首次同步
            while (item.hasNext()) {
                Tbl_lxh_acc temp = item.next();
                boolean isExist = dataTransfer.isExistTbl_lxh_acc(temp);
                if (!isExist) {
                    boolean isSave = dataTransfer.saveTbl_lxh_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    }
                    rList.add(temp);
                }
            }
        } else { // 非首次同步
            while (item.hasNext()) {
                Tbl_lxh_acc temp = item.next();
                if (temp.dataVersion == 0) { // 新记录
                    boolean isExist = dataTransfer.isExistTbl_lxh_acc(temp);
                    if (!isExist) {
                        boolean isSave = dataTransfer.saveTbl_lxh_acc(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;
                        }
                        rList.add(temp);
                    }
                } else if (temp.dataVersion > 0) {
                    boolean isSave = dataTransfer.updateTbl_lxh_acc(temp);
                    if (isSave) {
                        temp.dataVersion = t_version + 1;
                    } else {
                        temp.dataVersion = t_version + 2;
                    }
                    rList.add(temp);
                }
            }
        }

        TransferObject sendMessage = new TransferObject();
        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        return sendMessage;
    }

    /**
     * <pre>
     * nodeId,历史数据处理
     * </pre>
     *
     * @param message
     * @return Map< String, TransferObject >
     * @deprecated 2013-04-16 单个处理替换该方法
     */
    @SuppressWarnings("unchecked")
    public Map<String, TransferObject> nodeProcess(IoSession session,
                                                   Object message) {
        log.info(" \n========== 开始对N个node历史数据进行处理==========\n ");

        Map<String, TransferObject> sendMap = (Map<String, TransferObject>) message; // 要返回的Map

        Map<String, TransferObject> acceptMap = (Map<String, TransferObject>) message; // 接收到的Map

        Iterator<TransferObject> itemMessage = acceptMap.values().iterator(); // 接收到的Item

        while (itemMessage.hasNext()) { // 循环接收到的Map-acceptMap进行处理，并加入返回Map中-sendMap

            TransferObject acceptMessage = itemMessage.next(); // 接收的单个TransferObject-对应一个Node表数据
            TransferObject sendMessage = new TransferObject(); // 返回的单个TransferObject-对应一个Node表数据

            List<NodeCls> rList = new ArrayList<NodeCls>(); // 定义需要返回给Client的数据

            List<NodeCls> list = (List<NodeCls>) acceptMessage.data; // 到达tcpServer的一个node表数据

            long version = acceptMessage.dataVersion;

            log.info(" \n----------到达tcpServer的(" + acceptMessage.tableName
                    + ")表：共 (" + list.size() + ")条  -----------\n ");

            Iterator<NodeCls> item = list.iterator(); // 接收到client的数据

            // 判断node表是否存在(调用functions)
            boolean existTbl = dataTransfer
                    .existNodeTable(acceptMessage.tableName);
            boolean isCreateTrue = false;

            if (!existTbl) { // nodeid表是否存在
                isCreateTrue = dataTransfer
                        .createNodeTable(acceptMessage.tableName);
                log.info(" \n++++++++ " + acceptMessage.tableName + "创建:"
                        + isCreateTrue + " ++++++++\n");
            }

            if (existTbl == true || isCreateTrue == true) {
                // ---------------nodeid表创建成功-------
                if (version == 0) { // 首次同步
                    while (item.hasNext()) {
                        NodeCls temp = item.next();

                        log.info("\n 需要新增的一个节点历史数据 ===" + temp.nodeid + " "
                                + temp.sensorPhysicalid + " " + temp.createtime
                                + " === \n");

                        boolean isDataExist = dataTransfer
                                .isExistNodedata(temp);

                        if (!isDataExist) {
                            boolean isSave = dataTransfer.saveNode(temp);
                            if (isSave) {
                                temp.dataVersion = version + 1;

                                log.info(" \n  ----save：[" + temp.nodeid
                                        + "," + temp.id + ","
                                        + +temp.sensorPhysicalid + ","
                                        + temp.sensorPhysicalvalue + ""
                                        + temp.state + "," + temp.lowvoltage
                                        + "," + temp.createtime + "," + ","
                                        + temp.dataVersion + "] --- \n ");

                                rList.add(temp); // 何明明2012年6月18日,同步成功才需要返回client
                            }
                        }
                    }
                } else { // 非首次同步
                    while (item.hasNext()) {
                        NodeCls temp = item.next();
                        if (temp.dataVersion == 0) { // 新记录

                            boolean isDataExist = dataTransfer
                                    .isExistNodedata(temp);
                            if (!isDataExist) {
                                boolean isSave = dataTransfer.saveNode(temp);
                                if (isSave) {
                                    temp.dataVersion = version + 1;

                                    log.info(" \n ----save：[" + temp.nodeid
                                            + "," + temp.id + ","
                                            + temp.sensorPhysicalid + ","
                                            + temp.sensorPhysicalvalue + ""
                                            + temp.state + ","
                                            + temp.lowvoltage + ","
                                            + temp.createtime + "," + ","
                                            + temp.dataVersion + "] --- \n ");

                                    rList.add(temp); // 何明明2012年6月18日,同步成功才需要返回client
                                }
                            }
                        } else if (temp.dataVersion > 0) { // 旧记录,设计方案：temp.dataVersion=-1时为删除标记
                            boolean isSave = dataTransfer.updateNode(temp);
                            if (isSave) {
                                temp.dataVersion = version + 1; // update过的旧记录将数据版本号置为跟表数据版本号一致

                                log.info(" \n  ----update：[" + temp.nodeid
                                        + "," + temp.id + ","
                                        + temp.sensorPhysicalid + ","
                                        + temp.sensorPhysicalvalue + ""
                                        + temp.state + "," + temp.lowvoltage
                                        + "," + temp.createtime + "," + ","
                                        + temp.dataVersion + "] --- \n ");

                                rList.add(temp); // 何明明2012年6月18日,同步成功才需要返回client
                            }
                        }
                    }
                }
                // ---------------nodeid表创建成功-------
            }
            sendMessage.data = rList; // 加入返回对象
            sendMessage.dataVersion = version + 1;
            sendMessage.step = acceptMessage.step + 1;
            sendMessage.tableName = acceptMessage.tableName;
            sendMap.put(sendMessage.tableName, sendMessage);
            log.info(" \n==========" + acceptMessage.tableName
                    + "同步成功  ======\n ");
        }
        return sendMap;
    }

    /**
     * 处理设备链路
     *
     * @param acceptMessage socket 返回的信息
     * @return
     */
    public TransferObject deviceLinkProcess(TransferObject acceptMessage) {
        List<DeviceLink> deviceLinks = (List<DeviceLink>) acceptMessage.data;
        long t_version = acceptMessage.dataVersion;
        log.info("\n---- 到达tcpServer的(" + acceptMessage.tableName + ")表:共(" + deviceLinks.size() + ")条\n");
        // 判断node表是否存在(调用functions)
        boolean existTbl = dataTransfer.existNodeTable(acceptMessage.tableName);
        boolean isCreateTrue = false;
        // nodeId 表是否存在 ，不存在创建表
        if (!existTbl) {
            isCreateTrue = dataTransfer.createDeviceLink(acceptMessage.tableName);
            log.debug(" \n " + acceptMessage.tableName + "创建:" + isCreateTrue + "  \n");
        }
        //表存在或者创建成功
        if (existTbl || isCreateTrue) {
            for (DeviceLink deviceLink : deviceLinks) {
                boolean isExist = dataTransfer.isExistDeviceLink(deviceLink);
                log.debug("\n 需要新增的一个节点历史数据 ===" + deviceLink.nodeId + "," + deviceLink.stamp + "===\n");
                if (!isExist && dataTransfer.savaDeviceLink(deviceLink)) {
                    deviceLink.dataVersion += 1;
                    log.debug(" \n 设备链路表:" + deviceLink.nodeId + "保存成功\n");
                }
            }
        }
        acceptMessage.data = deviceLinks;
        acceptMessage.dataVersion = t_version + 1;
        acceptMessage.step = acceptMessage.step + 1;
        log.info(" \n==========" + acceptMessage.tableName + "同步成功  ======\n ");
        return acceptMessage;
    }

    /**
     * nodeId,单个历史数据处理
     *
     * @param message
     * @return Map< String, TransferObject >
     */
    @SuppressWarnings("unchecked")
    public TransferObject singleNodeProcess(Object message) {

        TransferObject sendMessage = (TransferObject) message; // 要返回的TransferObject
        TransferObject acceptMessage = (TransferObject) message; // 接收到的TransferObject

        List<NodeCls> rList = new ArrayList<NodeCls>(); // 定义需要返回给Client的数据
        List<NodeCls> list = (List<NodeCls>) acceptMessage.data; // 到达tcpServer的一个node表数据

        long t_version = acceptMessage.dataVersion;

        log.info(" \n---- 到达tcpServer的(" + acceptMessage.tableName
                + ")表：共 (" + list.size() + ")条  ----- \n ");

        Iterator<NodeCls> item = list.iterator(); // 接收到client的数据

        // 判断node表是否存在(调用functions)
        boolean existTbl = dataTransfer.existNodeTable(acceptMessage.tableName);
        boolean isCreateTrue = false;
        if (!existTbl) { // nodeid表是否存在
            isCreateTrue = dataTransfer
                    .createNodeTable(acceptMessage.tableName);
            log.debug(" \n " + acceptMessage.tableName + "创建:" + isCreateTrue
                    + "  \n");
        }

        if (existTbl || isCreateTrue) {
            if (t_version == 0) { // 首次同步
                while (item.hasNext()) {
                    NodeCls temp = item.next();

                    log.debug("\n 需要新增的一个节点历史数据 ===" + temp.nodeid + " "
                            + temp.createtime + " === \n");

                    boolean isDataExist = dataTransfer.isExistNodedata(temp);
                    if (!isDataExist) {
                        boolean isSave = dataTransfer.saveNode(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;

                            log.debug(" \n  ----save：[" + temp.nodeid + ","
                                    + temp.id + "," + +temp.sensorPhysicalid
                                    + "," + temp.sensorPhysicalvalue + ""
                                    + temp.state + "," + temp.lowvoltage + ","
                                    + temp.createtime + "," + ","
                                    + temp.dataVersion + "] --- \n ");
                        }
                        rList.add(temp); // 无论是否成功均返回
                    }
                }
            } else { // 非首次同步
                while (item.hasNext()) {
                    NodeCls temp = item.next();
                    if (temp.dataVersion == 0) { // 新记录
                        boolean isDataExist = dataTransfer
                                .isExistNodedata(temp);
                        if (!isDataExist) {
                            boolean isSave = dataTransfer.saveNode(temp);
                            if (isSave) {
                                temp.dataVersion = t_version + 1;

                                log.debug(" \n ----save：[" + temp.nodeid
                                        + "," + temp.id + ","
                                        + +temp.sensorPhysicalid + ","
                                        + temp.sensorPhysicalvalue + ""
                                        + temp.state + "," + temp.lowvoltage
                                        + "," + temp.createtime + "," + ","
                                        + temp.dataVersion + "] --- \n ");
                            }
                            rList.add(temp);
                        }
                    } else if (temp.dataVersion > 0) {
                        boolean isSave = dataTransfer.updateNode(temp);
                        if (isSave) {
                            temp.dataVersion = t_version + 1;

                            log.debug(" \n  ----update：[" + temp.nodeid + ","
                                    + temp.id + "," + temp.sensorPhysicalid
                                    + "," + temp.sensorPhysicalvalue + ""
                                    + temp.state + "," + temp.lowvoltage + ","
                                    + temp.createtime + "," + ","
                                    + temp.dataVersion + "] --- \n ");
                        } else {
                            temp.dataVersion = t_version + 2;
                        }
                        rList.add(temp);
                    }
                }
            }
        }

        sendMessage.data = rList;
        sendMessage.dataVersion = t_version + 1;
        sendMessage.step = acceptMessage.step + 1;
        sendMessage.tableName = acceptMessage.tableName;
        log.info(" \n==========" + acceptMessage.tableName
                + "同步成功  ======\n ");

        return sendMessage;
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
            if (Tables.nodeinfomemory.equals(acceptMessage.tableName)) {
                count++;
                session.write(nodeInfoAndNodeInfoMemoryProcess(acceptMessage));
            } else if (Tables.nodesensor.equals(acceptMessage.tableName)) {
                count++;
                session.write(nodesensorProcess(acceptMessage));
            } else if (Tables.avgdata.equals(acceptMessage.tableName)) {
                count++;
                session.write(avgdataProcess(acceptMessage));
            } else if (Tables.windrose.equals(acceptMessage.tableName)) {
                count++;
                session.write(windroseProcess(acceptMessage));
            } else if (Tables.tbl_rb_day_acc.equals(acceptMessage.tableName)) {
                count++;
                session.write(tbl_rb_day_accProcess(acceptMessage));
            } else if (Tables.tbl_rb_hour_acc.equals(acceptMessage.tableName)) {
                count++;
                session.write(tbl_rb_hour_accProcess(acceptMessage));
            } else if (Tables.tbl_lxh_acc.equals(acceptMessage.tableName)) {
                count++;
                session.write(tbl_lxh_accProcess(acceptMessage));
            } else if (Tables.logicGroup.equals(acceptMessage.tableName)) {
                count++;
                session.write(logicGroupProcess(acceptMessage));
            } else if (Tables.deviceLink.equals(acceptMessage.tableName.substring(0, 9))) {
                count++;
                session.write(deviceLinkProcess(acceptMessage));
            } else { // node历史数据
                count++;
                session.write(singleNodeProcess(message));
            }
        } else if (message instanceof String) {
            String msg = (String) message;
            if ("exit".equals(msg)) {
                log.info(" \n========== tcpServer,断开连接 ====== \n ");
                count = 1; // ##重置count逻辑计数器到初始状态##
                session.close(true);
            } else {
                log.info(" \n========== 非法内容，tcpServer断开连接 ====== \n ");
                count = 1; // ##重置count逻辑计数器到初始状态##
                session.close(true);
            }
        }
    }

    /**
     * 当接收到客户端的请求信息后触发此方法
     */
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        log.info("\n----第(" + count + ")次调用msgHandler() ---- \n ");
        msgHandler(session, message);
    }

    /**
     * <pre>
     * 当信息已经传送给客户端后触发此方法
     *
     * 发送消息成功时，调用这个方法，
     * 注意这里的措辞，发送成功之后，也就是说发送消息是不能用这个方法的, 此时可 session.close()
     * </pre>
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    /**
     * 当连接被关闭时触发，例如客户端程序意外退出等等
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        log.info(" \n-----客户端断开连接 ----  \n");
    }

    /**
     * 当接口中其他方法抛出异常未被捕获时触发此方法
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        super.exceptionCaught(session, cause);
        log.error(" \n-----异常：ServerHandler exceptionCaught，tcp异常\n ", cause);
    }

    /**
     * 连接空闲时触发
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        super.sessionIdle(session, status);
        log.info("\n----server连接空闲\n");
    }

    public DataTransferService getDataTransfer() {
        return dataTransfer;
    }

    public void setDataTransfer(DataTransferService dataTransfer) {
        this.dataTransfer = dataTransfer;
    }
}
