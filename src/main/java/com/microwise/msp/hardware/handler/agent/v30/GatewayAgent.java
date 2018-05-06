package com.microwise.msp.hardware.handler.agent.v30;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packets;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 网关代理
 *
 * @author gaohui
 * @date 13-8-10 14:42
 */
@Component("v30GatewayAgent")
@Scope("prototype")
public class GatewayAgent extends DefaultAgent {

    @Override
    protected void onDataReady(DataV30Packet packet) {
        DeviceBean deviceBean = DataV30Packets.fromPacket(packet);
        DataV30Packets.parseGpsToSensors(packet, deviceBean);
        siteId = deviceBean.siteId;
        nodeService.process(deviceBean);
    }
}
