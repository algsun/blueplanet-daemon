package com.microwise.msp.hardware.handler.codec.v30;

import com.google.common.primitives.Bytes;
import com.microwise.msp.util.CRCUtil;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * 数据包编码器
 * v30 协议
 *
 * @author gaohui
 * @date 13-8-9 14:26
 */
public class PacketEncoder {

    /**
     * 数据应答包(0x02) 编码
     *
     * @param packet
     * @return
     */
    public static byte[] encodeDataAck(DataV30Packet packet) {
        int selfId = packet.getSelfId();
        if (packet.isNetIdExists()) {
            selfId = Integer.parseInt(StringUtil.fillZero(selfId, 5).substring(2));
        }
        return PacketEncoder.encodeDataAck(
                packet.getDeviceType(),
                selfId,
                packet.getSequence(),
                packet.getCrc()
        );
    }

    /**
     * 数据应答包(0x02) 编码
     *
     * @param deviceType 设备类型
     * @param terminalId 终端ID
     * @param sequence   源包序列号
     * @param originCrc  源包CRC
     * @return
     */
    public static byte[] encodeDataAck(int deviceType, int terminalId, int sequence, int originCrc) {
        byte[] data = ByteBuffer.allocate(5)
                // 终端ID 两字节, 低位在前，高位在后 @gaohui 2013-08-09
                .put((byte) terminalId)
                .put((byte) (terminalId >> 8))
                .put((byte) sequence)
                .putShort((short) originCrc)
                .array();


        return wrapHeadAndCRC(0x02, deviceType, 0x03, data);
    }

    /**
     * 状态应答包(0x04) 编码
     *
     * @param statusPacket
     * @return
     */
    public static byte[] encodeStatusAck(StatusPacket statusPacket) {
        return PacketEncoder.encodeStatusAck(statusPacket.getDeviceType(),
                statusPacket.getSelfId(),
                statusPacket.getSequence(),
                statusPacket.getCrc());
    }

    /**
     * 状态应答包(0x04) 编码
     *
     * @param deviceType
     * @param terminalId
     * @param sequence
     * @param originCrc
     * @return
     */
    public static byte[] encodeStatusAck(int deviceType, int terminalId, int sequence, int originCrc) {
        byte[] data = ByteBuffer.allocate(5)
                // 终端ID 两字节, 低位在前，高位在后 @gaohui 2013-08-09
                .put((byte) terminalId)
                .put((byte) (terminalId >> 8))
                .put((byte) sequence)
                .putShort((short) originCrc)
                .array();


        return wrapHeadAndCRC(0x04, deviceType, 0x03, data);
    }

    /**
     * 请求应答包(0x06) 编码
     *
     * @param deviceType 设备类型
     * @param orderId    指令标识
     * @param sequence   包序列号
     * @param orderValue 指令参数
     * @return
     */
    public static byte[] encodeRequestAck(int deviceType, int orderId, int sequence, byte[] orderValue) {
        int orderValueLength = 0;
        if (orderValue != null) {
            orderValueLength = orderValue.length;
        }

        ByteBuffer buf = ByteBuffer.allocate(2 + orderValueLength)
                .put((byte) orderId)
                .put((byte) sequence);
        if (orderValue != null) {
            buf.put(orderValue);
        }

        return wrapHeadAndCRC(0x06, deviceType, 0x03, buf.array());
    }

    /**
     * 下行命令(0x09) 编辑
     *
     * @param deviceType 设备类型
     * @param terminalId 终端ID
     * @param sequence   包序列号
     * @param jump       路由跳数
     * @param routes     路由链路 (网关到终端)
     * @param orderId    指令标识
     * @param orderValue 指令参数
     * @return
     */
    public static byte[] encodeCommand(
            int deviceType,
            int terminalId,
            int sequence,
            int jump,
            int[] routes,
            int orderId,
            byte[] orderValue) {


        int routesLength = 0;
        if (routes != null) {
            routesLength = routes.length;
        }

        ByteBuffer buf = ByteBuffer.allocate(4 + routesLength * 2 + 2 + orderValue.length)
                .put((byte) terminalId)
                .put((byte) (terminalId >> 8))
                .put((byte) sequence)
                .put((byte) jump);

        if (routes != null) {
            for (int route : routes) {
                buf.put((byte) route);
                buf.put((byte) (route >> 8));
            }
        }

        buf.putShort((short) orderId);
        buf.put(orderValue);

        return wrapHeadAndCRC(0x09, deviceType, 0x03, buf.array());
    }


    /**
     * 封装包头与包尾CRC
     *
     * @param packetBody
     * @return
     */
    public static byte[] wrapHeadAndCRC(int packageType, int deviceType, int version, byte[] packetBody) {
        // bodyLength
        int bodyLength = packetBody.length;

        ByteBuffer buf = ByteBuffer.allocate(6 + bodyLength + 2);
        buf.putShort((short) 0x55AA);
        buf.put((byte) packageType);
        buf.put((byte) deviceType);
        buf.put((byte) version);
        buf.put((byte) (bodyLength + 2));
        buf.put(packetBody);

        // 计算 CRC
        byte[] withoutCrc = new byte[6 + bodyLength];
        buf.rewind();
        buf.get(withoutCrc);

        int crc = CRCUtil.generateCRC(Bytes.asList(withoutCrc));
        buf.putShort(buf.limit() - 2, (short) crc);

        return buf.array();
    }

    /**
     * 当前时间缀
     *
     * @return
     */
    public static byte[] timestampNow() {
        DateTime now = DateTime.now();
        return ByteBuffer.allocate(6)
                .put((byte) now.getYearOfCentury())
                .put((byte) now.getMonthOfYear())
                .put((byte) now.getDayOfMonth())
                .put((byte) now.getHourOfDay())
                .put((byte) now.getMinuteOfHour())
                .put((byte) now.getSecondOfMinute())
                .array();
    }
}
