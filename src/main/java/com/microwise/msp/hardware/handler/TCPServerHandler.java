package com.microwise.msp.hardware.handler;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.handler.channel.Channel;
import com.microwise.msp.hardware.handler.channel.TCPChannel;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.PacketEncoder;
import com.microwise.msp.hardware.handler.codec.v30.RequestPacket;
import com.microwise.msp.hardware.handler.codec.v30.StatusPacket;
import com.microwise.msp.hardware.netlink.ChannelAttributeCache;
import com.microwise.msp.hardware.netlink.TCPServer;
import com.microwise.msp.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * tcp 通信 handler,  一个客户端对应一个实例
 *
 * @author bastengao
 * @date 14-4-8 上午8:59
 */
@Component
@Scope("prototype")
public class TCPServerHandler extends AbstractServerHandler {
    public static final Logger log = LoggerFactory.getLogger(TCPServerHandler.class);
    public static final Logger pLog = Packets.log;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("tcp connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("tcp disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        ByteBuffer byteBuf = nioBuf(buf);
        buf.release();

        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();

        Packet packet = packetSplitter.split(byteBuf);
        if (packet == null) {
            return;
        }

        if (!Packets.checkCRC(packet)) {
            pLog.error("XXX CRC错误包：" + StringUtil.toHex(packet.getPacket()));
            return;
        }

        packet.getAttribute().put(Packet.ATTR_TIMESTAMP, new Date());
        packet.setRemote((java.net.InetSocketAddress) ctx.channel().remoteAddress());
        packet.setChannelType(Channel.TYPE_TCP);
        packet.setChannel(new TCPChannel(ctx.channel()));

        dispatchPacket(packet);
    }

    private void dispatchPacket(Packet packet) {
        Packet parsedPacket = null;

        for (PacketParser<Packet> parser : PACKET_PARSERS) {
            if (parser.isParseable(packet)) {
                try {
                    parsedPacket = parser.parse(packet);
                    break;
                } catch (Throwable e) {
                    pLog.error("[{}:{}] XXX 解析包异常: {}",
                            packet.getRemoteHost(),
                            packet.getRemotePort(),
                            StringUtil.toHex(packet.getPacket()));
                    if (!Strings.isNullOrEmpty(e.getMessage())) {
                        pLog.error("[{}:{}] XXX 异常信息：{}",
                                packet.getRemoteHost(),
                                packet.getRemotePort(),
                                e.getMessage());
                    }
                    log.error("解析包异常", e);
                }
            }
        }

        if (parsedPacket == null) {
            pLog.error("[{}:{}] XXX 解析包异常: {}",
                    packet.getRemoteHost(),
                    packet.getRemotePort(),
                    StringUtil.toHex(packet.getPacket()));
            return;
        }

        updateChannelAttribute(parsedPacket);

        if (parsedPacket.getVersion() == Versions.V_1) {
            // TCP模式暂不支持 v1.3协议 @gaohui 2014-04-09
            log.error("XXX v1.3 protocol not supported: {}", StringUtil.toHex(packet.getPacket()));
            return;

        } else if (parsedPacket.getVersion() == Versions.V_3) {
            switch (parsedPacket.getPacketType()) {
                case Packets.UP_DATA:
                    DataV30Packet dataPacket = (DataV30Packet) parsedPacket;
                    Packets.logData(dataPacket);

                    byte[] data = PacketEncoder.encodeDataAck(dataPacket);
                    dataPacket.getChannel().write(data);
                    Packets.logDataAck(dataPacket, data);
                    break;

                case Packets.UP_STATUS:
                    StatusPacket statusPacket = (StatusPacket) parsedPacket;
                    Packets.logV30UpStatus(statusPacket);

                    byte[] statusAck = PacketEncoder.encodeStatusAck(statusPacket);
                    parsedPacket.getChannel().write(statusAck);
                    Packets.logV30DownStatusAck(statusPacket, statusAck);
                    break;

                case Packets.UP_REQUEST:
                    RequestPacket reqPacket = (RequestPacket) parsedPacket;
                    Packets.logV30UpRequest(reqPacket);
                    break;

                case Packets.UP_COMMAND_ACK:
                    Packets.logV30DownCommandAck(parsedPacket);
                    break;
            }

        } else {
            log.error("XXX unknown protocol version:{}, {}", packet.getVersion(), StringUtil.toHex(packet.getPacket()));
            return;
        }

        agentWarden.onPacketReceived(parsedPacket);
    }

    @Override
    protected ChannelAttributeCache getChannelAttributeCache() {
        return TCPServer.getChannelAttributeCache();
    }
}
