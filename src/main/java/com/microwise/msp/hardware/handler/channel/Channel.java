package com.microwise.msp.hardware.handler.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 通道. 不同于 netty 的 channel， 这里的 channel 是对 netty channel 的一个简单地封装, 避免程序过多的使用 netty api。
 *
 * @author bastengao
 * @date 14-4-8 下午2:18
 */
public interface Channel {
    int TYPE_UDP = 2;
    int TYPE_TCP = 3;


    /**
     * 返回通道类型
     *
     * @return
     */
    int type();

    /**
     * 返回本地端口
     *
     * @return
     */
    InetSocketAddress localAddress();

    /**
     * 发送数据
     *
     * @param bytes
     */
    void write(ByteBuffer bytes);

    void write(byte[] bytes);

    void write(List<Byte> bytes);

}
