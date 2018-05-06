package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.Versions;
import com.microwise.msp.util.MergeUtil;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;

/**
 * v3 协议, 上行请求包(0x05)
 *
 * @author gaohui
 * @date 13-8-10 10:28
 */
public class RequestPacketV30Parser implements PacketParser<RequestPacket> {
    public static final Logger pLog = Packets.log;

    @Override
    public boolean isParseable(Packet packet) {
        if (packet.getVersion() == Versions.V_3 && packet.getPacketType() == Packets.UP_REQUEST) {
            return true;
        }

        return false;
    }

    @Override
    public RequestPacket parse(Packet packet) {
        RequestPacket reqPacket = new RequestPacket(packet);

        ByteBuffer buf = ByteBuffer.wrap(packet.getPacket());
        V30PacketParser.parseCommon(packet, reqPacket);

        buf.position(6);
        // 指令标识
        reqPacket.setOrderId(buf.get() & 0xFF);
        // 序列号
        reqPacket.setSequence(buf.get() & 0xFF);

        // 参数
        switch (reqPacket.getOrderId()){
            case RequestPacket.ORDER_REQUEST_CONNECT:
                // 2
                buf.getShort();
                break;
            case RequestPacket.ORDER_REQUEST_TIME:
                // 2
                buf.getShort();
                break;
            case RequestPacket.ORDER_REPORT_TIME:
                // 2, 节点ID
                byte low = buf.get();
                byte high = buf.get();
                reqPacket.setTerminalId(MergeUtil.merge2((char) high, (char) low));
                // 6，时间缀
                byte[] timestamp = new byte[6];
                buf.get(timestamp);
                reqPacket.setTimestamp(DataPacketV30Parser.parseTimestamp(ByteBuffer.wrap(timestamp)));
                break;
            default:
                pLog.error("XXX 未知指令标识: {}, {}", reqPacket.getOrderId(), StringUtil.toHex(packet.getPacket()));
                break;
        }


        return reqPacket;
    }
}
