package com.microwise.msp.hardware.threads;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.businessbean.rabbitmq.*;
import com.microwise.msp.hardware.businessservice.DataPersistenceService;
import com.microwise.msp.hardware.businessservice.NodeService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.OfficeDao;
import com.microwise.msp.hardware.dao.SiteDao;
import com.microwise.msp.hardware.service.LocationService;
import com.microwise.msp.hardware.service.ZoneService;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 节点数据超时检查线程 2012年4月24日 谢登
 *
 * @author he.ming
 * @since Feb 18, 2013
 */
@Component
@Scope("prototype")
public class TimeoutCheckingThread implements Runnable {

    private static Logger log = LoggerFactory.getLogger(TimeoutCheckingThread.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private LocationService locationService;

    // 是否关闭
    private AtomicBoolean closed = new AtomicBoolean(false);
    @Autowired
    private AppCache appCache;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private DataPersistenceService dataPersistenceService;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.virtual.host}")
    private String virtualHost;

    @Value("${systemFlag}")
    private int systemFlag;

    @Value("${rabbitmq.enable}")
    private Boolean enable;

    @Override
    public void run() {
        while (!isClosed()) {
            try {
                process();
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("\n\n EmptyThread:  \n\n", e);
            }
        }

        log.info("线程关闭");
    }

    /**
     * 检查节点数据是否超时
     */
    private void process() {
        // 循环遍历节点信息
        for (Map.Entry<String, DeviceBean> entry : appCache.getDeviceCache().asMap().entrySet()) {
            DeviceBean deviceBean = entry.getValue();

            if (isIgnored(deviceBean.deviceid)) {
                continue;
            }

            // 获取设备的数据时间戳
            long memoryTime;
            if (deviceBean.timeStamp == null) {
                memoryTime = System.currentTimeMillis();
            } else {
                memoryTime = deviceBean.timeStamp.getTime();
            }

            // 获取当前时间
            long currentTime = System.currentTimeMillis();

            // 获取时间间隔
            int interval = deviceBean.interval;

            if ((currentTime - memoryTime) > (interval * 1500)) {
                // 如果之前状态不是超时，则更新状态
                if (deviceBean.anomaly != Defines.ANORMALY) {
                    deviceBean.anomaly = Defines.ANORMALY;
                    if (deviceBean.getNodeInfoMemoryId() != null) {
                        nodeService.updateDeviceTimeOut(deviceBean.getNodeInfoMemoryId(), deviceBean.timeStamp);
                        // 如果启用RabbitMQ，将数据发送到rabbitmq服务器
                        if (enable) {
                            sendMessage(deviceBean);
                        }
                        if (SysConfig.isAddEmptyData) {
                            DeviceBean tempDeviceBean = new DeviceBean();
                            tempDeviceBean.deviceid = deviceBean.deviceid;
                            tempDeviceBean.locationId = deviceBean.locationId;
                            tempDeviceBean.timeStamp = new Timestamp(System.currentTimeMillis());
                            tempDeviceBean.anomaly = deviceBean.anomaly;
                            tempDeviceBean.voltage = deviceBean.voltage;

                            Map<Integer, SensorPhysicalBean> sensorPhysicalBeanMap = new HashMap<Integer, SensorPhysicalBean>();
                            for (Integer integer : deviceBean.getNodeSensors().keySet()) {
                                NodeSensor nodeSensor = deviceBean.getNodeSensors().get(integer);
                                SensorPhysicalBean sensorPhysicalBean = new SensorPhysicalBean();
                                sensorPhysicalBean.setValueStr("0");
                                //添加一条超时时间
                                sensorPhysicalBean.setSensor_State(5);
                                sensorPhysicalBean.setSensor_Value(0);
                                sensorPhysicalBean.sensorPhysical_id = nodeSensor.getSensorPhysicalid();
                                sensorPhysicalBeanMap.put(integer, sensorPhysicalBean);
                            }
                            tempDeviceBean.sensorData = sensorPhysicalBeanMap;

                            //设备超时加一条异常数据，设备实时数据、位置点实时数据、位置点历史数据
                            dataPersistenceService.persistence(tempDeviceBean);


                        }
                    }
                }
            }
        }
    }

    /**
     * 判断runnable是否关闭
     *
     * @return
     * @author he.ming
     * @since Jan 29, 2013
     */
    private boolean isClosed() {
        return closed.get();
    }

    /**
     * 关闭线程
     *
     * @author he.ming
     * @since Jan 29, 2013
     */
    public void shutdown() {
        closed.set(true);
    }

    private boolean isIgnored(String deviceId) {
        for (String siteId : SysConfig.getInstance().getSitesWithoutTimeOutCheck()) {
            if (deviceId.startsWith(siteId)) {
                return true;
            }
        }

        return false;
    }

    public void setAppCache(AppCache appCache) {
        this.appCache = appCache;
    }

    private void sendMessage(DeviceBean device) {
        if (device.locationId == null) return;
        Message message = new DeviceStateMessage();
        message.setSystemType(systemFlag);

        // 封装机构信息
        Unit unit = new Unit();
        unit.setId(device.getSiteId());
        // TODO 使用常量
        // 机构名称
        String name = "";
        // 银河
        if (systemFlag == 1) {
            name = siteDao.findBySiteId(device.getSiteId()).getName();
        } else if (systemFlag == 2) {
            name = officeDao.findOffice(device.getSiteId()).getOfficeName();
        }
        unit.setName(name);
        message.setUnit(unit);

        Location location = locationService.findLocation(device.getLocationId());
        location.setDevice(new Device(device.getDeviceid(), device.getDeviceType(), device.getAnomaly()));
        Map<Integer, LocationSensor> sensorData = null;
        if (Strings.isNullOrEmpty(location.getZoneId())) {
            message.setLocation(location);
        } else {
            // 封装区域/文物及实时数据信息
            String zoneName;
            String zoneId;
            zoneId = location.getZoneId();
            zoneName = zoneService.findZoneNameById(systemFlag, location.getZoneId());
            Target target = new Target();
            target.setId(zoneId);
            target.setName(zoneName);
            List<Location> locations = Lists.newArrayList();
            locations.add(location);
            target.setLocations(locations);
            message.setTarget(target);
        }
        message.setTimeStamp(device.getTimeStamp());
        Gson gson = new Gson();
        String alarmJson = gson.toJson(message);
        log.info(alarmJson);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue("device-state"));
        AmqpTemplate template = new RabbitTemplate(connectionFactory);
        template.convertAndSend("device-state", alarmJson);
        connectionFactory.destroy();
    }
}
