package com.microwise.msp.hardware.businessservice;


import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.Threshold;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.UploadSNToOnlineJobInitor;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.dao.ThresholdDao;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备业务处理基类
 *
 * @author heming
 * @since 2011-10-12
 */
public class DeviceService {

    private static Logger log = LoggerFactory.getLogger(DeviceService.class);
    private static Logger pLog = Packets.log;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    protected AnalysisDao dao;

    @Autowired
    protected DeviceDao deviceDao;

    @Autowired
    protected ThresholdDao thresholdDao;

    @Autowired
    private AppCache appCache;

    //    @Autowired
    private UploadSNToOnlineJobInitor uploadSNToOnlineJobInitor;

    /**
     * 存储历史数据开始时间
     */
    private Map<String, Date> startTimes = new HashMap<String, Date>();

    /**
     * 设备基类
     *
     * @deprecated 废弃 @gaohui 2013-08-01
     */
    protected DeviceBean deviceBean;

    /**
     * 监测点设备业务：初始化、检查重复数据、更新实时状态、数据计算、阀值计算
     * 中继、网关业务：初始化、检查重复数据、更新实时状态
     *
     * @param deviceBean
     */
    @Transactional
    public boolean businessProcess(DeviceBean deviceBean) {

        // 设备初始化
        if (!deviceExists(deviceBean.deviceid)) {
            deviceInit(deviceBean);
        }

        // 检查并设置低电状态
        checkVoltage(deviceBean);

        // 判断是否历史状态
        if (!isHistoryStatus(deviceBean)) {
            mergePackateData(deviceBean);
            // 更新实时状态
            modifyShareProperty(deviceBean);

            // 检查设备是否同步到云端
//            Integer uploadState = appCache.loadUploadState(deviceBean.deviceid);
//            if (uploadState == Defines.uploadState) {
//                uploadSNToOnlineJobInitor.initSendCommandTrigger(deviceBean);
//            }

            // 更新设备监测指标阈值
            // TODO 这里没有做报警业务
            if (deviceBean.thresholdList != null) {
                for (Threshold threshold : deviceBean.thresholdList) {
                    thresholdDao.addOrUpdateThreshold(threshold);
                }
            }
        }

        deviceDao.addDeviceStatusHistory(deviceBean);

        return true;
    }

    /**
     * 删除设备
     *
     * @param deviceId 设备编号
     * @return true：操作成功 false:操作失败
     */
    public boolean deleteDevice(String deviceId) {
        DeviceBean device = appCache.loadDevice(deviceId);
        // 如果当前设备为主模块，先清除从模块缓存
        if (device.deviceType == DeviceBean.TYPE_MASTER_MODULE) {
            List<DeviceBean> slaveModuleList = deviceDao.findSlaveModule(deviceId);
            for (DeviceBean slaveModule : slaveModuleList) {
                appCache.evictDevice(slaveModule.getDeviceid());
            }
        }
        appCache.evictDevice(deviceId);
        return true;
    }

    public void deviceBindingLocation(DeviceBean deviceBean) {
        if (deviceBean.isFirstPacket()) {
            startTimes.put(deviceBean.deviceid, deviceBean.timeStamp);
        }
        if (deviceBean.isLastPacket()) {
            Date startTime = startTimes.get(deviceBean.deviceid);
            deviceDao.addLocationBindRelationShip(deviceBean.locationId, deviceBean.deviceid, startTime, deviceBean.timeStamp);
        }
    }

    /**
     * 判断设备是否低电
     *
     * @param deviceBean 设备
     */
    protected void checkVoltage(DeviceBean deviceBean) {
        int voltage = Float.valueOf(deviceBean.voltage).intValue();
        switch (voltage) {
            case 0: // 市电
                deviceBean.anomaly = Defines.NORMALY;
                deviceBean.voltage = Defines.DefaultVolValue;
                break;
            case 1: // 低电
                deviceBean.anomaly = Defines.LowVoltageFlag;
                deviceBean.voltage = Defines.DefaultVolValue;
                break;
            case 2: // 掉电
                deviceBean.anomaly = Defines.NoVoltageFlag;
                deviceBean.voltage = Defines.DefaultVolValue;
                break;
            default:
                // 注意：20~128 时做除 10 处理，取小数点一位
                float vol = (float) voltage / 10;
                Float lowVolThreshold = appCache.loadDevice(deviceBean.deviceid).getVoltageThreshold();

                deviceBean.anomaly = Defines.NORMALY;
                // 判断节点的低电阈值是否存在
                if (lowVolThreshold != null) {
                    // 获取低电阈值
                    float lowVoltageThreshold = lowVolThreshold;
                    if (vol < lowVoltageThreshold) {
                        deviceBean.anomaly = Defines.LowVoltageFlag;
                    }
                } else {
                    // TODO 将 js 内联在 java 中 @gaohui 2013-11-19
                    // 检查电池是否低电
                    boolean isLowvoltage = ScriptService.getScriptService().isLowVoltage(deviceBean.deviceType, vol);
                    if (isLowvoltage) {
                        deviceBean.anomaly = Defines.LowVoltageFlag;
                    }
                }
                deviceBean.voltage = vol;
                break;
        }
    }

    /**
     * 检查设备是否已经存在
     *
     * @param deviceId 节点号
     * @return true：存在或已达上限 false:无并且未达上限
     */
    protected boolean deviceExists(String deviceId) {
        if (Strings.isNullOrEmpty(deviceId)) {
            return false;
        }

        return appCache.loadDevice(deviceId) != null;
    }

    /**
     * 是否是历时设备状态
     *
     * @param deviceBean
     * @return
     */
    protected boolean isHistoryStatus(DeviceBean deviceBean) {
        DeviceBean cacheDevice = appCache.loadDevice(deviceBean.deviceid);

        if (cacheDevice == null) {
            return false;
        }

        if (cacheDevice.timeStamp == null) {
            return false;
        }

        return deviceBean.timeStamp.before(cacheDevice.timeStamp);
    }

    /**
     * 处理状态包和数据包兼容性
     *
     * @param deviceBean
     */
    protected void mergePackateData(DeviceBean deviceBean) {
        DeviceBean memoryDevice = appCache.loadDevice(deviceBean.deviceid);

        // 状态合并
        if (deviceBean.packageType == 1) {
            deviceBean.setDemarcate(memoryDevice.getDemarcate());
            deviceBean.deviceMode = (memoryDevice.deviceMode == deviceBean.deviceMode ?
                    memoryDevice.deviceMode : deviceBean.deviceMode);
        } else if (deviceBean.packageType == 3) {
            deviceBean.parentid = memoryDevice.parentid;
            deviceBean.anomaly = memoryDevice.anomaly;
            deviceBean.voltage = memoryDevice.voltage;
        }
    }

    /**
     * 判断是否历史数据
     * <p/>
     * 修改历史数据判断依据. 以实时数据的时间来判断是否是历史数据. 如果比实时数据的时间晚，则是历史数据
     * 如果比较实时数据的时间早，则是实时数据 @gaohui 2013-08-09
     * <p/>
     * TODO 数据包历时与设备状态包历时区分 @gaohui
     *
     * @return
     * @author he.ming
     * @since May 10, 2013
     */
    protected boolean isHistory(DeviceBean deviceBean) {
        DeviceBean latestDeviceState = appCache.loadDevice(deviceBean.deviceid);
        if (latestDeviceState == null) {
            return false;
        }

        if (latestDeviceState.getDataTimestamp() == null) {
            return false;
        }

        return deviceBean.timeStamp.before(latestDeviceState.getDataTimestamp());
    }

    /**
     * 设备初始化(初始化nodeinfo,初始化nodeinfomemory), 并添加到 deviceCache 中
     */
    protected DeviceBean deviceInit(DeviceBean deviceBean) {
        // 初始化nodeinfo
        dao.initNodeInfo(deviceBean);
        deviceDao.createDeviceStatusTable(deviceBean.deviceid);

        // 初始化nodeinfomemory
        String nodeInfoMemoryId = dao.addMemoryInfo(deviceBean);

        // 单一设备属性信息,放入内存
        DeviceBean deviceMemory = new DeviceBean();
        deviceMemory.version = deviceBean.version;
        deviceMemory.parentid = deviceBean.parentid;
        deviceMemory.deviceid = deviceBean.deviceid; // 节点号
        deviceMemory.selfid = deviceBean.selfid;
        deviceMemory.deviceType = deviceBean.deviceType; // 设备类型
        deviceMemory.interval = deviceBean.interval; // 工作周期
        deviceMemory.timeStamp = deviceBean.timeStamp; // 时间戳
        deviceMemory.anomaly = deviceBean.anomaly;
        deviceMemory.voltage = deviceBean.voltage;
        deviceMemory.remoteAddress = deviceBean.remoteAddress;
        deviceMemory.remotePort = deviceBean.remotePort;
        deviceMemory.deviceMode = deviceBean.deviceMode;
        deviceMemory.sequence = -1; // 包序列号，系统初始化时包序列号为-1
        deviceMemory.setHumCompensate(1);   // 设备初始化时默认开启湿度补偿
        deviceMemory.setNodeSensors(new HashMap<Integer, NodeSensor>());
        deviceMemory.setLocationSensor(new HashMap<Integer, LocationSensor>());
        deviceMemory.setNodeInfoMemoryId(nodeInfoMemoryId);
        deviceMemory.setFaultCode(deviceBean.getFaultCode());

        // 将设备属性信息加入内存[key=deviceid,value=deviceBean]
        appCache.getDeviceCache().put(deviceBean.deviceid, deviceMemory);

        return deviceMemory;
    }

    /**
     * 更新公共属性信息
     */
    protected void modifyShareProperty(DeviceBean deviceBean) {
        DeviceBean memoryDevice = appCache.loadDevice(deviceBean.deviceid);
        deviceBean.setNodeInfoMemoryId(memoryDevice.getNodeInfoMemoryId());


        dao.updateMermoryInfo(deviceBean, 0);

        // 设备类型变化更新, v1.3 v30 协议兼容问题，设备类型兼容可能会改变 @gaohui 2014-05-07
        if (deviceBean.deviceType != memoryDevice.deviceType) {
            memoryDevice.deviceType = deviceBean.deviceType;
            deviceDao.updateDeviceType(deviceBean.deviceid, deviceBean.deviceType);
        }
        // 如果有产品序列号
        if (!Strings.isNullOrEmpty(deviceBean.getSn())) {
            deviceDao.updateDeviceSn(deviceBean.deviceid, deviceBean.getSn());
        }

//        memoryDevice.anomaly = Defines.NORMALY;
        memoryDevice.anomaly = deviceBean.anomaly;
        memoryDevice.timeStamp = deviceBean.timeStamp;
        memoryDevice.interval = deviceBean.interval;
        memoryDevice.voltage = deviceBean.voltage;
        memoryDevice.remoteAddress = deviceBean.remoteAddress;
        memoryDevice.remotePort = deviceBean.remotePort;
        memoryDevice.deviceMode = deviceBean.deviceMode;
        memoryDevice.version = deviceBean.version;
        memoryDevice.parentid = deviceBean.parentid;
        memoryDevice.setDemarcate(deviceBean.getDemarcate());
        memoryDevice.setSensitivity(deviceBean.getSensitivity());
        memoryDevice.setFaultCode(deviceBean.getFaultCode());
    }

    /**
     * 触发监听
     *
     * @param deviceBean
     */
    protected void fireListener(DeviceBean deviceBean) {
        Map<String, DataReceivedListener> listeners = appContext.getBeansOfType(DataReceivedListener.class);
        for (DataReceivedListener listener : listeners.values()) {
            try {
                listener.onDataReceived(deviceBean);
            } catch (Throwable e) {
                log.error("数据接收监听器异常", e);
            }
        }
    }

    public DeviceBean findById(String deviceId) {
        return deviceDao.findById(deviceId);
    }

    public List<DeviceBean> findAll() {
        return deviceDao.findAll();
    }

    public void setDao(AnalysisDao dao) {
        this.dao = dao;
    }
}
