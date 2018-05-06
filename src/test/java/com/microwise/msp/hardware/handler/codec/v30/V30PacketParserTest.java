package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 14-1-20 16:19
 */
public class V30PacketParserTest {

    @Test
    public void testParseCommon(){
        String  dataStr = "55AA050703060101FFFFB18A";
        byte[]  data = StringUtil.fromHexs(dataStr);

        Packet packet = new Packet();
        packet.setPacket(data);

        Packet targetPacket = new Packet();
        V30PacketParser.parseCommon(packet, targetPacket);

        Assert.assertEquals(0x05, targetPacket.getPacketType());
        Assert.assertEquals(0x07, targetPacket.getDeviceType());
        Assert.assertEquals(0x03, targetPacket.getVersion());
        Assert.assertEquals(0x06, targetPacket.getBodyLength());
        Assert.assertEquals(0xB18A,targetPacket.getCrc() & 0xFFFF);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCommon2(){
        String  dataStr = "55AA050903060101FFFFB18A";
        byte[]  data = StringUtil.fromHexs(dataStr);

        Packet packet = new Packet();
        packet.setPacket(data);

        Packet targetPacket = new Packet();
        V30PacketParser.parseCommon(packet, targetPacket);
    }

}
