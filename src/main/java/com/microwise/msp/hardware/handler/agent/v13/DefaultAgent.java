package com.microwise.msp.hardware.handler.agent.v13;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.NodeService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.handler.agent.DeviceAgent;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v13.DefaultPacket;
import com.microwise.msp.hardware.handler.codec.v13.V13Packet;
import com.microwise.msp.hardware.handler.codec.v13.V13Packets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认设备代理 (网关与中继)
 *
 * @author gaohui
 * @date 13-8-17 16:42
 */
public class DefaultAgent extends DeviceAgent {
    public static final Logger log = LoggerFactory.getLogger(DefaultAgent.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private AppCache appCache;

    // 包序列号
    protected int sequence = -1;

    @Override
    public void onPacketReceived(Packet packet) {
        if (!(packet instanceof DefaultPacket)) {
            log.error("未知数据包：{}", packet);
            return;
        }

        V13Packet defaultPacket = (V13Packet) packet;
        if(isSamePacket(defaultPacket)){
            Packets.log.error("[{}:{}] XXX 重复包", packet.getRemoteHost(), packet.getRemotePort());
            return;
        }
        sequence = defaultPacket.getSequence();

        DeviceBean deviceBean = V13Packets.fromPacket((DefaultPacket) packet);

        DeviceBean cachedDevice = appCache.loadDevice(deviceBean.deviceid);
        if (cachedDevice != null) {
            // 使用缓存的工作周期(1.3协议工作周期不会随意改变, 也不会上工作周期. 读取数据库的周期)
            deviceBean.interval = cachedDevice.interval;
        }
        nodeService.process(deviceBean);
    }

    /**
     * 判断是否重复数据：当前数据包与上一包序列号相同，为重复数据
     *
     * @param packet
     * @return
     */
    protected boolean isSamePacket(V13Packet packet) {
        return packet.getSequence() == sequence;
    }
}
