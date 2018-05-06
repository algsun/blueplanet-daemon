package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.Devices;
import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 网关心跳包
 *
 * @author gaohui
 * @date 13-8-13 10:12
 */
public class GatewayHeartBeatPacket extends DefaultPacket {

    public GatewayHeartBeatPacket() {
        super();
        this.deviceType = Devices.GATEWAY;
    }

    public GatewayHeartBeatPacket(Packet packet) {
        super(packet);
        this.deviceType = Devices.GATEWAY;
    }
}
