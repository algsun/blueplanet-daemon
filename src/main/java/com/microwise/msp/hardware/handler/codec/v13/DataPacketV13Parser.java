package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.Versions;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * v1.3 协议
 * <p/>
 * 上行数据包
 *
 * @author gaohui
 * @date 13-8-6 16:09
 */
public class DataPacketV13Parser implements PacketParser<DataV13Packet> {
    @Override
    public boolean isParseable(Packet packet) {
        if (packet.getVersion() == Versions.V_1 && packet.getPacketType() == Packets.NODE_DATA) {
            return true;
        }

        return false;
    }

    @Override
    public DataV13Packet parse(Packet packet) {
        DataV13Packet dataPacket = new DataV13Packet(packet);

        // v1.3 默认都不可控
        dataPacket.setControl(1);
        dataPacket.setTimestamp(new Timestamp(new Date().getTime()));

        ByteBuffer buf = ByteBuffer.wrap(packet.getPacket());
        dataPacket.setDeviceType(buf.get(2) & 0xFF);
        dataPacket.setSequence(buf.get(11) & 0xFF);

        dataPacket.setCrc(buf.getShort(buf.limit() - 2));

        parseHead(dataPacket, buf);
        parseBody(dataPacket, buf);

        return dataPacket;
    }

    private void parseHead(DataV13Packet dataPacket, ByteBuffer buf) {
        buf.position(5);
        dataPacket.setBodyLength(buf.get() & 0xFF);
        dataPacket.setNetId(buf.get() & 0xFF);
        dataPacket.setJump(buf.get() & 0xFF);
        dataPacket.setParentId(buf.get() & 0xFF);
        dataPacket.setSelfId(buf.get() & 0xFF);
        dataPacket.setFeedback(buf.get() & 0xFF);
        dataPacket.setSequence(buf.get() & 0xFF);
        dataPacket.setVoltage(buf.get() & 0xFF);
        dataPacket.setRssi(buf.get());  // 有符号
        dataPacket.setLqi(buf.get() & 0xFF); // 有符号


        if (dataPacket.getNetId() > 99) {
            throw new IllegalArgumentException("子网不能大于 99: " + dataPacket.getNetId());
        }
    }

    private void parseBody(DataV13Packet dataPacket, ByteBuffer buf) {
        Map<Integer, Double> sensors = new LinkedHashMap<Integer, Double>();
        while (buf.position() < buf.limit() - 3) {
            // 监测指标
            int sensorPhysicalId = buf.get() & 0xFF;
            // 监测指标原始值(无符号)
            double sensorValue = buf.getShort() & 0xFFFF;

            sensors.put(sensorPhysicalId, sensorValue);
        }
        dataPacket.setSensors(sensors);
    }
}
