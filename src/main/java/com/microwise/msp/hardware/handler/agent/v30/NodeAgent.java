package com.microwise.msp.hardware.handler.agent.v30;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.NodeService;
import com.microwise.msp.hardware.businessservice.SensorService;
import com.microwise.msp.hardware.handler.codec.DataPacketUtil;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * 节点代理(传感器)
 *
 * @author gaohui
 * @date 13-8-10 14:42
 */
@Component("v30NodeAgent")
@Scope("prototype")
public class NodeAgent extends DefaultAgent {
    public static final Logger log = LoggerFactory.getLogger(NodeAgent.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SensorService sensorService;

    /**
     * 当上行数据
     *
     * @param packet
     */
    @Override
    protected void onDataReady(DataV30Packet packet) {
        DeviceBean deviceBean = DataV30Packets.fromPacket(packet);
        DataPacketUtil.assembleSensor(deviceBean, packet);
        DataPacketUtil.deriveSensor(deviceBean, sensorService);
        siteId = deviceBean.siteId;

        nodeService.process(deviceBean);
    }
}
