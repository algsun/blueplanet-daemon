package com.microwise.msp.hardware.handler.agent.v13;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.NodeService;
import com.microwise.msp.hardware.businessservice.SensorService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.DataPacketUtil;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v13.DataV13Packet;
import com.microwise.msp.hardware.handler.codec.v13.V13Packets;
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
@Component("v13NodeAgent")
@Scope("prototype")
public class NodeAgent extends DefaultAgent {
    public static final Logger log = LoggerFactory.getLogger(NodeAgent.class);

    @Autowired
    private AppCache appCache;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SensorService sensorService;

    @Override
    public void onPacketReceived(Packet packet) {
        DataV13Packet defaultPacket = (DataV13Packet) packet;
        if (isSamePacket(defaultPacket)) {
            Packets.log.error("[{}:{}] XXX 重复包", packet.getRemoteHost(), packet.getRemotePort());
            return;
        }
        sequence = defaultPacket.getSequence();

        DeviceBean deviceBean = V13Packets.fromPacket((DataV13Packet) packet);
        DataPacketUtil.assembleSensor(deviceBean, (DataPacket) packet);
        DataPacketUtil.deriveSensor(deviceBean, sensorService);

        DeviceBean cachedDevice = appCache.loadDevice(deviceBean.deviceid);
        if (cachedDevice != null) {
            // 使用缓存的工作周期(1.3协议工作周期不会随意改变, 也不会上工作周期. 读取数据库的周期)
            deviceBean.interval = cachedDevice.interval;
        }

        nodeService.process(deviceBean);
    }
}
