package com.microwise.msp.hardware.handler.codec;

import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 *
 * 包含 v1.3 与 v3.0 协议的拆分器
 *
 * @author gaohui
 * @date 13-8-2 16:22
 */
public class MultiVersionPacketSplitter implements PacketSplitter {
    public static final Logger log = LoggerFactory.getLogger(MultiVersionPacketSplitter.class);
    public static final Logger pLog = Packets.log;

    @Override
    public Packet split(ByteBuffer buf) {
        try {
            while (buf.hasRemaining()) {
                // 包的起始位置 (0x55)
                int startOffset = buf.position();
                // 包头不满足
                if (!hasdHead(buf)) {
                    continue;
                }

                // 协议包类型
                int packageType = getPackageType(buf, startOffset);

                // 版本不满足
                if (!hasVersion(buf, startOffset)) {
                    break;
                }

                // 协议版本
                int version = getVersion(buf, startOffset);
                byte[] packetData = null;
                if (version == Versions.V_1) {
                    packetData = version13PacketData(buf, startOffset);
                } else if (version == Versions.V_3) {
                    packetData = version30PacketData(buf, startOffset);
                }
                Packet packet = new Packet();
                packet.setPacketType(packageType);
                packet.setVersion(version);
                packet.setPacket(packetData);

                return packet;
            }
        } catch (CouldNotParsedException e) {
            pLog.error("XXX 拆包异常: {}, {}", e.getMessage(), StringUtil.toHex(buf.array()));
            return null;
        }

        return null;
    }

    /**
     * 是否有固定包头(0x55AA)
     *
     * @param buf
     * @return
     */
    private static boolean hasdHead(ByteBuffer buf) {
        if (buf.remaining() < 2) {
            throw new CouldNotParsedException("包头不够两字节");
        }

        int position = buf.position();

        // 包头
        byte head0 = buf.get();
        byte head1 = buf.get();
        if (!((head0 & 0xFF) == 0x55 && (head1 & 0xFF) == 0xAA)) {
            buf.position(position + 1);
            return false;
        }

        return true;
    }

    /**
     * 返回包协议类型
     *
     * @param buf
     * @param offset
     * @return
     */
    private static int getPackageType(ByteBuffer buf, int offset){
        if(!buf.hasRemaining()){
            throw new CouldNotParsedException("没有协议包类型");
        }

        return buf.get() & 0xFF;
    }

    /**
     * 是否包含版本字段
     *
     * @param buf
     * @param offset
     * @return
     */
    private static boolean hasVersion(ByteBuffer buf, int offset) {
        if (buf.remaining() < 3) {
            throw new CouldNotParsedException("没有版本字段");
        }

        return true;
    }

    /**
     * 返回数据包的协议版本
     *
     * @param buf
     * @param offset
     * @return
     */
    private static int getVersion(ByteBuffer buf, int offset) {
        int preVersion = buf.get(offset + 3) & 0xFF;
        int version = (buf.get(offset + 4) & 0xFF);
        if (preVersion == 0 && version == 0x01) {
            return Versions.V_1;
        }


        if (version == 0x03) {
            return Versions.V_3;
        }

        throw new CouldNotParsedException("未知协议版本");
    }

    /**
     * 解析 1.3 协议, 返回完成数据包
     *
     * @param buf
     * @param offset
     * @return
     */
    private static byte[] version13PacketData(ByteBuffer buf, int offset){
        // 修正读取位置
        buf.position(offset + 5);

        if(!buf.hasRemaining()){
            throw new CouldNotParsedException("没有包长");
        }

        // 包长
        int length = buf.get() & 0xFF;
        if((buf.limit() - offset) < 6 + length){
            throw new CouldNotParsedException("包内容不够包长");
        }

        // 整个包
        byte[] packetData = new byte[ 6 + length];
        buf.position(offset);
        buf.get(packetData);
        return packetData;
    }

    /**
     * 解析 v3.0 协议，返回完整数据包
     *
     * @param buf
     * @param offset
     * @return
     */
    private static byte[] version30PacketData(ByteBuffer buf, int offset){
        // 修正读取位置
        buf.position(offset + 5);

        if(!buf.hasRemaining()){
            throw new CouldNotParsedException("没有包长字段");
        }

        // 内容包长
        int bodyLength = buf.get() & 0xFF;
        if((buf.limit() - offset) < 6 + bodyLength){
            throw new CouldNotParsedException("包内容不够包长");
        }

        // 整个包
        byte[] packetData = new byte[6 + bodyLength];
        buf.position(offset);
        buf.get(packetData);
        return packetData;
    }

    /**
     * 不能解析包异常
     */
    private static class CouldNotParsedException extends RuntimeException {
        private CouldNotParsedException() {
        }

        private CouldNotParsedException(String message) {
            super(message);
        }

        private CouldNotParsedException(String message, Throwable cause) {
            super(message, cause);
        }

        private CouldNotParsedException(Throwable cause) {
            super(cause);
        }
    }
}
