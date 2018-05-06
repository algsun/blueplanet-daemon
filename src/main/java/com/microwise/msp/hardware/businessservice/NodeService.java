package com.microwise.msp.hardware.businessservice;

import com.google.common.base.Strings;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.LocationSensorDao;
import com.microwise.msp.hardware.dao.SupplymentDataDao;
import com.microwise.msp.hardware.dao.SyncDataDao;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.Maths;
import com.microwise.msp.util.StringUtil;
import com.microwise.msp.util.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * <pre>
 * 节点 业务处理
 * </pre>
 *
 * @author heming
 * @since 2011-10-12
 */
public class NodeService extends DeviceService {

    private static Logger log = LoggerFactory.getLogger(NodeService.class);
    private static Logger pLog = Packets.log;

    /**
     * 传感业务处理
     */
    private SensorService sensorService;

    /**
     * 数据回补dao操作
     */
    private SupplymentDataDao supplymentDataDao;

    @Autowired
    private DataPersistenceService dataPersistenceService;

    @Autowired
    private SensorHelper sensorHelper;

    @Autowired
    private AppCache appCache;

    @Autowired
    private LocationSensorDao locationSensorDao;

    @Autowired
    private SyncDataDao syncDataDao;

    /**
     * 业务处理
     *
     * @param deviceBean
     * @author gaohui
     * @date 2013-09-04
     */
    public void process(DeviceBean deviceBean) {
        // 缓存中心模式
        if (SysConfig.getInstance().isPacketCacheMode()) {
            SysConfig.getInstance().getPersistenceQueue().offer(deviceBean);
        } else {

            boolean isHistory = isHistory(deviceBean);
            if (businessProcess(deviceBean)) {
                if (!isHistory) {
                    fireListener(deviceBean);
                }

                // 历史数据入库
                if (paginateRepeatPack(deviceBean)) {
                    dataPersistenceService.persistence(deviceBean);
                }

                // 如果是历史数据并且Galaxy启用 Obelisk 数据同步,则手动将历史数据插入到 o_sync_data 表中
                if (isHistory && SysConfig.useObelisk && !Strings.isNullOrEmpty(deviceBean.locationId)) {
                    syncDataDao.convertHistoryDataToSyncData(deviceBean.locationId, deviceBean.timeStamp);
                }
            }
        }
    }

    /**
     * 分包重复数据处理
     *
     * @param deviceBean
     * @return
     */
    private boolean paginateRepeatPack(DeviceBean deviceBean) {
        boolean insertFlag = true;

        //分包标识大于或等于两包数据时，做处理。
        if (deviceBean.paginateCount >= 2) {

            //缓存deviceBean
            DeviceBean cacheDeviceBean = appCache.loadDevice(deviceBean.deviceid);
            DeviceSequenceStampBean deviceSequenceStampBean = new DeviceSequenceStampBean();
            deviceSequenceStampBean.sequence = deviceBean.sequence;
            deviceSequenceStampBean.stamp = deviceBean.timeStamp;

            //重复包队列
            EvictingQueue<DeviceSequenceStampBean> deviceSequenceStampBeans = cacheDeviceBean.deviceSequenceStampBeans;
            if (deviceSequenceStampBeans.size() == 0) {
                deviceSequenceStampBeans.add(deviceSequenceStampBean);
            } else {
                //如果当前数据包和缓存数据包的序列号和时间戳一样即为重复包
                for (Iterator<DeviceSequenceStampBean> it = deviceSequenceStampBeans.iterator(); it.hasNext(); ) {
                    DeviceSequenceStampBean deviceSequenceStampBean1 = it.next();
                    if (deviceSequenceStampBean1.sequence == deviceBean.sequence
                            && deviceSequenceStampBean1.stamp.getTime() == deviceBean.timeStamp.getTime()) {
                        insertFlag = false;
                        break;
                    }
                }
                if (insertFlag) {
                    deviceSequenceStampBeans.add(deviceSequenceStampBean);
                }
            }
            //更新缓存
            cacheDeviceBean.deviceSequenceStampBeans = deviceSequenceStampBeans;
            appCache.getDeviceCache().put(deviceBean.deviceid, cacheDeviceBean);
        }
        return insertFlag;
    }

    /**
     * 根据 deviceBean 处理业务
     *
     * @param deviceBean
     * @author gaohui
     * @date 2013-08-17
     */
    @Transactional
    public boolean businessProcess(DeviceBean deviceBean) {
        DeviceBean device = appCache.loadDevice(deviceBean.deviceid);
        if (device != null && Strings.isNullOrEmpty(deviceBean.locationId)) {
            deviceBean.locationId = device.locationId;
        }

        // 进行涉及传感的相关业务
        checkSensors(deviceBean);

        // TODO 重构，调用 super.businessProcess @li.jianfei 2014-06-16

        // 设备初始化
        if (!deviceExists(deviceBean.deviceid)) {
            deviceInit(deviceBean);
        }

        // 检查并设置低电状态
        checkVoltage(deviceBean);

        // 判断是否历史数据
        boolean isHistoryData = isHistory(deviceBean);
        // 判断是否是历时状态, 更新实时状态
        if (!isHistoryStatus(deviceBean)) {
            mergePackateData(deviceBean);
            modifyShareProperty(deviceBean);
        }
        deviceDao.addDeviceStatusHistory(deviceBean);

        // 更新设备监测指标阈值
        // TODO 这里没有做报警业务
        if (deviceBean.thresholdList != null) {
            for (Threshold threshold : deviceBean.thresholdList) {
                thresholdDao.addOrUpdateThreshold(threshold);
            }
        }
        // 传感信息检查
        List<NodeSensor> sensor = hasNewSensor(deviceBean);
        if (sensor != null && !sensor.isEmpty()) {
            //设备监测指标
            initDeviceNewSensor(deviceBean, sensor);

            //位置点监测指标
            initLocationNewSensor(deviceBean, sensor);

        }
        // 传感值计算

        if (!mathSensorValue(deviceBean)) {
            return false;
        }
        if (!isHistoryData) {
            // 7.刷新实时数据
            refreshRealTimeData(deviceBean);

            //刷新位置点数据
            refreshRealTimeData2(deviceBean);
        } else {
            //如果是历史数据即回补数据，并且开启插入空数据的业务，删除插入的空数据。
            if (SysConfig.isAddEmptyData) {
                locationSensorDao.deleteInsertData(deviceBean.locationId, deviceBean.timeStamp);
            }
        }

        // 是否是上传的数据
        if (deviceBean.isUpload()) {
            deviceBindingLocation(deviceBean);
        }

        return true;
    }

    /**
     * <pre>
     * 设备初始化
     * 【创建nodeid表,nodeinfo,nodesensor】
     * </pre>
     *
     * @return
     * @author heming
     * @since 2011-10-09
     */
    @Override
    protected DeviceBean deviceInit(DeviceBean deviceBean) {
        // 初始化nodeinfo,初始化nodeinfomemory
        DeviceBean tempDeviceBean = super.deviceInit(deviceBean);

        // 初始化nodeSensor
        List<NodeSensor> nodesensorList = new ArrayList<NodeSensor>();
        for (SensorPhysicalBean sensor : deviceBean.sensorData.values()) {
            NodeSensor tempSensor = new NodeSensor();
            tempSensor.setId(StringUtil.uuid());
            tempSensor.setNodeid(deviceBean.deviceid);
            tempSensor.setSensorPhysicalid(sensor.sensorPhysical_id);
            tempSensor.setSensorPhysicalValue(Defines._Sensor_Value_Default);
            tempSensor.setState(Defines._Sensor_State_OK);
            tempSensor.setStamp(StringUtil.nowTimestamp());
            tempSensor.setDataVersion(Defines._dataVersion_Default);

            tempDeviceBean.getNodeSensors().put(tempSensor.getSensorPhysicalid(), tempSensor);
            nodesensorList.add(tempSensor);
        }

        if (!nodesensorList.isEmpty()) {
            dao.initNodeSensor(nodesensorList);
        }

        return tempDeviceBean;
    }

    /**
     * 清除未知的监测指标
     *
     * @param deviceBean
     */
    private void checkSensors(DeviceBean deviceBean) {
        for (SensorPhysicalBean deviceSensor : Lists.newArrayList(deviceBean.sensorData.values())) {
            Sensor sensor = appCache.loadSensor(deviceSensor.sensorPhysical_id);
            if (sensor == null) {
                // 删除此监测指标 @gaohui 2013-08-23
                deviceBean.sensorData.remove(deviceSensor.sensorPhysical_id);
                pLog.error("XXX 未知监测指标-{}-{}: {}, [{}:{}]",
                        deviceBean.deviceType,
                        deviceBean.selfid,
                        deviceSensor.sensorPhysical_id,
                        deviceBean.remoteAddress,
                        deviceBean.remotePort);
                return;
            }
            //处理有符号数据
            double srcValue = sensorHelper.handleSignedData(deviceSensor.sensorPhysical_id, deviceSensor.sensor_Value);
            deviceBean.sensorData.get(deviceSensor.sensorPhysical_id).sensor_Value = srcValue;
            if (sensor.getEscapeSensorId() > 0) {
                //如果有转义的传感，替换成转义的传感
                deviceBean.sensorData.remove(deviceSensor.sensorPhysical_id);
                deviceSensor.setSensorPhysical_id(sensor.getEscapeSensorId());
                deviceBean.sensorData.put(sensor.getEscapeSensorId(), deviceSensor);
            } else if (sensor.getPhysicalId() == 2052 || sensor.getPhysicalId() == 2053
                    || sensor.getPhysicalId() == 2054 || sensor.getPhysicalId() == 2055) {
                //判断是否pm系列并且设置标识
                deviceBean.setContainPmSensor(true);
            }
        }
    }

    /**
     * 判断是否有新的传感,return新的传感List
     *
     * @return 新的传感List[NodeSensor]
     * @since 2011-11-01
     */
    private List<NodeSensor> hasNewSensor(DeviceBean deviceBean) {
        // 从缓存中查找设备拥有的监测指标 @gaohui 2013-08-07
        List<NodeSensor> beforeSensor = appCache.loadDeviceSensors(deviceBean.deviceid);
        // 获取当前传感信息
        List<NodeSensor> currentSensor = getCurrentSensorProperty(deviceBean);
        if (beforeSensor != null && !beforeSensor.isEmpty() && currentSensor != null && !currentSensor.isEmpty()) {
            // 如果数据库中包含内存中的传感,则把内存中的传感移除,剩下的就是新传感()
            for (NodeSensor before : beforeSensor) { // 循环数据库中的传感量
                for (Iterator<NodeSensor> it = currentSensor.iterator(); it.hasNext(); ) { // 循环内存中的传感量
                    NodeSensor current = it.next();
                    if (before.getSensorPhysicalid() == current.getSensorPhysicalid()) {
                        it.remove(); // 移除
                    }
                }
            }
        }

        // 如果有新的监测指标，则先清空当前缓存
        if (!currentSensor.isEmpty()) {
            appCache.evictDeviceSensors(deviceBean.deviceid);
        }

        return currentSensor;
    }

    /**
     * 添加新传感，nodesensor
     *
     * @return
     * @author heming
     * @since 2011-09-22
     */
    private void initDeviceNewSensor(DeviceBean device, List<NodeSensor> sensorList) {
        String deviceId = device.deviceid;
        DeviceBean deviceCache = appCache.loadDevice(deviceId);
        List<NodeSensor> nodeSensorList = new ArrayList<NodeSensor>();
        Timestamp currentTimestamp = device.timeStamp;
        for (NodeSensor sensor : sensorList) {
            NodeSensor tempSensor = new NodeSensor();
            tempSensor.setId(StringUtil.uuid());
            tempSensor.setNodeid(deviceId);
            tempSensor.setSensorPhysicalid(sensor.getSensorPhysicalid());
            tempSensor.setSensorPhysicalValue(Defines._Sensor_Value_Default);
            tempSensor.setState(Defines._Sensor_State_OK);
            tempSensor.setStamp(currentTimestamp);
            tempSensor.setDataVersion(Defines._dataVersion_Default);

            nodeSensorList.add(tempSensor);
            deviceCache.getNodeSensors().put(sensor.getSensorPhysicalid(), tempSensor);
        }
        dao.initNodeSensor(nodeSensorList);
    }

    /**
     * 添加新传感，locationSensor
     *
     * @return
     * @author liuzhu
     * @since 2015-6-5
     */
    private void initLocationNewSensor(DeviceBean device, List<NodeSensor> sensorList) {
        if (Strings.isNullOrEmpty(device.locationId)) {
            return;
        }
        String deviceId = device.deviceid;
        DeviceBean deviceCache = appCache.loadDevice(deviceId);
        List<LocationSensor> locationSensorList = new ArrayList<LocationSensor>();
        Timestamp currentTimestamp = device.timeStamp;
        for (NodeSensor sensor : sensorList) {
            LocationSensor tempSensor = new LocationSensor();
            tempSensor.setId(StringUtil.uuid());
            tempSensor.setLocationId(deviceCache.locationId);
            tempSensor.setSensorPhysicalid(sensor.getSensorPhysicalid());
            tempSensor.setSensorPhysicalValue(Defines._Sensor_Value_Default);
            tempSensor.setState(Defines._Sensor_State_OK);
            tempSensor.setStamp(currentTimestamp);
            tempSensor.setDataVersion(Defines._dataVersion_Default);
            locationSensorList.add(tempSensor);
            deviceCache.getLocationSensor().put(sensor.getSensorPhysicalid(), tempSensor);
        }
        dao.initLocationSensor(locationSensorList);
    }

    /**
     * 传感计算
     *
     * @return boolean
     * @author heming
     * @since 2011-09-22
     */
    private boolean mathSensorValue(DeviceBean deviceBean) {

        Map<Integer, CustomFormula> customFormulas = appCache.loadCustomFormula(deviceBean.deviceid);

        List<FloatSensor> floatSensors = appCache.loadFloatSensor(deviceBean.deviceid);

        for (SensorPhysicalBean sensorBean : deviceBean.sensorData.values()) {
            if (sensorBean.sensor_State == Defines._Sensor_State_Err) { // 传感状态为0，则跳出此次循环
                continue;
            }
            // TODO sensor 无用 @gaohui 2014-05-07
            Sensor sensor = appCache.loadSensor(sensorBean.getSensorPhysical_id());
            // 监测指标自定义公式系数
            CustomFormula customFormula = customFormulas.get(sensorBean.getSensorPhysical_id());

            sensorHelper.doMathSensor(sensorBean, customFormula, floatSensors);
        }

        // 计算特殊传感
        mathSpecialSensor(deviceBean);

        //计算qcm差值
        mathQCMSensor(deviceBean);

        // 计算结果过滤(精度、阈值)
        resultFilter(deviceBean);

        return true;
    }

    /**
     * 刷新实时数据
     *
     * @param deviceBean
     * @author he.ming
     * @since Mar 12, 2013
     */
    //TODO 实时数据入库，需要处理位置点信息
    public void refreshRealTimeData(DeviceBean deviceBean) {
        String deviceId = deviceBean.deviceid;
        DeviceBean deviceCache = appCache.loadDevice(deviceId);

        // 如果是数据包，更新实时数据的时间戳
        if (!deviceBean.sensorData.isEmpty()) {
            deviceCache.setDataTimestamp(deviceBean.timeStamp);
        }

        // 批量获取各监测指标版本号
        Map<Integer, Integer> dataVersions = dao.getNodeSensorDataVersions(deviceId);

        for (SensorPhysicalBean sensorPhysicalBean : deviceBean.sensorData.values()) {

            int dataVersion = 0;
            if (dataVersions.containsKey(sensorPhysicalBean.sensorPhysical_id)) {
                dataVersion = dataVersions.get(sensorPhysicalBean.sensorPhysical_id);
            }

            int tblVersion = 0;
            if (dataVersion > 0) {
                tblVersion = dao.getDataVersion(Tables.nodesensor);
                tblVersion++;
            }
            NodeSensor nodeSensor = deviceCache.getNodeSensors().get(sensorPhysicalBean.getSensorPhysical_id());
            dao.updateSensorMemory(nodeSensor.getId(), deviceBean, sensorPhysicalBean, tblVersion);
        }
    }

    public void refreshRealTimeData2(DeviceBean deviceBean) {
        for (SensorPhysicalBean sensorPhysicalBean : deviceBean.sensorData.values()) {
            locationSensorDao.updateLocationSensorMemory(deviceBean, sensorPhysicalBean);
        }
    }

    /**
     * 计算特殊传感
     */
    private void mathSpecialSensor(DeviceBean deviceBean) {
        for (SensorPhysicalBean sensorBean : deviceBean.sensorData.values()) {
            if (sensorBean.sensor_State == Defines._Sensor_State_Err) {
                // 传感状态为0，则跳出此次循环
                continue;
            }
            DeviceBean device = appCache.loadDevice(deviceBean.deviceid);
            if (sensorBean.sensorPhysical_id == Defines._RH && device != null && device.isHumCompensate()) {
                //计算湿度补偿
                humCompensate(sensorBean, deviceBean, Double.parseDouble(sensorBean.valueStr));
            }

            // TODO 可能衍生的监测指标依赖的监测指标采样异常 @gaohui 2013-10-24
            // 对需要进行特殊处理的传感进行计算
            // sensorBean.sensor_Value =
            sensorService.defineSensorMath(deviceBean, sensorBean, deviceBean.sensorData);

        }
    }

    private void mathQCMSensor(DeviceBean deviceBean) {
        for (SensorPhysicalBean sensorBean : deviceBean.sensorData.values()) {
            if (sensorBean.sensor_State == Defines._Sensor_State_Err) {
                // 传感状态为0，则跳出此次循环
                continue;
            }
            // sensorBean.sensor_Value =
            sensorService.qcmSensorMath(deviceBean, sensorBean, deviceBean.sensorData);
        }
        DeviceBean cachedDeviceBean = appCache.loadDevice(deviceBean.deviceid);
        cachedDeviceBean.qcmState = deviceBean.qcmState;
    }

    /**
     * 湿度补偿
     *
     * @param sensorBean 湿度经过公式计算后的值
     * @param deviceBean 通过deviceBean获取实际温度数值
     * @param srcValue   湿度的原始数据
     */
    private void humCompensate(SensorPhysicalBean sensorBean, DeviceBean deviceBean, double srcValue) {
        for (SensorPhysicalBean sensor : deviceBean.sensorData.values()) {
            if (sensor.sensorPhysical_id == Defines._T) {
                log.debug("温度原始值:" + sensor.valueStr);
                log.debug("温度计算值:" + sensor.sensor_Value);
                log.debug("湿度原始值:" + srcValue);
                log.debug("湿度计算值:" + sensorBean.sensor_Value);
                sensorBean.sensor_Value = mathHumCompensate(sensor.sensor_Value, srcValue, sensorBean.sensor_Value);
                log.debug("湿度补偿值:" + sensorBean.sensor_Value);
            }
        }
    }

    /**
     * 湿度补偿
     * 算法
     * 补偿结果=(当前温度-25)*(0.01+0.00008*湿度原始值)+公式计算后的湿度
     *
     * @param tc       当前温度
     * @param srcValue 湿度原始值
     * @param humValue 公式计算后的湿度值
     * @return 补偿后的湿度
     */
    private double mathHumCompensate(double tc, double srcValue, double humValue) {
        double t1 = 0.01;
        double t2 = 0.00008;
        return (tc - 25) * (t1 + (t2 * srcValue)) + humValue;
    }

    /**
     * 计算结果过滤(精度、阈值)
     *
     * @param deviceBean
     */
    private void resultFilter(DeviceBean deviceBean) {
        for (SensorPhysicalBean sensorBean : deviceBean.sensorData.values()) {
            // 采样异常 且 错误不是超出范围. 因为超出范围也要把计算结果入库
            if (sensorBean.getSensor_State() == Defines._Sensor_State_Err
                    && sensorBean.getErrorType() != SensorPhysicalBean.ERROR_TYPE_OUT_RANGE) {
                continue;
            }

            Sensor sensor = appCache.loadSensor(sensorBean.getSensorPhysical_id());
            if (sensor == null) {
                continue;
            }

            double value = Maths.roundToDouble(sensorBean.getSensor_Value(), sensor.getPrecision());
            sensorBean.setSensor_Value(value);
            String valueStr = StringUtil.round(sensorBean.getSensor_Value(), sensor.getPrecision());
            sensorBean.setValueStr(valueStr);
        }
    }

    /**
     * 获取内存中当前传感信息, 返回新的监测指标集合
     *
     * @return
     * @author heming
     * @since 2011-09-22
     */
    private List<NodeSensor> getCurrentSensorProperty(DeviceBean deviceBean) {
        List<NodeSensor> sensorMemory = new ArrayList<NodeSensor>();
        for (SensorPhysicalBean sensorPhy : deviceBean.sensorData.values()) {
            NodeSensor nodeSensor = new NodeSensor();
            nodeSensor.setSensorPhysicalid(sensorPhy.sensorPhysical_id);
            nodeSensor.setStamp(deviceBean.timeStamp);
            sensorMemory.add(nodeSensor);
        }
        return sensorMemory;
    }

    /**
     * <pre>
     * 判断设备状态(正常/异常)
     * 添加节点空数据记录
     * </pre>
     *
     * @param devBean
     * @author he.ming
     * @since Feb 18, 2013
     */
    @Transactional
    public void emptyProcess(DeviceBean devBean) {
        String deviceid = devBean.deviceid;
        // 1、更新内存时间戳(时间戳=内存时间戳+设备工作周期)
        long ms = devBean.timeStamp.getTime() + devBean.interval * 1000;
        // devBean.timeStamp.setTime(ms);

        // 2、添加设备空数据
//		if (devBean.deviceType == Defines.DEVICE_NODE
//				|| devBean.deviceType == Defines.DEVICE_NODE_SLAVE) {
        EmptyDataBean emptyDataBean = new EmptyDataBean();
        emptyDataBean.setNodeid(deviceid);
        String tm = DateUtils.getDate(devBean.timeStamp, "yyyy-MM-dd HH:00:00");
        emptyDataBean.setStamp(Timestamp.valueOf(tm));
        emptyDataBean.setDataCacheSuccess(0);
        emptyDataBean.setGatewaySuccess(0);
        // 判断是否存在对应的空数据
        if (!supplymentDataDao.isExistEmptyData(emptyDataBean)) {
            supplymentDataDao.insertIntoEmptyInfo(emptyDataBean);
        }
//		}

    }

    /**
     * 更新设备工作状态为超时
     *
     * @param nodeInfoMemoryId
     * @param timeStamp
     */
    @Transactional
    public void updateDeviceTimeOut(String nodeInfoMemoryId, Date timeStamp) {
        // 更新nodeinfomemory,将anomaly字段改为工作异常
        long tblVersion = dao.getDataVersion(Tables.nodeinfomemory);
        if (tblVersion == 0) {
            dao.anomalySetting(nodeInfoMemoryId, timeStamp, Defines.ANORMALY);
        } else {
            dao.anomalySetting(nodeInfoMemoryId, timeStamp, Defines.ANORMALY, tblVersion + 1);
        }

    }

    public void setSensorService(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public void setSupplymentDataDao(SupplymentDataDao supplymentDataDao) {
        this.supplymentDataDao = supplymentDataDao;
    }

}
