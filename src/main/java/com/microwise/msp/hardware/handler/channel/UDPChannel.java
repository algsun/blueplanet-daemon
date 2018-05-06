package com.microwise.msp.hardware.handler.channel;


import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 对 netty channel 的一个封装
 *
 * TODO write 方法是否判断 channel#isActive @gaohui 2014-04-09
 *
 * @author bastengao
 * @date 14-4-8 下午2:19
 */
public class UDPChannel implements Channel {

    private io.netty.channel.Channel _channel;
    private InetSocketAddress remoteAddress;

    public UDPChannel(io.netty.channel.Channel _channel, InetSocketAddress remoteAddress) {
        this._channel = _channel;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public int type() {
        return TYPE_UDP;
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) _channel.localAddress();
    }

    @Override
    public void write(ByteBuffer bytes) {
        DatagramPacket datagramPacket = packet(Unpooled.wrappedBuffer(bytes));
        _channel.writeAndFlush(datagramPacket);
    }

    @Override
    public void write(byte[] bytes) {
        DatagramPacket datagramPacket = packet(Unpooled.wrappedBuffer(bytes));
        _channel.writeAndFlush(datagramPacket);
    }

    @Override
    public void write(List<Byte> bytes) {
        DatagramPacket datagramPacket = packet(Unpooled.wrappedBuffer(Bytes.toArray(bytes)));
        _channel.writeAndFlush(datagramPacket);
    }

    protected DatagramPacket packet(ByteBuf data){
        return new DatagramPacket(data, remoteAddress);
    }
}
