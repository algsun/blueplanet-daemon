package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 解析中继路由包
 *
 * @author gaohui
 * @date 13-8-13 11:02
 */
public class RelayRoutePacketParser extends DefaultPacketParser<RelayRoutePacket> {

    @Override
    public boolean isParseable(Packet packet) {
        return (super.isParseable(packet) && packet.getPacketType() == Packets.RELAY_ROUTE);
    }

    @Override
    RelayRoutePacket copyFrom(Packet packet) {
        return new RelayRoutePacket(packet);
    }
}
