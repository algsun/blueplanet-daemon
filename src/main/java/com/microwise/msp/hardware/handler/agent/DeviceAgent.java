package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 设备代理，处理此代理负责为真实的设备服务
 *
 * <p/>
 * 为了避免线程安全问题，承诺此类的方法始终在同一个线程下调用，至死不渝。所以此类的状态不用刻意考虑线程安全。
 *
 * @author gaohui
 * @date 13-8-10 14:40
 */
public abstract class DeviceAgent {

    public abstract void onPacketReceived(Packet packet);
}
