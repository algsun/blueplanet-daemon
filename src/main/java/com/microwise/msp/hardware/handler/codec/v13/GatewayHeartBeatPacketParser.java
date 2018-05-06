package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 解析网关心跳包
 *
 * @author gaohui
 * @date 13-8-13 11:05
 */
public class GatewayHeartBeatPacketParser extends DefaultPacketParser<GatewayHeartBeatPacket> {
    @Override
    public boolean isParseable(Packet packet) {
        return (super.isParseable(packet) && packet.getPacketType() == Packets.GATEWAY_HEART_BEAT);
    }

    @Override
    GatewayHeartBeatPacket copyFrom(Packet packet) {
        return new GatewayHeartBeatPacket(packet);
    }
}
