package com.microwise.msp.hardware.handler.agent.v30;

import com.microwise.msp.hardware.handler.agent.AbstractAgentWarden;
import com.microwise.msp.hardware.handler.agent.DeviceAgent;
import com.microwise.msp.hardware.handler.agent.ThreadWorkers;
import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.Devices;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v30.CmdRespPacket;
import com.microwise.msp.hardware.handler.codec.v30.StatusPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * v3 协议
 *
 * @author gaohui
 * @date 13-8-12 12:38
 */
@Component("agentV30Warden")
@Scope("prototype")
public class AgentV30Warden extends AbstractAgentWarden {
    public static final Logger log = LoggerFactory.getLogger(AgentV30Warden.class);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ThreadWorkers threadWorkers;

    private RequestService requestService = new RequestService();

    public void onPacketReceived(Packet packet) {
        // 0x01 数据包, 0x05 请求包, 0x0A 命令应答包
        switch (packet.getPacketType()) {
            case Packets.UP_DATA:
            case Packets.UP_COMMAND_ACK:
            case Packets.UP_STATUS:
                dispatchToDevice(packet);
                break;

            case Packets.UP_REQUEST:
                dispatchToService(packet);
                break;
            default:
                log.error("未知包类型: {}", packet);
                break;
        }
    }

    /**
     * 分发到服务, 与设备无关的(例如请求授时)
     */
    private void dispatchToService(Packet packet) {
        if (packet.getPacketType() == Packets.UP_REQUEST) {
            requestService.service(packet);
        }
    }

    /**
     * 分发到设备, 例如上行请求包
     */
    private void dispatchToDevice(Packet packet) {
        // 只有 0x01, 0x0A 协议有设备ID

        //1.如果不存在此设备对应的 agent, 初始化对应的 deviceAgent, 并分配对应的执行线程(以后所有的操作都在此线程上)
        DeviceAgent deviceAgent = null;
        if (packet.getPacketType() == Packets.UP_DATA) {
            DataPacket dataPacket = (DataPacket) packet;
            deviceAgent = getOrInitDeviceAgent(dataPacket.getDeviceType(), dataPacket.getSelfId());

            //2.如果已存在，则执行对应的操作在指定的线程
            execute(dataPacket.getSelfId(), deviceAgent, packet);
        } else if (packet.getPacketType() == Packets.UP_COMMAND_ACK) {
            CmdRespPacket cmdRespPacket = (CmdRespPacket) packet;

            deviceAgent = getOrInitDeviceAgent(cmdRespPacket.getDeviceType(), cmdRespPacket.getTerminalId());
            execute(cmdRespPacket.getTerminalId(), deviceAgent, cmdRespPacket);
        } else if (packet.getPacketType() == Packets.UP_STATUS) {
            StatusPacket statusPacket = (StatusPacket) packet;
            deviceAgent = getOrInitDeviceAgent(statusPacket.getDeviceType(), statusPacket.getSelfId());

            execute(statusPacket.getSelfId(), deviceAgent, packet);
        }

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
            case Devices.NODE:
            case Devices.MASTER_MODULE:
            case Devices.SLAVE_MODULE:
                return NodeAgent.class;
            case Devices.CONTROL_MODULE:
                return ControlModuleAgent.class;

            case Devices.RELAY:
                return RelayAgent.class;

            case Devices.GATEWAY:
                return GatewayAgent.class;
            default:
                throw new IllegalArgumentException("未设备类型: " + deviceType);
        }
    }
}
