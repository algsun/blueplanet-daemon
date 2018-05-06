package com.microwise.msp.hardware.handler.codec.v30;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.DeviceProperties;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.StringUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * 数据包工具类
 *
 * @author gaohui
 * @date 13-8-18 11:22
 */
public class DataV30Packets {

    /**
     * 根据 v3.0 协议创建 DeviceBean(不包括监测指标)
     *
     * @param dataPacket
     * @return
     */
    public static DeviceBean fromPacket(DataV30Packet dataPacket) {
        DeviceBean deviceBean = new DeviceBean();

        deviceBean.remoteAddress = dataPacket.getRemoteHost();
        deviceBean.remotePort = dataPacket.getRemotePort();

        deviceBean.packageType = dataPacket.getPacketType();
        deviceBean.deviceType = dataPacket.getDeviceType();
        deviceBean.version = dataPacket.getVersion();
        deviceBean.setDummyV3Version(dataPacket.isDummyV3Version());
        deviceBean.size = dataPacket.getBodyLength();

        deviceBean.isControl = dataPacket.getControl();
        deviceBean.parentid = dataPacket.getParentId();
        deviceBean.selfid = dataPacket.getSelfId();
        deviceBean.sequence = dataPacket.getSequence();
        deviceBean.voltage = dataPacket.getVoltage();
        deviceBean.rssi = dataPacket.getRssi();
        deviceBean.lqi = dataPacket.getLqi();

        deviceBean.interval = dataPacket.getInterval();
        deviceBean.timeStamp = dataPacket.getTimestamp();
        deviceBean.siteId = dataPacket.getSiteId();
        deviceBean.sdCardState = dataPacket.getSdCardState();
        deviceBean.deviceMode = dataPacket.getWorkMode();
        deviceBean.paginateIndex = dataPacket.getPaginateIndex();
        deviceBean.paginateCount = dataPacket.getPaginateCount();
        deviceBean.setSensitivity(dataPacket.getSensitivity());
        // 分包暂时无用
        deviceBean.setFaultCode(StringUtil.integerList2String(dataPacket.getFaultCodes()));
        deviceBean.setFaultCodes(dataPacket.getFaultCodes());
        DeviceProperties deviceProperties = new DeviceProperties();
        deviceProperties.setRainState(dataPacket.getRainState());
        deviceProperties.setRainGaugeState(dataPacket.getRainGaugeState());
        switch (dataPacket.getType()) {
            case DataV30Packet.TYPE_HUMIDITY:
                //处理恒湿机状态
                deviceProperties.setHumidityController(dataPacket.getHumidityController());
                break;
            case DataV30Packet.TYPE_AIRCONDITIONER:
                //处理空调组
                deviceProperties.setAirConditionerController(dataPacket.getAirConditionerController());
                break;
        }
        deviceProperties.setCreateTime(DateUtils.getDate(dataPacket.getTimestamp()));
        //显示屏开关状态
        deviceProperties.setScreenState(dataPacket.getScreenState());
        //震动传感器灵敏度
        deviceProperties.setSensitivity(dataPacket.getSensitivity());

        deviceBean.setDeviceProperties(deviceProperties);
        deviceBean.setType(dataPacket.getType());

        // 设置设备预热时间
        deviceBean.setWarmUpTime(dataPacket.getWarmUpTime());

        if (dataPacket.qcmState == null) {
            deviceBean.qcm = 0;
        } else {
            deviceBean.qcm = dataPacket.qcmState;
        }

        deviceBean.deviceid = deviceBean.siteId + StringUtil.fillZero(deviceBean.selfid, 5);
        // TODO packet 已经不需要了，可以考虑删除 @gaohui 2014-05-07
        deviceBean.packet = StringUtil.toHex(dataPacket.getPacket());
        //是否是木卫一
        deviceBean.setUpload(dataPacket.isUpload());
        if (deviceBean.isUpload()) {
            // 添加 木卫一数据文件解析的locationId;
            deviceBean.locationId = dataPacket.getLocationId();
            deviceBean.setFirstPacket(dataPacket.isFirstPacket());
            deviceBean.setLastPacket(dataPacket.isLastPacket());
        }

        return deviceBean;
    }

    /**
     * 根据设备状态包创建 DeviceBean
     *
     * @param statusPacket
     * @return
     */
    public static DeviceBean fromPacket(StatusPacket statusPacket) {
        DeviceBean deviceBean = new DeviceBean();

        deviceBean.remoteAddress = statusPacket.getRemoteHost();
        deviceBean.remotePort = statusPacket.getRemotePort();

        deviceBean.packageType = statusPacket.getPacketType();
        deviceBean.deviceType = statusPacket.getDeviceType();
        deviceBean.version = statusPacket.getVersion();

        deviceBean.selfid = statusPacket.getSelfId();
        // TODO 可能和上行数据包冲突 @gaohui 2014-04-23
        deviceBean.sequence = statusPacket.getSequence();
        // TODO 数据库暂无存储字段,目前此字段没有显示的要求 @xiedeng
        statusPacket.getDeviceProperty();
        deviceBean.rssi = statusPacket.getRssi();
        deviceBean.lqi = statusPacket.getLqi();
        deviceBean.interval = statusPacket.getInterval();
        deviceBean.deviceMode = statusPacket.getWorkMode();
        deviceBean.isControl = statusPacket.getControl();

        deviceBean.setSn(statusPacket.getSerialNumber());
        deviceBean.siteId = statusPacket.getSiteId();

        Date timestamp = (Date) statusPacket.getAttribute().get(Packet.ATTR_TIMESTAMP);
        deviceBean.timeStamp = new Timestamp(timestamp.getTime());
        deviceBean.deviceid = deviceBean.siteId + StringUtil.fillZero(deviceBean.selfid, 5);
        deviceBean.packet = StringUtil.toHex(statusPacket.getPacket());

        deviceBean.isThresholdAlarm = statusPacket.isThresholdAlarm;
        deviceBean.thresholdList = statusPacket.thresholdList;
        deviceBean.setDemarcate(statusPacket.getDemarcate());

        return deviceBean;
    }

    // 解析 GPS 到时监测指标
    public static void parseGpsToSensors(DataV30Packet packet, DeviceBean device) {
        if (packet.isGpsExists()) {
            // type => gpsParam
            Map<Integer, DataV30Packet.GpsParam> gpsMap = Maps.uniqueIndex(packet.getGpsParams(), new Function<DataV30Packet.GpsParam, Integer>() {
                @Override
                public Integer apply(DataV30Packet.GpsParam gpsParam) {
                    return gpsParam.getType();
                }
            });

            for (Map.Entry<Integer, DataV30Packet.GpsParam> entry : gpsMap.entrySet()) {
                try {
                    switch (entry.getKey()) {
                        // 经度
                        case DataV30Packet.GpsParam.LONGITUDE:
                            // 经度类型
                            if (gpsMap.containsKey(DataV30Packet.GpsParam.LONGITUDE_TYPE)) {
                                // E/W, 正：东经　负：西经
                                String longitudeType = gpsMap.get(DataV30Packet.GpsParam.LONGITUDE_TYPE).getValue();
                                int signType = parseGpsSignType(longitudeType);
                                // dddmm.mmm
                                String longitude = entry.getValue().getValue();
                                double value = parseGpsCoordinate(longitude, 3);
                                device.sensorData.put(0x2FFF, new SensorPhysicalBean(0x2FFF, signType * value));
                            }
                            break;

                        // 纬度
                        case DataV30Packet.GpsParam.LATITUDE:
                            // 纬度类型
                            if (gpsMap.containsKey(DataV30Packet.GpsParam.LATITUDE_TYPE)) {
                                // N/S, 正：北纬　负：南纬
                                String latitudeType = gpsMap.get(DataV30Packet.GpsParam.LATITUDE_TYPE).getValue();
                                int signType = parseGpsSignType(latitudeType);
                                // ddmm.mmm
                                String latitude = entry.getValue().getValue();
                                double value = parseGpsCoordinate(latitude, 2);
                                device.sensorData.put(0x2FFE, new SensorPhysicalBean(0x2FFE, signType * value));
                            }
                            break;

                        case DataV30Packet.GpsParam.ALTITUDE:
                            double altitude = Double.parseDouble(entry.getValue().getValue());
                            device.sensorData.put(0x2FFD, new SensorPhysicalBean(0x2FFD, altitude));

                            break;

                        case DataV30Packet.GpsParam.SPEED:
                            // 节
                            double speedKnots = Double.parseDouble(entry.getValue().getValue());
                            // 参考 http://zh.wikipedia.org/wiki/%E7%AF%80_(%E5%96%AE%E4%BD%8D)
                            // 公里/小时
                            double speedKm = speedKnots * 1.852;
                            // 如果时速超过每小时300公里，认为数据不可靠
                            if (speedKm <= 300) {
                                device.sensorData.put(0x2FFC, new SensorPhysicalBean(0x2FFC, speedKm));
                            }
                            break;

                        case DataV30Packet.GpsParam.DIRECTION:
                            double direction = Double.parseDouble(entry.getValue().getValue());
                            device.sensorData.put(0x2FFB, new SensorPhysicalBean(0x2FFB, direction));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析 gps 原始格式经度，纬度
     *
     * @param originValue
     * @param degreeLength 度的长度
     * @return
     */
    public static double parseGpsCoordinate(String originValue, int degreeLength) {
        String degreeStr = originValue.substring(0, degreeLength);
        int degree = Integer.parseInt(degreeStr);
        double minute = Double.parseDouble(originValue.substring(degreeLength));
        return degree + (minute / 60);
    }

    /**
     * 根据经纬度类型，返回对应的经纬度符号(正负)
     * 经度（正：东经　负：西经）
     * 纬度（正：北纬　负：南纬）
     *
     * @param coordinateType
     * @return
     */
    public static int parseGpsSignType(String coordinateType) {
        if (coordinateType.equalsIgnoreCase("N") || coordinateType.equalsIgnoreCase("E")) {
            return 1;
        }

        if (coordinateType.equalsIgnoreCase("S") || coordinateType.equalsIgnoreCase("W")) {
            return -1;
        }

        return 1;
    }

}
