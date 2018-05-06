package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.codec.Packet;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 基于  ThreadWorkers 的 AgentWarden，执行每次包请求在独立的线程中
 *
 * @author gaohui
 * @date 13-8-17 08:29
 */
public abstract class AbstractAgentWarden implements AgentWarden {

    /**
     * terminalId => workId 的映射
     */
    private ConcurrentMap<Integer, Integer> workIds = new ConcurrentHashMap<Integer, Integer>();

    /**
     * 网关对应网关下所以的设备
     * <p/>
     * terminalId(deviceId/selfId) => deviceAgent(DeviceAgent)
     */
    private ConcurrentMap<Integer, DeviceAgent> devices = new ConcurrentHashMap<Integer, DeviceAgent>();


    protected abstract ThreadWorkers getThreadWorkers();

    protected abstract ApplicationContext getAppContext();

    protected abstract Class<? extends DeviceAgent> deviceAgentClassOfType(int deviceTye);

    protected void execute(int terminalId, DeviceAgent deviceAgent, Packet packet) {
        getThreadWorkers().execute(
                workIds.get(terminalId),
                new DeviceInvokeContext(deviceAgent, packet)
        );
    }

    protected DeviceAgent getOrInitDeviceAgent(int deviceType, int terminalId) {
        DeviceAgent deviceAgent = devices.get(terminalId);
        if (deviceAgent == null) {
            deviceAgent = initDeviceAgent(deviceType);
            devices.putIfAbsent(terminalId, deviceAgent);
            deviceAgent = devices.get(terminalId);
            workIds.putIfAbsent(terminalId, getThreadWorkers().newWorkId());
        }

        return deviceAgent;
    }

    /**
     * 初始化 DeviceAgent
     *
     * @param deviceType
     * @return
     */
    protected DeviceAgent initDeviceAgent(int deviceType) {
        return getAppContext().getBean(deviceAgentClassOfType(deviceType));
    }

}
