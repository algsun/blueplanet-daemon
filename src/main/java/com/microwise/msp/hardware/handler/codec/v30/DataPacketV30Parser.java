package com.microwise.msp.hardware.handler.codec.v30;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.util.MergeUtil;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * v3.0 协议解析
 * <p>
 * 负责上行数据包(0x01)解析
 *
 * @author gaohui
 * @date 13-8-5 18:12
 */
public class DataPacketV30Parser implements PacketParser<DataV30Packet> {
    public static final Logger log = LoggerFactory.getLogger(DataPacketV30Parser.class);
    public static final Logger pLog = Packets.log;

    // 最大开关个数
    public static final int SWITCH_MAX_SIZE = 6;

    @Override
    public boolean isParseable(Packet packet) {
        if (packet.getVersion() == Versions.V_3 && packet.getPacketType() == Packets.UP_DATA) {
            return true;
        }

        return false;
    }

    @Override
    public DataV30Packet parse(Packet packet) {
        DataV30Packet dataPacket = new DataV30Packet(packet);

        V30PacketParser.parseCommon(packet, dataPacket);

        ByteBuffer buf = ByteBuffer.wrap(packet.getPacket());
        parseHead(dataPacket, buf);
        parseBody(dataPacket, buf);

        // 设备ID 不能为 0 和 65535
        Preconditions.checkArgument(dataPacket.getParentId() != 0 && dataPacket.getParentId() != 65535,
                "父节点ID不能为 " + dataPacket.getParentId());
        Preconditions.checkArgument(dataPacket.getSelfId() != 0 && dataPacket.getSelfId() != 65535,
                "设备ID不能为 " + dataPacket.getSelfId());

        return dataPacket;
    }

    private void parseHead(DataV30Packet dataPacket, ByteBuffer buf) {

        buf.position(6);
        int isControl = buf.get() & 0xFF;
        if (isControl == 0 || isControl == 1) {
            dataPacket.setControl(isControl);
        } else {
            pLog.error("XXX 未知控制类型(可控、不可控): {}, {}", isControl, StringUtil.toHex(dataPacket.getPacket()));
        }

        // 父节点ID--2byte
        byte low = buf.get();
        byte high = buf.get();
        dataPacket.setParentId(MergeUtil.merge2((char) high, (char) low));

        // 终端ID--2byte
        low = buf.get();
        high = buf.get();
        dataPacket.setSelfId(MergeUtil.merge2((char) high, (char) low));

        dataPacket.setSequence(buf.get() & 0xFF);
        dataPacket.setVoltage(buf.get() & 0xFF);
        dataPacket.setRssi(buf.get());
        dataPacket.setLqi(buf.get() & 0xFF);
    }

    private void parseBody(DataV30Packet dataPacket, ByteBuffer buf) {
        Map<Integer, Double> sensors = new LinkedHashMap<Integer, Double>();
        while (buf.position() < buf.limit() - 3) {
            // 监测指标 (2byte)
            int sensorId = buf.getShort() & 0xFFFF;
            if (isSensorParam(sensorId)) {
                parseSensorParam(dataPacket, sensorId, sensors, buf);
            } else if (isTerminalParam(sensorId)) {
                parseTerminalParam(dataPacket, sensorId, buf);
            } else {
                // 未知参数标识 @gaohui 2013-08-08
                pLog.error("XXX 未知参数标识: {}, {}", sensorId, StringUtil.toHex(dataPacket.getPacket()));
            }
        }
        dataPacket.setSensors(sensors);
    }

    private void parseSensorParam(DataV30Packet dataPacket, int sensorId, Map<Integer, Double> sensors, ByteBuffer buf) {

        // 监测指标原始值(无符号)
        double sensorValue = 0f;

        // 监测指标原始值(无符号)
        if (sensorId >= 0x0000 && sensorId <= 0x07FF) {  // 在此区间的传感量参数值长度为 2 字节
            sensorValue = buf.getShort() & 0xFFFF;
        } else if (sensorId >= 0x0800 && sensorId <= 0x0BFF) { // 在此区间的传感量数值长度为4字节
            sensorValue = buf.getFloat();
        } else if (sensorId >= 0x0C00 && sensorId <= 0x0CFF) {// 在此区间的传感量数值长度为8字节
            sensorValue = buf.getDouble();
        } else if (sensorId >= 0x0D00 && sensorId <= 0xFFFF) { // 在此区间的传感量数值长度为4字节
            sensorValue = buf.getFloat();
        }
        sensors.put(sensorId, sensorValue);
    }

    /**
     * 解析终端参数
     *
     * @param dataPacket
     * @param sensorId
     * @param buf
     */
    private void parseTerminalParam(DataV30Packet dataPacket, int sensorId, ByteBuffer buf) {
        switch (sensorId) {
            case Params.INTERVAL_S:
                //工作周期为秒
                dataPacket.setInterval(buf.getShort() & 0xFFFF);
                break;
            case Params.INTERVAL_M:
                //工作周期为分钟
                int minute = buf.getShort() & 0xFFFF;
                dataPacket.setInterval(minute * 60);
                break;
            case Params.TIMESTAMP:
                // 6
                byte[] timestamp = new byte[6];
                buf.get(timestamp);
                dataPacket.setTimestamp(parseTimestamp(ByteBuffer.wrap(timestamp)));
                break;
            case Params.SITE_ID:
                // 4
                // 目前站点ID 为数字的字符串, 且长度为 8 位
                String siteId = String.valueOf(buf.getInt() & 0xFFFFFFFF);
                if (siteId.length() != 8 || siteId.indexOf('0') == 0) {
                    throw new IllegalArgumentException("站点ID(接入点号不是8位数字): " + siteId);
                }
                dataPacket.setSiteId(siteId);
                break;
            case Params.SD_CARD_STATE:
                // 2
                dataPacket.setSdCardState(buf.getShort());
                break;
            case Params.PACKET_PAGINATE:
                // 2
                dataPacket.setPaginateCount(buf.get() & 0xFF);
                dataPacket.setPaginateIndex(buf.get() & 0xFF);
                break;
            case Params.FAULT_CODE:
                // 2
                // 故障代码
                int faultCode = buf.getShort() & 0xFFFF;
                if (dataPacket.getFaultCodes() == null) {
                    dataPacket.setFaultCodes(new ArrayList<Integer>());
                }
                dataPacket.getFaultCodes().add(faultCode);
                break;
            case Params.OFFLINE_INSTRUCTION:
                // 2
                int connectionCount = buf.getShort() & 0xFFFF;
                dataPacket.setConnectionCount(connectionCount);
                dataPacket.setConnectionCountExists(true);
                break;
            case Params.WORK_MODE:
                // 2
                buf.get();
                dataPacket.setWorkMode(buf.get() & 0xFF);
                break;
            case Params.GPS:
                try {
                    // GPS 参数总长度(1字节)
                    int length = buf.get() & 0xFF;
                    byte[] params = new byte[length];
                    buf.get(params);
                    dataPacket.setGpsParams(parseGpsParam(ByteBuffer.wrap(params)));
                    dataPacket.setGpsExists(true);
                } catch (Exception e) {
                    throw new IllegalArgumentException("GPS 解析异常", e);
                }
                break;
            case Params.SWITCH:
                // 控制模块开关状态
                int enable = buf.get() & 0xFF;
                int onOff = buf.get() & 0xFF;
                int flag = buf.get() & 0xFF;
                // 是否是条件反射期间的状态
                boolean isCondRelf = (((flag >> 7) & 0x0001) == 1);

                List<Switch> switches = new ArrayList<Switch>();
                for (int i = 0; i < SWITCH_MAX_SIZE; i++) {
                    Switch switcH = new Switch();
                    switcH.setIndex(i + 1);
                    switcH.setEnable(((enable >> i) & 0x0001) == 1);
                    switcH.setOn(((onOff >> i) & 0x0001) == 1);
                    switcH.setChanged(((flag >> i) & 0x0001) == 1);
                    switcH.setCondRelf(isCondRelf);

                    switches.add(switcH);
                }
                dataPacket.setSwitchExists(true);
                dataPacket.setSwitchEnable(enable);
                dataPacket.setSwitchStatus(onOff);
                dataPacket.setSwitchCondRefl(isCondRelf);
                dataPacket.setSwitches(switches);

                break;
            case Params.CONDITION_REFL:
                // 控制模块条件反射参数，一个数据包一个端口的条件
                DataV30Packet.ConditionRefl conditionRefl = new DataV30Packet.ConditionRefl();
                conditionRefl.setRoute(buf.get() & 0xFF);

                byte idLow = buf.get();
                byte idHigh = buf.get();
                int _terminalId = MergeUtil.merge2((char) idHigh, (char) idLow);
                conditionRefl.setSubTerminalId(_terminalId);

                conditionRefl.setSensorId(buf.getShort() & 0xFFFF);
                conditionRefl.setHighLeft(buf.getShort() & 0xFFFF);
                conditionRefl.setHigh(buf.getShort() & 0xFFFF);
                conditionRefl.setHighRight(buf.getShort() & 0xFFFF);
                conditionRefl.setLowLeft(buf.getShort() & 0xFFFF);
                conditionRefl.setLow(buf.getShort() & 0xFFFF);
                conditionRefl.setLowRight(buf.getShort() & 0xFFFF);
                conditionRefl.setAction(buf.get() & 0xFF);

                dataPacket.setConditionReflExists(true);
                dataPacket.setConditionRefl(conditionRefl);

                if (conditionRefl.getRoute() < 1 || conditionRefl.getRoute() > 8) {
                    pLog.error("XXX 条件反射的端口应该在 1 到 8 之间");
                    throw new IllegalArgumentException("条件反射的端口应该在 1 到 8 之间: " + conditionRefl.getRoute());
                }
                break;

            case Params.DEVICE_THRESHOLD:
                // TODO 节点阈值报警, 可能会有多个传感器的阈值
                int _sensorId = buf.getShort() & 0xFFFF;
                int high = buf.getShort() & 0xFFFF;
                int low = buf.getShort() & 0xFFFF;
                break;

            case Params.NET_ID:
                int netId = buf.getShort() & 0xFFFF;
                dataPacket.setParentId(Integer.parseInt(netId + StringUtil.fillZero(dataPacket.getParentId(), 3)));
                dataPacket.setSelfId(Integer.parseInt(netId + StringUtil.fillZero(dataPacket.getSelfId(), 3)));
                dataPacket.setNetIdExists(true);
                dataPacket.setDummyV3Version(true);

                if (netId > 99) {
                    throw new IllegalArgumentException("子网不能大于 99: " + netId);
                }
                break;
            case Params.ACID_RAIN_STATE:
                dataPacket.setRainGaugeState(buf.get() & 0xFF);
                dataPacket.setRainState(buf.get() & 0xFF);
                break;
            case Params.ZM_HUMIDITY_CONTROLLER_STATE:
                DataV30Packet.HumidityController zmHumidityController = new DataV30Packet.HumidityController();
                zmHumidityController.setTargetHumidity(DataV30Packet.HumidityController.TYPE_ZM);
                zmHumidityController.setCurrentTemperature((float) (buf.getShort() & 0xFFFF) / 10);
                zmHumidityController.setCurrentHumidity((float) (buf.getShort() & 0xFFFF) / 10);
                int state = buf.getShort() & 0xFFFF;
                DataV30Packet.HumidityControllerState zmHumidityControllerState = new DataV30Packet.HumidityControllerState(state);
                zmHumidityController.setHumidityControllerState(zmHumidityControllerState);
                //保留区6个字节
                buf.get(new byte[6]);
                zmHumidityController.setTargetHumidity(buf.getShort() & 0xFFFF);
                zmHumidityController.setHighHumidity(buf.getShort() & 0xFFFF);
                zmHumidityController.setLowHumidity(buf.getShort() & 0xFFFF);
                //寄存器地址
                buf.getShort();
                dataPacket.setHumidityController(zmHumidityController);
                dataPacket.setType(DataV30Packet.TYPE_HUMIDITY);
                break;
            case Params.GQ_HUMIDITY_CONTROLLER_STATE:
                DataV30Packet.HumidityController gqHumidityController = new DataV30Packet.HumidityController();
                gqHumidityController.setType(DataV30Packet.HumidityController.TYPE_GQ);
                gqHumidityController.setCurrentHumidity((float) (buf.getShort() & 0xFFFF) / 10);
                gqHumidityController.setCurrentTemperature((float) ((buf.getShort() & 0xFFFF) - 1000) / 10);
                // 抛弃环境湿度 环境温度 实时电流 保留字节
                buf.get(new byte[9]);

                DataV30Packet.HumidityControllerState gqHumidityControllerState = new DataV30Packet.HumidityControllerState();
                gqHumidityControllerState.setWaterLow((buf.get() & 0xFF) == 0xFF);
                gqHumidityControllerState.setHumidityAlarm((buf.get() & 0xFF) == 0xFF);
                gqHumidityControllerState.setTemperatureAlarm((buf.get() & 0xFF) == 0xFF);
                gqHumidityController.setHumidityControllerState(gqHumidityControllerState);
                dataPacket.setHumidityController(gqHumidityController);
                dataPacket.setType(DataV30Packet.TYPE_HUMIDITY);
                break;
            case Params.QCM_STATE:
                dataPacket.setQcmState(buf.getShort() & 0xFFFF);
                break;
            case Params.SCREEN_STATE:
                dataPacket.setScreenState(buf.get() & 0xFF);
                break;
            case Params.WARM_UP_TIME:
                dataPacket.setWarmUpTime(buf.getShort() & 0xFFFF);
                break;
            case Params.SENSITIVITY:
                dataPacket.setSensitivity(buf.get() & 0xFF);
                break;
            case Params.RT_AIRCONDITIONER_CONTROLLER_STATE:
                DataV30Packet.AirConditionerController airConditionerController = new DataV30Packet.AirConditionerController();
                airConditionerController.setSwitchState(buf.get() & 0xFF);
                airConditionerController.setTargetHumidity((buf.getShort() & 0xFFFF) / 10);
                airConditionerController.setTargetTemperature((buf.getShort() & 0xFFFF) / 10);
                airConditionerController.setType(DataV30Packet.AirConditionerController.TYPE_RT);
                dataPacket.setAirConditionerController(airConditionerController);
                dataPacket.setType(DataV30Packet.TYPE_AIRCONDITIONER);
                break;
            default:
                // 未知参数 @gaohui 2013-08-08
                pLog.error("XXX 未知参数标识: {}, {}", String.format("0x%04X", sensorId), StringUtil.toHex(dataPacket.getPacket()));
                break;
        }
    }

    /**
     * 是否是传感参数
     *
     * @param sensorId
     * @return
     */
    @VisibleForTesting
    public static boolean isSensorParam(int sensorId) {
        if (sensorId >= 0x0000 && sensorId <= 0x2FFF) {
            return true;
        }

        return false;
    }

    /**
     * 是否是终端参数
     *
     * @param sensorId
     * @return
     */
    @VisibleForTesting
    public static boolean isTerminalParam(int sensorId) {
        if (sensorId >= 0xA000 && sensorId <= 0xBFFF) {
            return true;
        }

        return false;
    }


    /**
     * 解析时间戳, 如果解析异常返回 null
     *
     * @param buf
     * @return
     */
    @VisibleForTesting
    public static Timestamp parseTimestamp(ByteBuffer buf) {
        if (buf.remaining() < 6) {
            return null;
        }

        int year = buf.get() & 0xFF;
        year = 2000 + year;
        int month = buf.get() & 0xFF;
        int day = buf.get() & 0xFF;
        int hour = buf.get() & 0xFF;
        int minute = buf.get() & 0xFF;
        int second = buf.get() & 0xFF;


        try {
            long millis = DateTime.now()
                    .withDate(year, month, day)
                    .withTime(hour, minute, second, 0).getMillis();

            return new Timestamp(millis);
        } catch (IllegalFieldValueException e) {
            log.debug("", e);
            return null;
        }
    }

    /**
     * 解析 GPS 参数
     *
     * @param buf
     * @return
     */
    public static List<DataV30Packet.GpsParam> parseGpsParam(ByteBuffer buf) {
        List<DataV30Packet.GpsParam> gpsParams = new ArrayList<DataV30Packet.GpsParam>();
        while (buf.hasRemaining()) {
            int type = buf.get() & 0xFF;
            int length = buf.get() & 0xFF;
            byte[] value = new byte[length];
            buf.get(value);
            if (length == 0) continue;
            gpsParams.add(new DataV30Packet.GpsParam(type, length, value));
        }

        return gpsParams;
    }
}
