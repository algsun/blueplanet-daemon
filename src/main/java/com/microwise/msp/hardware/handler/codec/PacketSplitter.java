package com.microwise.msp.hardware.handler.codec;

import java.nio.ByteBuffer;

/**
 * 负责包拆分
 *
 * @author gaohui
 * @date 13-8-2 18:58
 */
public interface PacketSplitter {

    /**
     * 根据数据包进行拆分包. 如果无法拆分返回 null
     *
     * <p>
     * 解析后，会有 version(版本) 与 packet(完成数据包). 同时 buf 的 position 将处在下一包的开始位置，如果还有数据。
     *
     * @param buf
     * @return
     */
    public Packet split(ByteBuffer buf);
}
