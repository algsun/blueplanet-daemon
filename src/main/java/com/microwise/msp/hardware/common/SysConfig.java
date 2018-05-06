package com.microwise.msp.hardware.common;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import com.microwise.msp.hardware.businessbean.ConditionRefl;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.platform.bean.Siteinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * <pre>
 * 单例模式，系统级变量
 * 该对象只可以且只能实例化一次，主要用于实例化内存信息表 ，系统退出时销毁。
 *
 * 获取 字节队列：SysConfig.getInstance().getByte_LinkedList()
 * 获取 上行数据包0x01：SysConfig.getInstance().getDataArray()
 * 获取 数据应答 0x02: SysConfig.getInstance().getAckDataQueue()
 *
 * 获取 上行请求包0x05:Sysconfig.getIntance().getRequestArray()
 * 获取 消息：SysConfig.getInstance().getMessage_queue()
 *
 *
 * 队列 加入元素 .offer()
 * 队列 获取元素并移除 .poll()
 * </pre>
 *
 * @author heming
 * @check xu.baoji 2013-09-02 #5353
 * @since 2011-09-20
 */
public class SysConfig {
    public static final Logger log = LoggerFactory.getLogger(SysConfig.class);

    private static SysConfig instance = new SysConfig();

    // ============================================================================================
    /**
     * 上行数据包0x01--队列
     *
     * @deprecated 请使用 dataQueue
     */
    private Queue<List<Byte>> dataArray;

    /**
     * 上行数据包0x01--队列, 同上 dataArray 的代替品，将逐步废弃上面
     *
     * @deprecated 废弃 @gaohui 2013-08-17
     */
    private Queue<Packet> dataQueue;

    /**
     * 数据持久化队列
     */
    private Queue<DeviceBean> persistenceQueue;

    /**
     * 命令响应包0x0A--队列(下行命令包0x09的ACK), 代替 feedback_queue
     * TODO 废弃命令响应队列 @gaohui 2013-12-27
     */
    private Queue<Packet> feedbackQueue;

    /**
     * 条件反射参数 (deviceId + route) => conditionRefl
     */
    private ConcurrentMap<String, ConditionRefl> conditionReflCache = new ConcurrentHashMap<String, ConditionRefl>();

    /**
     * 缓存记录表
     */
    private ConcurrentMap<String, String> cacheTableMap = new ConcurrentHashMap<String, String>();

    // ===============================================================================

    /**
     * 远程ip
     *
     * @deprecated @gaohui 2014-03-10
     */
    public static String remoteIp;
    /**
     * 远程端口
     *
     * @deprecated @gaohui 2014-03-10
     */
    public static int remotePort;
    /**
     * 本地监听端口
     *
     * @deprecated @gaohui 2014-03-10
     */
    public static int lport;

    // ============================== ==================================
    /**
     * SD卡当前执行步骤
     *
     * @deprecated @gaohui 2014-03-10
     */
    public static int sdCardStep;

    // ==============================config.properties=========================
    /**
     * 是否缓存中心运行模式
     */
    private boolean isPacketCacheMode = false;

    /**
     * 是否开启网关sd卡回补
     */
    public static boolean isSdDatabackup = false;

    /**
     * 是否开启缓存中心回补
     */
    public static boolean isCacheDatabackup = false;

    /**
     * 是否开启设备状态(根据工作周期)检查
     */
    public static boolean isEmptyCheck;

    /**
     * 是否插入空数据
     */
    public static boolean isAddEmptyData = false;

    /**
     * 数据统计业务开关，0关闭，1开启
     */
    public static boolean countSwitch;

    /**
     * pm 统计业务
     */
    public static boolean countPmSensor;

    /**
     * 不检查设备工作周期的站点
     */
    private Set<String> sitesWithoutTimeOutCheck = new HashSet<String>();

    /**
     * 数据回补次数(sd/cache各自的次数)
     */
    public static int databackupNum = 65;

    /**
     * 发布WebService的本机IP
     *
     * @deprecated 无用 @gaohui 2013-08-19
     */
    public static String localWebServiceIP = "0.0.0.0";

    /**
     * 发布WebService的服务端口
     *
     * @deprecated 无用 @gaohui 2013-08-19
     */
    public static int localWebServicePort = 9000;

    // 数据同步相关配置
    /**
     * platform,tcp监听端口
     */
    public static final int tcpPort = 9912;
    /**
     * platform,tcp同步周期(间隔)
     */
    private int synchronizeInterval = 20;

    /**
     * 数据同步本地监听端口
     */
    private int synchronizePort = 9912;
    // ==============================config.properties=========================

    /**
     * 2012年3月30日 何明明 当前运行站点的相关信息从mapping_area_site获取
     */
    private Siteinfo siteinfo;

    /**
     * 数据缓存访问路径
     */
    public static String dataCacheUrl;

    /**
     * 短信发送状态记录
     * Key： zoneId_sensorPhysicalId
     * Value：1-超过最大值；2-小于最小值；3-恢复正常
     */
    private Map<String, Integer> sendState = new HashMap<String, Integer>();

    /**
     * sd卡回补标识 state 0 未回补，1回补中 stamp 时间戳 判断是否超时
     */

    public static ConcurrentHashMap<String, Object> sd_state = new ConcurrentHashMap<String, Object>();

    /**
     * sd卡回补超时时间，单位ms
     */
    public static long sd_timeout = 60000;

    /**
     * sd 卡回补时间
     */
    public static Date sd_date;

    /**
     * 数据文件目录
     */
    public static String datafileDirectory;

    /**
     * pm 传感计算的时间
     */
    public static int countPmSensorTime;

    /**
     * galaxy云端url
     */
    public static String galaxyOnLineUrl;

    /**
     * 转发开关
     */
    public static int open;

    /**
     * 转发ip
     */
    public static String remoteHost;

    /**
     * 转发端口
     */
    public static int port;

    /**
     * qcm设备缓存key:设备id ，value 设备bean
     */
    public static Map<String, DeviceBean> qcmStates;

    public static boolean useObelisk;

    private SysConfig() {
        feedbackQueue = new ConcurrentLinkedQueue<Packet>();
        persistenceQueue = new ConcurrentLinkedQueue<DeviceBean>();
        dataQueue = new ConcurrentLinkedQueue<Packet>();
        initConfig();
        initConfigFromYaml();
        qcmStates = new HashMap<String, DeviceBean>();
    }

    /**
     * 加载系统配置信息
     */
    private void initConfig() {
        log.info("加载配置信息");

        InputStream in = null;
        try {
            in = Resources.asByteSource(Resources.getResource("config.properties")).openStream();
            Properties prop = new Properties();
            prop.load(in);
            // 缓存中心运行模式
            isPacketCacheMode = parseNumericBoolean(prop.getProperty("isPacketCacheMode"));
            log.info(String.format("%-8s  [%s]", "缓存中心模式", isPacketCacheMode));
            // sd卡回补
            SysConfig.isSdDatabackup = parseNumericBoolean(prop.getProperty("isSdDatabackup"));
            log.info(String.format("%-8s  [%s]", "SD卡回补", SysConfig.isSdDatabackup));
            // 缓存中心回补
            SysConfig.isCacheDatabackup = parseNumericBoolean(prop.getProperty("isCacheDatabackup"));
            log.info(String.format("%-8s  [%s]", "缓存中心回补", SysConfig.isCacheDatabackup));
            // 设备状态检查
            SysConfig.isEmptyCheck = parseNumericBoolean(prop.getProperty("isEmptyCheck"));
            log.info(String.format("%-8s  [%s]", "设备状态检查", SysConfig.isEmptyCheck));
            // 是否插入空数据
            SysConfig.isAddEmptyData = parseNumericBoolean(prop.getProperty("isAddEmptyData"));
            log.info(String.format("%-8s  [%s]", "是否插入空数据", SysConfig.isAddEmptyData));
            // 数据统计
            SysConfig.countSwitch = parseNumericBoolean(prop.getProperty("countSwitch"));
            log.info(String.format("%-8s  [%s]", "数据统计", SysConfig.countSwitch));

            countPmSensor = parseNumericBoolean(prop.getProperty("countPmSensor"));
            log.info(String.format("%-8s  [%s]", "pm数据统计", countPmSensor));

            // platform,tcp同步周期(间隔)
            synchronizeInterval = Integer.valueOf(prop.getProperty("synchronize.interval").trim());
            synchronizePort = Integer.valueOf(prop.getProperty("synchronize.localPort"));

            // 获取缓存中心数据获取路径
            SysConfig.dataCacheUrl = prop.getProperty("dataCacheUrl").trim();
            // 获取SD卡回补的超时时间
            SysConfig.sd_timeout = Long.valueOf(prop.getProperty("sd_timeout").trim());
            // 获取数据文件目录
            datafileDirectory = prop.getProperty("blueplanet.locationDataFiles.dir").trim();

            countPmSensorTime = Integer.parseInt(prop.getProperty("countPmSensorTime").trim());

            galaxyOnLineUrl = prop.getProperty("galaxy_online.api.url").trim();

            open = Integer.parseInt(prop.getProperty("forwarding.open").trim());

            remoteHost = prop.getProperty("remoteHost").trim();

            port = Integer.parseInt(prop.getProperty("port").trim());

        } catch (Exception e) {
            SysConfig.isCacheDatabackup = false;
            SysConfig.isSdDatabackup = false;
            isPacketCacheMode = false;
            SysConfig.isEmptyCheck = true;
            SysConfig.countSwitch = true;
            log.error("Load config.properties Error... ", e);
        } finally {
            try {
                Closeables.close(in, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从 yaml 配置文件加载配置
     */
    @VisibleForTesting
    private void initConfigFromYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        try {
            inputStream = Resources.newInputStreamSupplier(Resources.getResource("config.yml")).getInput();
            Map<String, Object> configYaml = (Map<String, Object>) yaml.load(inputStream);
            List<String> sites = (List<String>) configYaml.get("sitesWithoutTimeoutCheck");
            sitesWithoutTimeOutCheck = new HashSet<String>(sites);
        } catch (IOException e) {
            log.error("init config from yaml", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("init config from yaml", e);
                }
            }
        }
    }

    public static SysConfig getInstance() {
        return instance;
    }

    /**
     * 将数字转换为 boolean
     *
     * @param number
     * @return
     */
    private static boolean parseNumericBoolean(String number) {
        if (Strings.isNullOrEmpty(number)) {
            return false;
        }

        return number.trim().equals("1");
    }

    public ConcurrentMap<String, ConditionRefl> getConditionReflCache() {
        return conditionReflCache;
    }

    public ConcurrentMap<String, String> getCacheTableMap() {
        return cacheTableMap;
    }

    public boolean isPacketCacheMode() {
        return isPacketCacheMode;
    }

    public int getSynchronizeInterval() {
        return synchronizeInterval;
    }

    public int getSynchronizePort() {
        return synchronizePort;
    }

    public boolean isSdDataBackup() {
        return isSdDatabackup;
    }

    public Siteinfo getSiteinfo() {
        return siteinfo;
    }

    public void setSiteinfo(Siteinfo siteinfo) {
        this.siteinfo = siteinfo;
    }

    public Map<String, Integer> getSendState() {
        return sendState;
    }

    public Set<String> getSitesWithoutTimeOutCheck() {
        return sitesWithoutTimeOutCheck;
    }

    /**
     * 上行数据包队列0x01
     *
     * @return
     * @author he.ming
     * @since Feb 18, 2013
     * @deprecated 请使用 dataQueue
     */
    public Queue<List<Byte>> getDataArray() {
        return dataArray;
    }

    /**
     * @return
     * @deprecated 废弃 @gaohui 2013-08-17
     */
    public Queue<Packet> getDataQueue() {
        return dataQueue;
    }

    /**
     * 命令响应包队列0x0A
     *
     * @return TODO 废弃命令响应队列 @gaohui 2013-12-27
     */
    public Queue<Packet> getFeedbackQueue() {
        return feedbackQueue;
    }

    /**
     * 持久化队列
     *
     * @return
     * @author he.ming
     * @since Feb 18, 2013
     */
    public Queue<DeviceBean> getPersistenceQueue() {
        return persistenceQueue;
    }

}
