package com.microwise.msp.hardware.handler;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.handler.channel.Channel;
import com.microwise.msp.hardware.handler.channel.UDPChannel;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.hardware.handler.codec.v13.V13Packet;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.handler.codec.v30.PacketEncoder;
import com.microwise.msp.hardware.handler.codec.v30.RequestPacket;
import com.microwise.msp.hardware.handler.codec.v30.StatusPacket;
import com.microwise.msp.hardware.netlink.ChannelAttributeCache;
import com.microwise.msp.hardware.netlink.UDPClient;
import com.microwise.msp.hardware.netlink.UDPServer;
import com.microwise.msp.hardware.vo.NetInfo;
import com.microwise.msp.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * 一个本地端口对应一个 handler(并不是单例的) 和一个独立的线程
 *
 * @author gaohui
 * @date 14-1-10 13:09
 */
@Component
@Scope("prototype")
public class UDPServerHandler2 extends AbstractServerHandler {
    public static final Logger log = LoggerFactory.getLogger(UDPServerHandler2.class);
    // 用于打印数据包
    public static final Logger pLog = Packets.log;

    @Autowired
    private AppCache appCache;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("inactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof DatagramPacket)) {
            return;
        }

        try {
            DatagramPacket datagramPacket = (DatagramPacket) msg;
            UDPServer.putChannel(datagramPacket.sender(), ctx);

            ByteBuf byteBuf = datagramPacket.content();
            log.trace("received: {}", ByteBufUtil.hexDump(byteBuf).toUpperCase());

            PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
            ByteBuffer buf = nioBuf(byteBuf);
            if (SysConfig.open == 1) {
                UDPClient.send(buf.array(), SysConfig.remoteHost, SysConfig.port);
            }
            byteBuf.release();
            while (buf.hasRemaining()) {
                Packet packet = packetSplitter.split(buf);
                if (packet == null) {
                    break;
                }

                if (!Packets.checkCRC(packet)) {
                    pLog.error("XXX CRC错误包：" + StringUtil.toHex(packet.getPacket()));
                    continue;
                }

                InetSocketAddress remoteAddress = datagramPacket.sender();
                packet.setRemote(remoteAddress);
                packet.setChannelType(Channel.TYPE_UDP);
                packet.setChannel(new UDPChannel(ctx.channel(), remoteAddress));

                initExtraAttribute(packet, datagramPacket.recipient().getPort());
                dispatchPacket(packet);
            }
        } catch (Exception e) {
            log.error("UDP端口数据监听异常", e);
        }
    }

    /**
     * 放置一些额外的信息
     *
     * @param packet
     */
    private void initExtraAttribute(Packet packet, int localPort) {
        // 收到数据第一时间的时间缀，留作后面业务处理
        packet.getAttribute().put(Packet.ATTR_TIMESTAMP, new Date());

        // 如果此端口绑定了站点ID，则将 siteId 放到 packet 自定义属性中
        NetInfo netInfo = appCache.loadNetInfo(localPort);
        if (netInfo != null && !Strings.isNullOrEmpty(netInfo.getSiteId())) {
            packet.getAttribute().put(Packet.ATTR_SITE_ID, netInfo.getSiteId());
        }

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

        if (!beforeProcess(parsedPacket)) {
            return;
        }

        if (parsedPacket.getVersion() == Versions.V_1) {
            V13Packet v13Packet = (V13Packet) parsedPacket;
            Packets.logV13(v13Packet);
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
            log.error("XXX 未知包类型: {}", StringUtil.toHex(packet.getPacket()));
            return;
        }

        agentWarden.onPacketReceived(parsedPacket);
    }

    /**
     * 处理前过滤，如果能处理返回 true, 否则返回 false
     *
     * @param packet
     * @return
     */
    private boolean beforeProcess(Packet packet) {
        if (packet.getVersion() == Versions.V_1) {
            if (Strings.isNullOrEmpty((String) packet.getAttribute().get("siteId"))) {
                pLog.error("XXX V1.3协议未绑定站点ID [{}:{}], {}",
                        packet.getRemoteHost(),
                        packet.getRemotePort(),
                        StringUtil.toHex(packet.getPacket()));
                return false;
            }
        }

        return true;
    }

    @Override
    protected ChannelAttributeCache getChannelAttributeCache() {
        return UDPServer.getChannelAttributeCache();
    }

}
