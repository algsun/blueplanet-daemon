package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 14-1-17 13:33
 */
public class StatusPacketParserTest {
    @Test
    public void testIsParsable() {
        Packet packet = new Packet();
        packet.setVersion(Versions.V_3);
        packet.setPacketType(Packets.UP_STATUS);

        PacketParser<StatusPacket> packetParser = new StatusPacketParser();

        Assert.assertTrue(packetParser.isParseable(packet));

        packet.setPacketType(Packets.UP_DATA);
        Assert.assertFalse(packetParser.isParseable(packet));
    }

    @Test
    public void testParse() {
        String dataStr = "55AA030703180100010000000000003C000000010000000003A2F62D494B";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        PacketParser<StatusPacket> packetParser = new StatusPacketParser();
        StatusPacket statusPacket = packetParser.parse(packet);

        Assert.assertNotNull(statusPacket);
        Assert.assertEquals(3, statusPacket.getPacketType());
        Assert.assertEquals(7, statusPacket.getDeviceType());
        Assert.assertEquals(3, statusPacket.getVersion());

        Assert.assertEquals(24, statusPacket.getBodyLength());
        Assert.assertEquals(1, statusPacket.getDemarcate());
        Assert.assertEquals(1, statusPacket.getSelfId());
        Assert.assertEquals(0, statusPacket.getSequence());
        Assert.assertEquals(0, statusPacket.getVoltage());
        Assert.assertEquals(0, statusPacket.getRssi());
        Assert.assertEquals(0, statusPacket.getLqi());

        Assert.assertEquals(60, statusPacket.getInterval());
        Assert.assertEquals(0, statusPacket.getWorkMode());
        Assert.assertEquals(0, statusPacket.getConnectionCount());
        Assert.assertEquals(1, statusPacket.getControl());
        Assert.assertEquals("00000000", statusPacket.getSerialNumber());
        Assert.assertEquals("61011501", statusPacket.getSiteId());
    }
}
