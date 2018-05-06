package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gaohui
 * @date 13-8-12 13:46
 */
public class DeviceInvokeContext implements Runnable {
    public static final Logger log = LoggerFactory.getLogger(DeviceInvokeContext.class);

    private DeviceAgent deviceAgent;
    private Packet packet;

    public DeviceInvokeContext(DeviceAgent deviceAgent, Packet packet) {
        this.deviceAgent = deviceAgent;
        this.packet = packet;
    }

    private void execute() {
        deviceAgent.onPacketReceived(packet);
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Throwable e) {
            log.error("", e);
        }
    }
}

