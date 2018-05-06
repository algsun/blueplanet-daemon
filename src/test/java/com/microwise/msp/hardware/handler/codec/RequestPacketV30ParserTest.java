package com.microwise.msp.hardware.handler.codec;

import com.microwise.msp.hardware.handler.codec.v30.RequestPacket;
import com.microwise.msp.hardware.handler.codec.v30.RequestPacketV30Parser;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @author gaohui
 * @date 13-8-10 10:38
 */
public class RequestPacketV30ParserTest {

    @Test
    public void testIsParseable() {
        Packet packet = new Packet();
        packet.setVersion(Versions.V_3);
        packet.setPacketType(Packets.UP_REQUEST);

        PacketParser packetParser = new RequestPacketV30Parser();

        Assert.assertTrue(packetParser.isParseable(packet));
    }

    // 测试包的基本信息复制是否正确
    @Test
    public void testParse(){
        String  dataStr = "55AA050703060101FFFFB18A";
        byte[]  data = StringUtil.fromHexs(dataStr);

        Packet packet = new Packet();
        packet.setPacketType(Packets.UP_REQUEST);
        packet.setVersion(Versions.V_3);
        packet.setRemote(new InetSocketAddress("123.134.156.178", 7890));
        packet.setPacket(data);

        PacketParser<RequestPacket> packetParser = new RequestPacketV30Parser();

        Packet reqPacket =  packetParser.parse(packet);

        Assert.assertNotNull(reqPacket);
        Assert.assertEquals(Packets.UP_REQUEST , reqPacket.getPacketType());
        Assert.assertEquals(Versions.V_3, reqPacket.getVersion());
        Assert.assertEquals("123.134.156.178", reqPacket.getRemoteHost());
        Assert.assertEquals(7890, reqPacket.getRemotePort());
        Assert.assertEquals(data.length , reqPacket.getPacket().length);
    }

    @Test
    public void testParse2(){
        String  dataStr = "55AA050703060102FFFFB18A";
        byte[]  data = StringUtil.fromHexs(dataStr);

        Packet packet = new Packet();
        packet.setPacketType(Packets.UP_REQUEST);
        packet.setVersion(Versions.V_3);
        packet.setRemoteHost("1234");
        packet.setRemotePort(7890);
        packet.setPacket(data);

        PacketParser<RequestPacket> packetParser = new RequestPacketV30Parser();

        RequestPacket reqPacket = packetParser.parse(packet);
        Assert.assertEquals(7, reqPacket.getDeviceType());
        Assert.assertEquals(6, reqPacket.getBodyLength());
        Assert.assertEquals(0x01, reqPacket.getOrderId());
        Assert.assertEquals(0x02, reqPacket.getSequence());
    }

    @Test
    public void testParseReportTime(){
        String  dataStr = "55AA0507030C030104000102030405065C18";
        byte[]  data = StringUtil.fromHexs(dataStr);

        Packet packet = new Packet();
        packet.setPacketType(Packets.UP_REQUEST);
        packet.setVersion(Versions.V_3);
        packet.setRemoteHost("1234");
        packet.setRemotePort(7890);
        packet.setPacket(data);

        PacketParser<RequestPacket> packetParser = new RequestPacketV30Parser();

        RequestPacket reqPacket = packetParser.parse(packet);
        Assert.assertEquals(7, reqPacket.getDeviceType());
        Assert.assertEquals(12, reqPacket.getBodyLength());
        Assert.assertEquals(0x03, reqPacket.getOrderId());
        Assert.assertEquals(0x01, reqPacket.getSequence());
        Assert.assertEquals(0x04, reqPacket.getTerminalId());
        Assert.assertNotNull(reqPacket.getTimestamp());
    }

}
