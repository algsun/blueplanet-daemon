package com.microwise.msp.hardware.businessservice;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.EmptyDataBean;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.BaseDao;
import com.microwise.msp.hardware.dao.SupplymentDataDao;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.Tables;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * <pre>
 * 系统初始化业务
 * </pre>
 *
 * @author heming
 * @check xu.baoji 2013-09-02 #5353
 * @since 2011-10-11
 */
public class SystemInitService {

    private static Logger log = LoggerFactory.getLogger(SystemInitService.class);

    @Autowired
    private AppCache appCache;

    @Qualifier("DeviceService")
    @Autowired
    private DeviceService deviceService;

    private SupplymentDataDao supplymentDataDao;

    private BaseDao dao;

    /**
     * 加载设备信息
     *
     * @author heming
     * @since 2012年4月1日
     */
    @Transactional
    public void loadDevicesInfo() {
        // 查询所有设备
        List<DeviceBean> deviceList = deviceService.findAll();
        for (DeviceBean device : deviceList) {
            if (device == null) {
                continue;
            }

            String deviceId = device.deviceid;
            List<NodeSensor> nodeSensors = appCache.loadDeviceSensors(deviceId);
            String locationId = device.locationId;
            List<LocationSensor> locationSensors;
            if (!Strings.isEmpty(locationId)) {
                locationSensors = appCache.loadLocationSensors(device.locationId);
            } else {
                locationSensors = new ArrayList<LocationSensor>();
            }
            // 注意：返回 immutable map
            Map<Integer, NodeSensor> nodeSensorMap = Maps.uniqueIndex(nodeSensors, new Function<NodeSensor, Integer>() {
                @Override
                public Integer apply(NodeSensor nodeSensor) {
                    return nodeSensor.getSensorPhysicalid();
                }
            });

            Map<Integer, LocationSensor> LocationSensorMap = Maps.uniqueIndex(locationSensors, new Function<LocationSensor, Integer>() {
                @Override
                public Integer apply(LocationSensor locationSensor) {
                    return locationSensor.getSensorPhysicalid();
                }
            });
            // 设备监测指标
            device.setNodeSensors(new HashMap<Integer, NodeSensor>(nodeSensorMap));
            device.setLocationSensor(new HashMap<Integer, LocationSensor>(LocationSensorMap));
            // 设备信息加载至内存
            appCache.getDeviceCache().put(deviceId, device);
        }
    }

    /**
     * 初始化空数据, 仅 Macross 使用
     */
    public void initEmptyData() {
        Iterator<String> iter = appCache.getDeviceCache().asMap().keySet().iterator();
        // 获取当前时间
        long currentTime = DateUtils.getDate(DateUtils.getDate(new Date(), "yyyy-MM-dd HH:00:00")).getTime();
        // 循环遍历节点信息
        while (iter.hasNext()) {
            // 获取设备的节点号
            String deviceId = iter.next();
            DeviceBean deviceBean = appCache.loadDevice(deviceId);
            // 获取设备的数据时间戳
            if (deviceBean.timeStamp == null
                    && deviceBean.deviceType == Defines.DEVICE_NODE) {
                continue;
            }
            long memoryTime = DateUtils.getDate(
                    DateUtils.getDate(deviceBean.timeStamp,
                            "yyyy-MM-dd HH:00:00")).getTime();
            while (memoryTime < currentTime) {
                memoryTime += 3600000;
                // 插入空数据
                EmptyDataBean emptyDataBean = new EmptyDataBean();
                emptyDataBean.setNodeid(deviceId);
                emptyDataBean.setStamp(new Timestamp(memoryTime));
                emptyDataBean.setDataCacheSuccess(0);
                emptyDataBean.setGatewaySuccess(0);
                // 判断是否存在对应的空数据
                if (!supplymentDataDao.isExistEmptyData(emptyDataBean)) {
                    supplymentDataDao.insertIntoEmptyInfo(emptyDataBean);
                }
            }
        }
    }

    public void initObeliskStatus() {
        SysConfig.useObelisk = dao.isExistTable(Tables.syncTables);
    }

    public void setDao(BaseDao dao) {
        this.dao = dao;
    }

    public void setSupplymentDataDao(SupplymentDataDao supplymentDataDao) {
        this.supplymentDataDao = supplymentDataDao;
    }

}
