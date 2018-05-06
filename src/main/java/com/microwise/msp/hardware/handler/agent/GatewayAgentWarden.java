package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Versions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 每个网关的守护者
 *
 * @author gaohui
 * @date 13-8-12 14:18
 */
@Component
@Scope("prototype")
public class GatewayAgentWarden implements AgentWarden{

    @Qualifier("agentV13Warden")
    @Autowired
    private AgentWarden agentV13Warden;

    @Qualifier("agentV30Warden")
    @Autowired
    private AgentWarden agentV30Warden;

    @Override
    public void onPacketReceived(Packet packet) {
        if (packet.getVersion() == Versions.V_1) {
            // 0x01 数据包, 0x02 中继路由, 0x07 网关心跳
            agentV13Warden.onPacketReceived(packet);

        } else if (packet.getVersion() == Versions.V_3) {
            // 0x01 数据包, 0x05 请求包, 0x0A 命令应答包
            agentV30Warden.onPacketReceived(packet);
        }
    }
}
