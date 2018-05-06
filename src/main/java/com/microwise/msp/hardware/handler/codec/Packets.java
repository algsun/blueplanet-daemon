package com.microwise.msp.hardware.handler.codec;

import com.google.common.collect.Sets;
import com.google.common.primitives.Bytes;
import com.microwise.msp.hardware.handler.codec.v13.V13Packet;
import com.microwise.msp.hardware.handler.codec.v30.*;
import com.microwise.msp.util.CRCUtil;
import com.microwise.msp.util.MergeUtil;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * 协议类型常量
 *
 * @author gaohui
 * @date 13-8-10 10:30
 */
public class Packets {

    // 数据包日志
    public static final Logger log = LoggerFactory.getLogger(Packets.class);

    private Packets() {
    }

    // v1.3 协议包类型
    /**
     * 传感数据包
     */
    public static final int V1_DATA = 0x01;
    /**
     * 中继路由包
     */
    public static final int V1_RELAY = 0x02;
    /**
     * 网关心跳包
     */
    public static final int V1_GATEWAY = 0x07;


    /**
     * 上行数据
     */
    public static final int UP_DATA = 0x01;
    /**
     * 数据应答
     */
    public static final int DOWN_DATA_ACK = 0x02;

    /**
     * 上行设备状态包
     */
    public static final int UP_STATUS = 0x03;

    /**
     * 设备状态应答包
     */
    public static final int DOWN_STATUS_ACK = 0x04;

    /**
     * 上行请求
     */
    public static final int UP_REQUEST = 0x05;
    /**
     * 请求应答
     */
    public static final int DOWN_REQUEST_ACK = 0x06;
    /**
     * 下行命令
     */
    public static final int DOWN_COMMAND = 0x09;
    /**
     * 命令应答
     */
    public static final int UP_COMMAND_ACK = 0x0A;

    /**
     * V3 目前的设备类型
     */
    public static final Set<Integer> V30_DEVICE_TYPES = Collections.unmodifiableSet(Sets.newHashSet(1, 2, 3, 4, 5, 7));

    /**
     * 将 packet 基本信息复制到 另一个 packet
     *
     * @param srcPacket
     * @param targetPacket
     */
    public static void copy(Packet srcPacket, Packet targetPacket) {
        targetPacket.setChannel(srcPacket.getChannel());
        targetPacket.setChannelType(srcPacket.getChannelType());
        targetPacket.setPacketType(srcPacket.getPacketType());
        targetPacket.setVersion(srcPacket.getVersion());
        targetPacket.setRemote(srcPacket.getRemote());
        targetPacket.setPacket(srcPacket.getPacket());
    }

    public static void logData(DataPacket packet) {
        log.info(String.format("[%s:%d] <= 上行数据[01]-%d-%05d: %s", packet.getRemoteHost(), packet.getRemotePort(), packet.getDeviceType(), packet.getSelfId(), StringUtil.toHex(packet.getPacket())));
    }

    public static void logDataAck(DataPacket packet, byte[] ackData) {
        log.info(String.format("[%s:%d] => 应答数据[02]-%d-%05d: %s", packet.getRemoteHost(), packet.getRemotePort(), packet.getDeviceType(), packet.getSelfId(), StringUtil.toHex(ackData)));
    }

    public static void logV13(V13Packet packet) {
        if (packet.getPacketType() == Packets.V1_DATA) {
            logData((DataPacket) packet);
        } else if (packet.getPacketType() == Packets.V1_RELAY) {
            log.info("[{}:{}] <= 中继路由[02]-{}-{}: {}",
                    packet.getRemoteHost(),
                    packet.getRemotePort(),
                    packet.getDeviceType(),
                    packet.getSelfId(),
                    StringUtil.toHex(packet.getPacket())
            );
        } else if (packet.getPacketType() == Packets.V1_GATEWAY) {
            log.info("[{}:{}] <= 网关心跳[07]-{}-{}: {}",
                    packet.getRemoteHost(),
                    packet.getRemotePort(),
                    packet.getDeviceType(),
                    packet.getSelfId(),
                    StringUtil.toHex(packet.getPacket())
            );
        }
    }

    public static void logV30UpData(DataV30Packet packet) {
        logData(packet);
    }

    public static void logV30DownDataAck(DataPacket packet, byte[] ackData) {
        logDataAck(packet, ackData);
    }

    public static void logV30UpStatus(StatusPacket packet){
        log.info("[{}:{}] <= 设备状态[03]-{}-{}: {}",
                packet.getRemoteHost(),
                packet.getRemotePort(),
                packet.getDeviceType(),
                String.format("%05d", packet.getSelfId()),
                StringUtil.toHex(packet.getPacket())
                );
    }

    public static void logV30DownStatusAck(StatusPacket packet, byte[] ackData){
        log.info("[{}:{}] => 应答状态[04]-{}-{}: {}",
                packet.getRemoteHost(),
                packet.getRemotePort(),
                packet.getDeviceType(),
                String.format("%05d", packet.getSelfId()),
                StringUtil.toHex(ackData)
        );
    }

    public static void logV30UpRequest(RequestPacket packet) {
        log.info("[{}:{}] <= 上行请求[05]-{}-     : order {}[{}] {}",
                packet.getRemoteHost(),
                packet.getRemotePort(),
                packet.getDeviceType(),
                packet.getOrderId(),
                Requests.requestName(packet.getOrderId()),
                StringUtil.toHex(packet.getPacket()
                ));
    }

    public static void logV30DownRequestAck(RequestPacket packet, byte[] ackData) {
        log.info("[{}:{}] => 应答请求[06]-{}-     : order {}[{}] {}",
                packet.getRemoteHost(),
                packet.getRemotePort(),
                packet.getDeviceType(),
                packet.getOrderId(),
                Requests.requestName(packet.getOrderId()),
                StringUtil.toHex(ackData)
        );
    }

    public static void logV30DownCommandAck(Packet packet) {
        CmdRespPacket respPacket = (CmdRespPacket) packet;
        log.info("[{}:{}] <= 命令响应[0A]-{}-{}: cmd {}, result {}, {}",
                packet.getRemoteHost(),
                packet.getRemotePort(),
                respPacket.getDeviceType(),
                String.format("%05d", respPacket.getTerminalId()),
                Commands.cmdName(respPacket.getOrderId()),
                Commands.feedbackName(respPacket.getFeedback()),
                StringUtil.toHex(packet.getPacket()));
    }

    /**
     * 检查 CRC
     *
     * @param packet
     * @return
     */
    public static boolean checkCRC(Packet packet) {
        int crc = CRCUtil.getCRC(Bytes.asList(packet.getPacket()));


        int length = packet.getPacket().length;
        char crc1 = (char) (packet.getPacket()[length - 2] & 0xFF);
        char crc2 = (char) (packet.getPacket()[length - 1] & 0xFF);
        int crcOrigin = MergeUtil.merge2(crc1, crc2);
        return crc == crcOrigin;
    }
}

