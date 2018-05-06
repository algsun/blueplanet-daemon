package com.microwise.msp.hardware.handler.codec;

import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.handler.codec.v30.DataPacketV30Parser;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-8-5 18:21
 */
public class DataPacketV30ParserTest {

    @Test
    public void testParseTimestamp() {
        byte[] bytes = new byte[]{13, 8, 5, 12, 34, 56};

        Timestamp timestamp = DataPacketV30Parser.parseTimestamp(ByteBuffer.wrap(bytes));

        DateTime datetime = DateTime.now().withMillis(timestamp.getTime());
        Assert.assertEquals(2013, datetime.getYear());
        Assert.assertEquals(8, datetime.getMonthOfYear());
        Assert.assertEquals(5, datetime.getDayOfMonth());
        Assert.assertEquals(12, datetime.getHourOfDay());
        Assert.assertEquals(34, datetime.getMinuteOfHour());
        Assert.assertEquals(56, datetime.getSecondOfMinute());
    }

    @Test
    public void testParseTimestampParseError() {
        // 76 秒
        byte[] bytes = new byte[]{13, 8, 5, 12, 34, 76};

        Timestamp timestamp = DataPacketV30Parser.parseTimestamp(ByteBuffer.wrap(bytes));
        Assert.assertNull(timestamp);
    }

    @Test
    public void testIsParseable() {
        Packet packet = new Packet();
        packet.setPacketType(Defines.PACKAGE_DATA);
        packet.setVersion(Defines.VERSION_3);

        PacketParser packetParser = new DataPacketV30Parser();
        Assert.assertTrue(packetParser.isParseable(packet));
    }

    @Test
    public void testParse() {
        String dataStr = "55AA01010351010101303A0105C50100292739002A00A7003000D6003100000021001300200018003D049A002F0000002C0064003C0064A0010D08080B0C2EA0000258A00200BC614EA0040101A0050101A00601019DDC";

        byte[] bytes = StringUtil.fromHexs(dataStr);


        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        Packet packet = packetSplitter.split(ByteBuffer.wrap(bytes));
        Assert.assertNotNull(packet);

        PacketParser<DataV30Packet> dataPacketParser = new DataPacketV30Parser();
        Assert.assertTrue(dataPacketParser.isParseable(packet));

        Packet p = dataPacketParser.parse(packet);
        Assert.assertNotNull(p);
        Assert.assertTrue(p instanceof DataV30Packet);

        DataV30Packet dataPacket = (DataV30Packet) p;
        Assert.assertEquals(1, dataPacket.getPacketType());
        Assert.assertEquals(1, dataPacket.getDeviceType());
        Assert.assertEquals(3, dataPacket.getVersion());

        Assert.assertEquals(81, dataPacket.getBodyLength());
        Assert.assertEquals(1, dataPacket.getControl());
        Assert.assertEquals(257, dataPacket.getParentId());
        Assert.assertEquals(14896, dataPacket.getSelfId());
        Assert.assertEquals(1, dataPacket.getSequence());
        Assert.assertEquals(5, dataPacket.getVoltage());
        Assert.assertEquals(-59, dataPacket.getRssi());
        Assert.assertEquals(1, dataPacket.getLqi());

        Map<Integer, Double> params = dataPacket.getSensors();
        Assert.assertNotNull(params);
        Assert.assertTrue(!params.isEmpty());

        Assert.assertEquals(10, params.size());


        Assert.assertEquals("12345678", dataPacket.getSiteId());
        Assert.assertEquals(0, dataPacket.getWorkMode());
        Assert.assertEquals("2013-08-08 11:12:46.0", dataPacket.getTimestamp().toString());
        Assert.assertEquals(600, dataPacket.getInterval());
        Assert.assertEquals(1, dataPacket.getPaginateCount());
        Assert.assertEquals(1, dataPacket.getPaginateIndex());
        Assert.assertTrue(dataPacket.isConnectionCountExists());
        Assert.assertEquals(0x0101, dataPacket.getConnectionCount());
    }

    @Test
    public void testIsSensorParam() {
        Assert.assertTrue(DataPacketV30Parser.isSensorParam(0x0000));
        Assert.assertTrue(DataPacketV30Parser.isSensorParam(0x2FFF));

        Assert.assertFalse(DataPacketV30Parser.isSensorParam(0x3000));
    }

    @Test
    public void testIsTerminalParam() {
        Assert.assertTrue(DataPacketV30Parser.isTerminalParam(0xA000));
        Assert.assertTrue(DataPacketV30Parser.isTerminalParam(0xBFFF));

        Assert.assertFalse(DataPacketV30Parser.isTerminalParam(0x9FFF));
        Assert.assertFalse(DataPacketV30Parser.isTerminalParam(0xC000));
    }

    @Test
    public void testParseGps() {
        String number = "123.456";
        byte[] bytes = number.getBytes();
        Assert.assertEquals(7, bytes.length);

        // 1, 4, '1234'
        byte[] gpsParams = new byte[]{0x01, 0x04, 0x31, 0x32, 0x33, 0x34};

        List<DataV30Packet.GpsParam> params =  DataPacketV30Parser.parseGpsParam(ByteBuffer.wrap(gpsParams));
        Assert.assertEquals(1, params.size());

        DataV30Packet.GpsParam param = params.get(0);
        Assert.assertEquals(1, param.getType());
        Assert.assertEquals(4, param.getLength());
        Assert.assertEquals("1234", param.getValue());
    }


}
