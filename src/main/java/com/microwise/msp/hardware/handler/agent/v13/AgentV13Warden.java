package com.microwise.msp.hardware.handler.agent.v13;

import com.microwise.msp.hardware.handler.agent.AbstractAgentWarden;
import com.microwise.msp.hardware.handler.agent.DeviceAgent;
import com.microwise.msp.hardware.handler.agent.ThreadWorkers;
import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v13.DefaultPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 1.3 协议
 *
 * @author gaohui
 * @date 13-8-12 12:37
 */
@Component("agentV13Warden")
@Scope("prototype")
public class AgentV13Warden extends AbstractAgentWarden {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ThreadWorkers threadWorkers;

    public void onPacketReceived(Packet packet) {
        // 0x01 数据包, 0x02 中继路由, 0x07 网关心跳
        dispatchToDevice(packet);
    }

    /**
     * 分发到设备, 例如上行请求包
     */
    private void dispatchToDevice(Packet packet) {
        DeviceAgent deviceAgent = null;
        int terminalId = -1;
        int deviceType = -1;
        if (packet.getPacketType() == Packets.V1_DATA) {
            DataPacket dataPacket = (DataPacket) packet;
            deviceType = dataPacket.getDeviceType();
            terminalId = dataPacket.getSelfId();
        } else if (packet.getPacketType() == Packets.V1_RELAY) {
            DefaultPacket p = (DefaultPacket) packet;
            deviceType = 2;
            terminalId = p.getSelfId();
        } else if (packet.getPacketType() == Packets.V1_GATEWAY) {
            DefaultPacket p = (DefaultPacket) packet;
            deviceType = 7;
            terminalId = p.getSelfId();
        }

        if (terminalId == -1 || deviceType == -1) {
            return;
        }

        //1.如果不存在此设备对应的 agent, 初始化对应的 deviceAgent, 并分配对应的执行线程(以后所有的操作都在此线程上)
        deviceAgent = getOrInitDeviceAgent(deviceType, terminalId);

        //2.如果已存在，则执行对应的操作在指定的线程
        execute(terminalId, deviceAgent, packet);
    }

    @Override
    protected ThreadWorkers getThreadWorkers() {
        return threadWorkers;
    }

    @Override
    protected ApplicationContext getAppContext() {
        return appContext;
    }

    @Override
    protected Class<? extends DeviceAgent> deviceAgentClassOfType(int deviceType) {
        switch (deviceType) {
            case 1:
            case 3:
            case 4:
                return NodeAgent.class;

            case 2:
                return RelayAgent.class;

            case 7:
                return GatewayAgent.class;
            default:
                throw new IllegalStateException("未知设备类型: " + deviceType);
        }
    }
}
