package com.microwise.msp.hardware.handler.codec;

import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.handler.codec.v13.DataPacketV13Parser;
import com.microwise.msp.hardware.handler.codec.v13.DataV13Packet;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-8-7 11:42
 */
public class DataPacketV13ParserTest {

    @Test
    public void testIsParseable() {
        Packet packet = new Packet();
        packet.setPacketType(Defines.PACKAGE_DATA);
        packet.setVersion(Defines.VERSION_1);

        PacketParser packetParser = new DataPacketV13Parser();
        Assert.assertTrue(packetParser.isParseable(packet));
    }

    @Test
    public void testParse() {
        String dataStr = "55AA0100011128010146462800C4232006BB211AD58974";

        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);

        PacketParser<DataV13Packet> packetParser = new DataPacketV13Parser();
        Assert.assertTrue(packetParser.isParseable(packet));

        DataPacket p = packetParser.parse(packet);
        Assert.assertNotNull(p);
        Assert.assertTrue(p instanceof DataV13Packet);

        DataV13Packet dataPacket = (DataV13Packet) p;
        Assert.assertEquals(1, dataPacket.getPacketType());
        Assert.assertEquals(1, dataPacket.getDeviceType());
        Assert.assertEquals(1, dataPacket.getVersion());


        Assert.assertEquals(17, dataPacket.getBodyLength());
        Assert.assertEquals(40, dataPacket.getNetId());
        Assert.assertEquals(1, dataPacket.getJump());
        Assert.assertEquals(1, dataPacket.getParentId());
        Assert.assertEquals(70, dataPacket.getSelfId());
        Assert.assertEquals(70, dataPacket.getFeedback());
        Assert.assertEquals(40, dataPacket.getSequence());
        Assert.assertEquals(0, dataPacket.getVoltage());
        Assert.assertEquals(-60, dataPacket.getRssi());
        Assert.assertEquals(35, dataPacket.getLqi());

        Map<Integer, Double> sensors = dataPacket.getSensors();
        Assert.assertNotNull(sensors);
        Assert.assertTrue(!sensors.isEmpty());
        Assert.assertEquals(2, sensors.size());

        Assert.assertEquals(1723, sensors.get(32).intValue());
        Assert.assertEquals(6869, sensors.get(33).intValue());
    }
}
