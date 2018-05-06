package com.microwise.msp.hardware.handler.codec;

import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.PacketEncoder;
import com.microwise.msp.hardware.handler.codec.v30.StatusPacket;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-8-9 14:42
 */
public class PacketEncoderTest {

    @Test
    public void testWrapHeadAndCrc() {
        byte[] data = PacketEncoder.wrapHeadAndCRC(2, 1, 3, new byte[]{});
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals(8 * 2, hexes.length());
        Assert.assertEquals("55AA020103024FC5", hexes);
    }

    @Test
    public void testEncodeDataAck() {
        byte[] data = PacketEncoder.encodeDataAck(1, 12345, 3, 0xABCD);
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals(13 * 2, hexes.length());
        Assert.assertEquals("55AA02010307393003ABCD3007", hexes);
    }

    @Test
    public void testEncodeDataAck2(){
        DataV30Packet packet = new DataV30Packet(new Packet());
        packet.setDeviceType(1);
        packet.setSelfId(12345);
        packet.setSequence(3);
        packet.setCrc(0xABCd);

        byte[] data = PacketEncoder.encodeDataAck(packet);
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals(13 * 2, hexes.length());
        Assert.assertEquals("55AA02010307393003ABCD3007", hexes);
    }

    @Test
    public void testEncodeStatusAck(){
        byte[] data = PacketEncoder.encodeStatusAck(1, 0x0086, 4, 0xABCD);
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals(13 * 2, hexes.length());
        Assert.assertEquals("55AA04010307860004ABCD8A87", hexes);
    }

    @Test
    public void testEncodeStatusAck2(){
        StatusPacket packet = new StatusPacket(new Packet());
        packet.setDeviceType(1);
        packet.setSelfId(0x0086);
        packet.setSequence(4);
        packet.setCrc(0xABCD);

        byte[] data = PacketEncoder.encodeStatusAck(packet);
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals(13 * 2, hexes.length());
        Assert.assertEquals("55AA04010307860004ABCD8A87", hexes);
    }

    @Test
    public void testEncodeRequestAck() {
        byte[] data = PacketEncoder.encodeRequestAck(1, 0x02, 3, new byte[]{0x13, 0x08, 0x09, 0x16, 0x58, 0x23});
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals("55AA0601030A02031308091658235E2D", hexes);

        data = PacketEncoder.encodeRequestAck(1, 0x01, 3, null);
        hexes = StringUtil.toHex(data);
        Assert.assertEquals("55AA060103040103B0B2", hexes);
    }

    @Test
    public void testEncodeCommand() {
        byte[] data = PacketEncoder.encodeCommand(1, 12345, 1, 0, null, 0x0012, new byte[]{});
        String hexes = StringUtil.toHex(data);
        Assert.assertEquals("55AA09010308393001000012E25B", hexes);
    }

    @Test
    public void testTimestampNow() {
        DateTime now = DateTime.now();
        String s = StringUtil.toHex(PacketEncoder.timestampNow());
        Assert.assertNotNull(s);
        byte[] data = StringUtil.fromHexs(s);

        Assert.assertEquals(6, data.length);
        Assert.assertEquals(now.getYearOfCentury(), data[0]);
        Assert.assertEquals(now.getMonthOfYear(), data[1]);
        Assert.assertEquals(now.getDayOfMonth(), data[2]);
        Assert.assertEquals(now.getHourOfDay(), data[3]);
        Assert.assertEquals(now.getMinuteOfHour(), data[4]);
        // 秒不能保证准确,  跳过
    }
}
