package com.microwise.msp.hardware.handler.listener;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.businessbean.rabbitmq.*;
import com.microwise.msp.hardware.businessservice.DataReceivedListener;
import com.microwise.msp.hardware.businessservice.UserService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.OfficeDao;
import com.microwise.msp.hardware.dao.SiteDao;
import com.microwise.msp.hardware.service.*;
import com.microwise.msp.hardware.vo.LocationSensor;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 阈值判断监听器. 处理阈值相关业务
 *
 * @author gaohui
 * @date 13-8-26 11:06
 * @check xu.baoji 2013-09-02 #5353
 */
@Component
@Scope("prototype")
public class ThresholdDataReceivedListener implements DataReceivedListener {

    // 高于阈值
    public static final int SEND_STATE_MORE_THEN_MAX = 1;
    // 低于阈值
    public static final int SEND_STATE_LESS_THEN_MIN = 2;
    // 正常
    public static final int SEND_STATE_NORMAL = 3;

    @Autowired
    private ThresholdService thresholdService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationSensorService locationSensorService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private SensorService sensorService;

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

    private static final Logger logger = LoggerFactory.getLogger(ThresholdDataReceivedListener.class);

    /**
     * 向mq服务器发送json数据包
     *
     * @param deviceBean
     */
    @Override
    public void onDataReceived(DeviceBean deviceBean) {
        if (deviceBean.locationId == null) return;
        if (systemFlag == 1) {
            version2(deviceBean);
        }
        sendDataToTattletale(deviceBean);
    }

    /**
     * 组装设备数据，并将数据发送到报警平台（Tattletale）
     *
     * @param deviceBean
     */
    private void sendDataToTattletale(DeviceBean deviceBean) {
        try {
            if (enable) {
                Message message = this.assemble(deviceBean);
                Gson gson = new Gson();
                String alarmJson = gson.toJson(message);
                logger.info(alarmJson);
                CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
                connectionFactory.setUsername(userName);
                connectionFactory.setPassword(password);
                connectionFactory.setVirtualHost(virtualHost);
                AmqpAdmin admin = new RabbitAdmin(connectionFactory);
                admin.declareQueue(new Queue("sensor-data"));
                AmqpTemplate template = new RabbitTemplate(connectionFactory);
                template.convertAndSend("sensor-data", alarmJson);
                connectionFactory.destroy();
            }
        } catch (Exception e) {
            logger.error("连接RabbitMQ服务器不成功", e);
        }
    }

    /**
     * 生成报警数据包
     *
     * @param deviceBean
     * @return
     */
    private Message assemble(DeviceBean deviceBean) {

        Message alarmBean = new SensorDataMessage();
        alarmBean.setSystemType(systemFlag);

        // 封装机构信息
        Unit unit = new Unit();
        unit.setId(deviceBean.getSiteId());
        // TODO 使用常量
        // 机构名称
        String name = "";
        // 银河
        if (systemFlag == 1) {
            name = siteDao.findBySiteId(deviceBean.getSiteId()).getName();
        } else if (systemFlag == 2) {
            name = officeDao.findOffice(deviceBean.getSiteId()).getOfficeName();
        }
        unit.setName(name);
        alarmBean.setUnit(unit);

        Location location = locationService.findLocation(deviceBean.getLocationId());


        Map<Integer, LocationSensor> sensorData = null;
        if (Strings.isNullOrEmpty(location.getZoneId())) {
            // 封装位置点信息
            List<LocationSensor> locationSensors = locationSensorService.findLocationSensor(location.getId());
            sensorData = Maps.newHashMap();
            for (LocationSensor locationSensor : locationSensors) {
                sensorData.put(locationSensor.getSensorPhysicalid(), locationSensor);
            }
            alarmBean.setLocation(location);
        } else {
            // 封装区域/文物及实时数据信息
            String zoneName;
            String zoneId;
            zoneId = location.getZoneId();
            zoneName = zoneService.findZoneNameById(systemFlag, location.getZoneId());
            Target target = new Target();
            target.setId(zoneId);
            target.setName(zoneName);
            List<Location> locations = locationService.findLocations(zoneId);
            for (Location tempLocation : locations) {
                List<LocationSensor> locationSensors = locationSensorService.findLocationSensor(tempLocation.getId());
                sensorData = Maps.newHashMap();
                for (LocationSensor locationSensor : locationSensors) {
                    sensorData.put(locationSensor.getSensorPhysicalid(), locationSensor);
                }
                tempLocation.setSensorData(sensorData);
            }
            target.setLocations(locations);

            alarmBean.setTarget(target);
            alarmBean.setTimeStamp(deviceBean.getTimeStamp());
        }
        return alarmBean;
    }


    private void version2(DeviceBean deviceBean) {
        String deviceId = deviceBean.deviceid;
        if (deviceBean.locationId == null) {
            return;
        }
        Location location = locationService.findLocation(deviceBean.locationId);
        int logicGroupId = siteDao.findLogicGroupBySiteId(location.getSiteId());

        List<Threshold> thresholds = thresholdService.findThresholds(deviceId);
        if (thresholds == null || thresholds.size() == 0) return;

        for (SensorPhysicalBean sensorBean : deviceBean.sensorData.values()) {

            // 判断传感状态是否正常
            if (sensorBean.getSensor_State() != Defines._Sensor_State_OK) continue;

            for (Threshold threshold : thresholds) {
                if (sensorBean.sensorPhysical_id != threshold.getSensorId()) continue;
                String sendStateKey = location.getId() + "_" + sensorBean.getSensorPhysical_id();
                int lastState = lastState(sendStateKey);
                int currentState = currentState(threshold, sensorBean.sensor_Value);

                // 状态是否变化
                // 状态未改变, 直接返回
                if (lastState == currentState) continue;

                Sensor sensor = appCache.loadSensor(sensorBean.sensorPhysical_id);
                String content = getContent(deviceBean, location, sensorBean, currentState, sensor);

                // 添加报警记录
                String alarm = genAlarmHistoryFactor(sensorBean, sensor, currentState);
                ThresholdAlarmHistory alarmHistory = new ThresholdAlarmHistory(location.getSiteId(),
                        deviceBean.locationId, threshold.getSensorId(), alarm, currentState);
                // 添加报警历史记录
                thresholdService.addAlarmHistory(alarmHistory);

                if (currentState != SEND_STATE_NORMAL) {

                    // 添加我的任务
                    String title = ("监测预警：" + location.getLocationName() + sensor.getCnName() +
                            (currentState == SEND_STATE_LESS_THEN_MIN ? "低于阈值" : "") +
                            (currentState == SEND_STATE_MORE_THEN_MAX ? "高于阈值" : "")
                    );
                    // 任务发起人
                    User user = userService.findSiteManager(logicGroupId);
                    Task task = new Task(logicGroupId, title, content, user.getId());

                    // 查询位置点关注人
                    List<User> followers = locationService.findFollowers(location.getId());
                    taskService.addTask(task, followers);
                }

                // 改变报警状态
                SysConfig.getInstance().getSendState().put(sendStateKey, currentState);
            }
        }
    }

    private String getContent(DeviceBean deviceBean, Location location, SensorPhysicalBean sensorBean, int currentState, Sensor sensor) {
        // 报警内容
        String content;
        if (currentState == SEND_STATE_NORMAL) {
            content = "【元智系统】" + location.getLocationName() + sensor.getCnName() + "值已恢复到正常范围，";
        } else {
            content = "【元智系统】" + location.getLocationName()
                    + sensor.getCnName()
                    + (currentState == SEND_STATE_MORE_THEN_MAX ? "高于阈值达到" : "")
                    + (currentState == SEND_STATE_LESS_THEN_MIN ? "低于阈值达到" : "")
                    + sensorBean.getSensor_Value()
                    + "(" + sensor.getUnit() + ")，";
        }
        content += "采样时间：" + new SimpleDateFormat("MM月dd日HH时mm分").format(deviceBean.timeStamp);
        if (location.getZone() != null) {
            content += "，所属区域：" + location.getZone().getZoneName();
        }
        return content;
    }

    /**
     * 返回默认状态
     *
     * @param sendStateKey
     * @return
     */
    private int lastState(String sendStateKey) {
        // 发送状态
        int state = SEND_STATE_NORMAL;
        Map<String, Integer> sendState = SysConfig.getInstance().getSendState();
        if (sendState.containsKey(sendStateKey)) {
            state = sendState.get(sendStateKey);
        }
        return state;
    }

    /**
     * 返回当前状态
     *
     * @param threshold   监测指标阈值
     * @param sensorValue 传感量值
     * @return
     */
    private int currentState(Threshold threshold, double sensorValue) {
        int state = SEND_STATE_NORMAL;

        int conditionType = threshold.getConditionType();
        float target = threshold.getTarget();
        float floating = threshold.getFloating();

        switch (conditionType) {
            case 1:
                float max = target + floating;
                float min = target - floating;
                if (!(min <= sensorValue && max >= sensorValue)) {
                    if (sensorValue > max) {
                        state = SEND_STATE_MORE_THEN_MAX;
                    } else if (sensorValue < min) {
                        state = SEND_STATE_LESS_THEN_MIN;
                    }
                }
                break;
            case 2:
                if (sensorValue <= target) state = SEND_STATE_LESS_THEN_MIN;
                break;
            case 3:
                if (sensorValue >= target) state = SEND_STATE_MORE_THEN_MAX;
                break;
            case 4:
                if (sensorValue < target) state = SEND_STATE_LESS_THEN_MIN;
                break;
            case 5:
                if (sensorValue > target) state = SEND_STATE_MORE_THEN_MAX;
                break;
            default:
                state = SEND_STATE_NORMAL;
                break;
        }

        // 木卫一相关业务
        if (conditionType == 0) {
            float minValue = threshold.getMinValue();
            float maxValue = threshold.getMaxValue();

            if (sensorValue < minValue) {
                state = SEND_STATE_LESS_THEN_MIN;
            }

            if (sensorValue > maxValue) {
                state = SEND_STATE_MORE_THEN_MAX;
            }

            if (minValue <= sensorValue && sensorValue <= maxValue) {
                state = SEND_STATE_NORMAL;
            }
        }

        return state;
    }


    /**
     * 生成报警历史因素
     *
     * @param sensorPhysicalBean 传感量包装类[标识、值、状态]
     * @param sensor             监测指标
     * @param state              采样状态
     * @return 报警历史因素
     */
    private static String genAlarmHistoryFactor(SensorPhysicalBean sensorPhysicalBean, Sensor sensor, int state) {
        StringBuilder sb = new StringBuilder();
        if (state != SEND_STATE_NORMAL) {
            sb.append(sensor.getCnName())
                    .append((state == SEND_STATE_MORE_THEN_MAX ? "高于阈值达到" : ""))
                    .append((state == SEND_STATE_LESS_THEN_MIN ? "低于阈值达到" : ""))
                    .append(sensorPhysicalBean.getSensor_Value())
                    .append("(").append(sensor.getUnit()).append(")");
        }
        return sb.toString();
    }

}
