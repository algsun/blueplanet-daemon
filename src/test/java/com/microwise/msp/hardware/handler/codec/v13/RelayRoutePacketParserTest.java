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
public class RelayRoutePacketParserTest {

    @Test
    public void testParseable() {
        PacketParser<RelayRoutePacket> packetParser = new RelayRoutePacketParser();
        Packet packet = new Packet();
        packet.setVersion(Versions.V_1);
        packet.setPacketType(Packets.RELAY_ROUTE);

        Assert.assertTrue(packetParser.isParseable(packet));

        packet.setPacketType(Packets.NODE_DATA);
        Assert.assertFalse(packetParser.isParseable(packet));
    }

    @Test
    public void testParse() {
        String dataStr = "55AA0200010D28010146462800C4238974E0CA";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);
        Assert.assertEquals(Versions.V_1, packet.getVersion());
        Assert.assertEquals(Packets.RELAY_ROUTE, packet.getPacketType());


        PacketParser<RelayRoutePacket> packetParser = new RelayRoutePacketParser();
        Assert.assertTrue(packetParser.isParseable(packet));

        RelayRoutePacket p = packetParser.parse(packet);
        Assert.assertNotNull(p);
        Assert.assertEquals(Versions.V_1, p.getVersion());
        Assert.assertEquals(Packets.RELAY_ROUTE, p.getPacketType());

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
