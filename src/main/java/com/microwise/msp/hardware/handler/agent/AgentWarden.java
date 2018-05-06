package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 代理守望者. 默默的守望着代理们。
 *
 * @author gaohui
 * @date 13-8-12 12:43
 */
public interface AgentWarden {

    void onPacketReceived(Packet packet);

}
