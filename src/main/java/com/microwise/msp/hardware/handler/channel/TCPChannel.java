package com.microwise.msp.hardware.handler.channel;

import com.google.common.primitives.Bytes;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 对 netty channel 的一个封装
 *
 * @author bastengao
 * @date 14-4-8 下午2:18
 */
public class TCPChannel implements Channel {
    private io.netty.channel.Channel _channel;

    public TCPChannel(io.netty.channel.Channel _channel) {
        this._channel = _channel;
    }

    @Override
    public int type() {
        return TYPE_TCP;
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) _channel.localAddress();
    }

    @Override
    public void write(ByteBuffer bytes) {
        _channel.writeAndFlush(Unpooled.wrappedBuffer(bytes));
    }

    @Override
    public void write(byte[] bytes) {
        _channel.writeAndFlush(Unpooled.wrappedBuffer(bytes));
    }

    @Override
    public void write(List<Byte> bytes) {
        _channel.writeAndFlush(Unpooled.wrappedBuffer(Bytes.toArray(bytes)));
    }
}
