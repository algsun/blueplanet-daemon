package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 13-8-15 17:56
 */
public class CmdRespPacketParserTest {

    @Test
    public void isParseable(){
        Packet packet = new Packet();
        packet.setVersion(Versions.V_3);
        packet.setPacketType(Packets.UP_COMMAND_ACK);

        PacketParser packetParser = new CmdRespPacketParser();
        Assert.assertTrue(packetParser.isParseable(packet));
    }

    @Test
    public void parse(){
        String dataStr = "55AA0A0703080500010012011888";
        byte[] bytes = StringUtil.fromHexs(dataStr);

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));

        CmdRespPacket p = new CmdRespPacketParser().parse(packet);
        Assert.assertNotNull(p);
        Assert.assertEquals(0x0A, p.getPacketType());
        Assert.assertEquals(7, p.getDeviceType());
        Assert.assertEquals(3, p.getVersion());
        Assert.assertEquals(5, p.getTerminalId());
        Assert.assertEquals(1, p.getSourceSequence());
        Assert.assertEquals(0x0012, p.getOrderId());
        Assert.assertEquals(0x01, p.getFeedback());
    }
}
