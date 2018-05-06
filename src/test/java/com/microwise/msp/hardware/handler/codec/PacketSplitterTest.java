package com.microwise.msp.hardware.handler.codec;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author gaohui
 * @date 13-8-2 16:02
 */
public class PacketSplitterTest {
    @Test
    public void testParse13() {
        PacketSplitter parser = new MultiVersionPacketSplitter();

        // v1.3
        String datagram = "55AA010001040000ABCD";

        byte[] bytes = StringUtil.fromHexs(datagram);

        ByteBuffer buf = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
        Packet packet = parser.split(buf);
        Assert.assertNotNull(packet);
        Assert.assertEquals(Defines.VERSION_1, packet.getVersion());
        Assert.assertEquals(1, packet.getPacketType());
    }

    @Test
    public void testParse13Multiple() {
        PacketSplitter parser = new MultiVersionPacketSplitter();

        int count = 10;
        // v1.3
        String datagram = Strings.repeat("55AA010001040000ABCD", count);

        byte[] bytes = StringUtil.fromHexs(datagram);

        int times = 0;
        ByteBuffer buf = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
        while (buf.hasRemaining()) {
            Packet packet = parser.split(buf);
            Assert.assertNotNull(packet);
            Assert.assertEquals(Defines.VERSION_1, packet.getVersion());
            Assert.assertEquals(1, packet.getPacketType());
            times++;
        }

        Assert.assertEquals(count, times);
    }

    @Test
    public void testParse30() {
        PacketSplitter parser = new MultiVersionPacketSplitter();

        // v3.0
        String datagram = "55AA010103040000ABCD";

        byte[] bytes = StringUtil.fromHexs(datagram);

        Packet packet = parser.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);
        Assert.assertEquals(Defines.VERSION_3, packet.getVersion());
        Assert.assertEquals(1, packet.getPacketType());
    }

    @Test
    public void testParse30Multiple() {
        PacketSplitter parser = new MultiVersionPacketSplitter();

        int count = 10;
        // v3.0
        String datagram = Strings.repeat("55AA010103040000ABCD", count);

        byte[] bytes = StringUtil.fromHexs(datagram);

        int times = 0;
        ByteBuffer buf = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
        while (buf.hasRemaining()) {
            Packet packet = parser.split(buf);
            Assert.assertNotNull(packet);
            Assert.assertEquals(Defines.VERSION_3, packet.getVersion());
            Assert.assertEquals(1, packet.getPacketType());
            times++;
        }

        Assert.assertEquals(count, times);
    }

    @Test
    public void testParse30RequestPackageNewHead() {
        PacketSplitter parser = new MultiVersionPacketSplitter();

        // v3.0
        String datagram = "55AA050703060101FFFFF58A";

        byte[] bytes = StringUtil.fromHexs(datagram);

        Packet packet = parser.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);
        Assert.assertEquals(Defines.VERSION_3, packet.getVersion());
        Assert.assertEquals(5, packet.getPacketType());
    }
}
