package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.businessbean.Threshold;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.Versions;
import com.microwise.msp.util.MergeUtil;
import com.microwise.msp.util.StringUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 上行状态包解析器
 *
 * @author gaohui
 * @date 14-1-17 13:20
 */
public class StatusPacketParser implements PacketParser<StatusPacket> {

    @Override
    public boolean isParseable(Packet packet) {
        return packet.getVersion() == Versions.V_3 && packet.getPacketType() == Packets.UP_STATUS;
    }

    @Override
    public StatusPacket parse(Packet packet) {
        StatusPacket statusPacket = new StatusPacket(packet);
        V30PacketParser.parseCommon(packet, statusPacket);

        ByteBuffer buf = ByteBuffer.wrap(packet.getPacket());

        buf.position(6);
//        // 父节点ID--2byte
//        byte low = buf.get();
//        byte high = buf.get();
//        statusPacket.setParentId(MergeUtil.merge2((char) high, (char) low));
        // 标定模式
        statusPacket.setDemarcate(buf.get() & 0xFF);
        // 预留
        buf.get();

        // 终端ID--2byte
        byte low = buf.get();
        byte high = buf.get();
        statusPacket.setSelfId(MergeUtil.merge2((char) high, (char) low));

        statusPacket.setSequence(buf.get() & 0xFF);
        statusPacket.setDeviceProperty(buf.get() & 0xFF);
        statusPacket.setRssi(buf.get());
        statusPacket.setLqi(buf.get() & 0xFF);
        statusPacket.setInterval(buf.getShort() & 0xFFFF);
        statusPacket.setWorkMode(buf.get() & 0xFF);
        statusPacket.setConnectionCount(buf.getShort() & 0xFFFF);
        statusPacket.setControl(buf.get() & 0xFF);
        int intSn = buf.getInt() & 0xFFFFFFFF;
        String sn = Integer.toHexString(intSn);
        int size = 8 - sn.length();
        for (int i = 0; i < size; i++) {
            sn = "0" + sn;
        }
        statusPacket.setSerialNumber(sn.toUpperCase());
        String siteId = String.valueOf(buf.getInt() & 0xFFFFFFFF);
        if (siteId.length() != 8) {
            throw new IllegalArgumentException("站点ID(接入点号不是8位): " + siteId);
        }
        statusPacket.setSiteId(siteId);

        List<Threshold> thresholdList = new ArrayList<Threshold>();
        while (buf.position() < buf.limit() - 3) {
            switch (buf.get() & 0xFF) {
                case 0x00:
                    statusPacket.setIsThresholdAlarm(buf.get() & 0xFF);
                    break;
                case 0x01:
                    Threshold threshold = new Threshold();
                    int sensorId = buf.getShort() & 0xFFFF;

//                    threshold.setDeviceId(statusPacket.siteId + StringUtil.fillZero(statusPacket.selfId, 5));
                    threshold.setSensorId(sensorId);
                    if (sensorId >= 0x0800 && sensorId <= 0xFFFF) { // 在此区间的传感量数值长度为4字节
                        threshold.setMaxValue(buf.getInt() & 0xFFFFFFFF);
                        threshold.setMinValue(buf.getInt() & 0xFFFFFFFF);
                    } else if (sensorId >= 0x0000 && sensorId <= 0x07FF) {  // 在此区间的传感量参数值长度为 2 字节
                        threshold.setMaxValue(buf.getShort() & 0xFFFF);
                        threshold.setMinValue(buf.getShort() & 0xFFFF);
                    }
                    thresholdList.add(threshold);
                    break;
                default:
                    break;
            }
        }
        statusPacket.setThresholdList(thresholdList);

        return statusPacket;
    }

    /**
     * 是否单字节标识
     *
     * @param b
     * @return
     */
    private boolean isSingleParamId(byte b) {
        return (b & 0xFF) >= 0 && (b & 0xFF) <= 0x7F;
    }
}
