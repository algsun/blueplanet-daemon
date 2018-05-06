package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.Versions;

import java.nio.ByteBuffer;

/**
 * v1.3 常规数据包解析
 *
 * @author gaohui
 * @date 13-8-13 10:46
 */
public abstract class DefaultPacketParser<T extends DefaultPacket> implements PacketParser<T> {
    @Override
    public boolean isParseable(Packet packet) {
        return packet.getVersion() == Versions.V_1;
    }

    @Override
    public T parse(Packet packet) {
        T defaultPacket = copyFrom(packet);

        ByteBuffer buf = ByteBuffer.wrap(defaultPacket.getPacket());
        parseHead(defaultPacket, buf);

        return defaultPacket;
    }

    abstract T copyFrom(Packet packet);

    protected void parseHead(DefaultPacket packet, ByteBuffer buf) {
        buf.position(5);
        packet.setBodyLength(buf.get() & 0xFF);
        packet.setNetId(buf.get() & 0xFF);
        packet.setJump(buf.get() & 0xFF);
        packet.setParentId(buf.get() & 0xFF);
        packet.setSelfId(buf.get() & 0xFF);
        packet.setFeedback(buf.get() & 0xFF);
        packet.setSequence(buf.get() & 0xFF);
        packet.setVoltage(buf.get() & 0xFF);
        packet.setRssi(buf.get());  // 有符号
        packet.setLqi(buf.get() & 0xFF); // 有符号

        if(packet.getNetId() > 99){
            throw new IllegalArgumentException("子网不能大于 99: " + packet.getNetId());
        }
    }

}
