package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Devices;
import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 中继路由包
 *
 * @author gaohui
 * @date 13-8-13 10:11
 */
public class RelayRoutePacket extends DefaultPacket {
    public RelayRoutePacket() {
        super();
        this.deviceType = Devices.RELAY;
    }

    public RelayRoutePacket(Packet packet) {
        super(packet);
        this.deviceType = Devices.RELAY;
    }
}
