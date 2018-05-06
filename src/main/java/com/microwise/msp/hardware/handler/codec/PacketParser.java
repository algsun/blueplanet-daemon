package com.microwise.msp.hardware.handler.codec;

/**
 * 包解析
 *
 * @author gaohui
 * @date 13-8-2 15:48
 */
public interface PacketParser<T extends Packet> {

    /**
     * 是否可解析
     *
     * @param packet
     * @return
     */
    boolean isParseable(Packet packet);

    /**
     * 解析结果
     *
     * @param packet
     * @return
     */
    T parse(Packet packet);
}
