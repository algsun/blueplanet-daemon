package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.MultiVersionPacketSplitter;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketParser;
import com.microwise.msp.hardware.handler.codec.PacketSplitter;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 13-10-18 13:16
 */
public class DataV30PacketsTest {

    @Test
    public void testParseGpsCoordinate(){
        String longitude  = "3411.84125";
        double value = DataV30Packets.parseGpsCoordinate(longitude, 2);
        Assert.assertEquals(34.19735, value, 0.00001);


        longitude  = "0401.84125";
        value = DataV30Packets.parseGpsCoordinate(longitude, 2);
        Assert.assertEquals(4.03068, value, 0.00001);

        String latitude  = "10851.31033";
        value = DataV30Packets.parseGpsCoordinate(latitude, 3);
        Assert.assertEquals(108.85517, value, 0.00001);

        latitude  = "00801.31033";
        value = DataV30Packets.parseGpsCoordinate(latitude, 3);
        Assert.assertEquals(8.02183, value, 0.00001);
    }

    @Test
    public void testFaultCodes(){
        String dataStr = "55AA01070338010100010008060000A000001EA0010D0B070A1F0CA0040101A00200BC614FA0030000A0060000A00800A0050001A0050003A00500020AC3";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        PacketParser<DataV30Packet> dataPacketParser = new DataPacketV30Parser();
        Packet p = dataPacketParser.parse(packet);
        DataV30Packet dataPacket = (DataV30Packet) p;
        Assert.assertEquals(3, dataPacket.getFaultCodes().size());
        Assert.assertEquals(1,(int) dataPacket.getFaultCodes().get(0));
        Assert.assertEquals(3,(int) dataPacket.getFaultCodes().get(1));
        Assert.assertEquals(2,(int) dataPacket.getFaultCodes().get(2));
    }

    @Test
    public void testFaultCodes2(){
        String dataStr = "55AA0107032C010100010008060000A000001EA0010D0B070A1F0CA0040101A00200BC614FA0030000A0060000A00800D436";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        PacketParser<DataV30Packet> dataPacketParser = new DataPacketV30Parser();
        Packet p = dataPacketParser.parse(packet);
        DataV30Packet dataPacket = (DataV30Packet) p;
        Assert.assertNull(dataPacket.getFaultCodes());
    }

    @Test
    public void testParseSwitch(){
       String dataStr = "55AA010503220001000C000228D515A0000258A0010E01150D3A1FA00203A2F62DA0090F0F820ACE";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        PacketParser<DataV30Packet> dataPacketParser = new DataPacketV30Parser();
        Packet p = dataPacketParser.parse(packet);
        DataV30Packet dataPacket = (DataV30Packet) p;

        Assert.assertTrue(dataPacket.isSwitchExists());
        Assert.assertEquals(0x0F, dataPacket.getSwitchEnable());
        Assert.assertEquals(0x0F, dataPacket.getSwitchStatus());
        Assert.assertNotNull(dataPacket.getSwitches());

        Assert.assertEquals(6, dataPacket.getSwitches().size());

        Switch switcH = dataPacket.getSwitches().get(1);
        Assert.assertEquals(2, switcH.getIndex());
        Assert.assertTrue(switcH.isChanged());


        switcH = dataPacket.getSwitches().get(3);
        Assert.assertEquals(4, switcH.getIndex());
        Assert.assertTrue(switcH.isEnable());
        Assert.assertTrue(switcH.isOn());
        Assert.assertTrue(switcH.isCondRelf());

        switcH = dataPacket.getSwitches().get(4);
        Assert.assertEquals(5, switcH.getIndex());
        Assert.assertFalse(switcH.isEnable());
        Assert.assertFalse(switcH.isOn());
    }

    @Test
    public void testParseConditionRefl(){
        String dataStr = "55AA01050336000A000B000328D515A000000AA0010E04170E081EA00203A2F62DA009432100A00A013E000020089809600A2804B00578064002838B";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        PacketParser<DataV30Packet> dataPacketParser = new DataPacketV30Parser();
        Packet p = dataPacketParser.parse(packet);
        DataV30Packet dataPacket = (DataV30Packet) p;

        Assert.assertNotNull(dataPacket);
        Assert.assertTrue(dataPacket.isConditionReflExists());
        DataV30Packet.ConditionRefl conditionRefl = dataPacket.getConditionRefl();
        Assert.assertNotNull(conditionRefl);
        Assert.assertEquals(1, conditionRefl.getRoute());
        Assert.assertEquals(62, conditionRefl.getSubTerminalId());
        Assert.assertEquals(32, conditionRefl.getSensorId());
        Assert.assertEquals(1200, conditionRefl.getLowLeft());
        Assert.assertEquals(1400, conditionRefl.getLow());
        Assert.assertEquals(1600, conditionRefl.getLowRight());
        Assert.assertEquals(2200, conditionRefl.getHighLeft());
        Assert.assertEquals(2400, conditionRefl.getHigh());
        Assert.assertEquals(2600, conditionRefl.getHighRight());
        Assert.assertEquals(2, conditionRefl.getAction());
    }

}

