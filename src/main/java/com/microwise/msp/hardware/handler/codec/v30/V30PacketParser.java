package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 14-1-20 16:08
 */
public class V30PacketParser {

    /**
     * 解析 v30 协议包公共部分
     *
     * @param src
     * @param target
     */
    public static void parseCommon(Packet src, Packet target) {
        ByteBuffer buf = ByteBuffer.wrap(src.getPacket());

        target.setPacketType(buf.get(2) & 0xFF);
        target.setDeviceType(buf.get(3) & 0xFF);
        target.setVersion(buf.get(4) & 0xFF);
        target.setBodyLength(buf.get(5) & 0xFF);

        target.setCrc(buf.getShort(buf.limit() - 2));

        if (!Packets.V30_DEVICE_TYPES.contains(target.getDeviceType())) {
            throw new IllegalArgumentException("未知设备类型: " + target.getDeviceType());
        }
    }
}
