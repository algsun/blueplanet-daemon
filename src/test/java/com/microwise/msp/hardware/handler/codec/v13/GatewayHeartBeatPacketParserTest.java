package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 13-8-13 11:12
 */
public class GatewayHeartBeatPacketParserTest {

    @Test
    public void testParseable() {
        PacketParser<GatewayHeartBeatPacket> packetParser = new GatewayHeartBeatPacketParser();
        Packet packet = new Packet();
        packet.setVersion(Versions.V_1);
        packet.setPacketType(Packets.GATEWAY_HEART_BEAT);

        Assert.assertTrue(packetParser.isParseable(packet));

        packet.setPacketType(Packets.NODE_DATA);
        Assert.assertFalse(packetParser.isParseable(packet));
    }

    @Test
    public void testParse() {
        String dataStr = "55AA0700010D28010146462800C4238974E5C6";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);
        Assert.assertEquals(Versions.V_1, packet.getVersion());
        Assert.assertEquals(Packets.GATEWAY_HEART_BEAT, packet.getPacketType());


        PacketParser<GatewayHeartBeatPacket> packetParser = new GatewayHeartBeatPacketParser();
        Assert.assertTrue(packetParser.isParseable(packet));

        GatewayHeartBeatPacket p = packetParser.parse(packet);
        Assert.assertNotNull(p);
        Assert.assertEquals(Versions.V_1, p.getVersion());
        Assert.assertEquals(Packets.GATEWAY_HEART_BEAT, p.getPacketType());

        Assert.assertEquals(13, p.getBodyLength());
        Assert.assertEquals(40, p.getNetId());
        Assert.assertEquals(1, p.getJump());
        Assert.assertEquals(1, p.getParentId());
        Assert.assertEquals(70, p.getSelfId());
        Assert.assertEquals(70, p.getFeedback());
        Assert.assertEquals(40, p.getSequence());
        Assert.assertEquals(0, p.getVoltage());
        Assert.assertEquals(-60, p.getRssi());
        Assert.assertEquals(35, p.getLqi());
    }
}
