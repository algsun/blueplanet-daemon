package com.microwise.msp.hardware.handler.codec;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @author gaohui
 * @date 13-8-10 14:14
 */
public class PacketsTest {

    @Test
    public void testCopy() {
        Packet srcPacket = new Packet();
        srcPacket.setPacket(new byte[]{1, 2, 3, 4});
        srcPacket.setPacketType(Packets.UP_DATA);
        srcPacket.setVersion(Versions.V_3);
        srcPacket.setRemote(new InetSocketAddress("198.176.154.132", 8888));

        Packet targetPacket = new Packet();

        Packets.copy(srcPacket, targetPacket);

        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, srcPacket.getPacket());
        Assert.assertEquals(Packets.UP_DATA, targetPacket.getPacketType());
        Assert.assertEquals(Versions.V_3, targetPacket.getVersion());
        Assert.assertEquals("198.176.154.132", targetPacket.getRemoteHost());
        Assert.assertEquals(8888, targetPacket.getRemotePort());
    }
}
