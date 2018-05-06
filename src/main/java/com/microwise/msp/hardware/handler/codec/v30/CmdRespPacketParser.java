package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.Versions;
import com.microwise.msp.util.MergeUtil;

import java.nio.ByteBuffer;

/**
 * 解析命令响应包
 *
 * @author gaohui
 * @date 13-8-15 17:53
 */
public class CmdRespPacketParser implements PacketParser<CmdRespPacket> {

    @Override
    public boolean isParseable(Packet packet) {
        return packet.getVersion() == Versions.V_3 && packet.getPacketType() == Packets.UP_COMMAND_ACK;
    }

    @Override
    public CmdRespPacket parse(Packet packet) {
        ByteBuffer buf = ByteBuffer.wrap(packet.getPacket());

        CmdRespPacket cmdResp = new CmdRespPacket(packet);

        V30PacketParser.parseCommon(packet, cmdResp);

        buf.position(6);

        byte low = buf.get();
        byte high = buf.get();
        int terminalId = MergeUtil.merge2((char) high, (char) low);
        cmdResp.setTerminalId(terminalId);
        cmdResp.setSourceSequence((short) (buf.get() & 0xFF));
        cmdResp.setOrderId(buf.getShort() & 0xFFFF);
        cmdResp.setFeedback((short) (buf.get() & 0xFF));

        return cmdResp;
    }
}
