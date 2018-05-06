package com.microwise.msp.hardware.handler;

import com.google.common.collect.Lists;
import com.microwise.msp.hardware.handler.agent.AgentWarden;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.hardware.handler.codec.v13.DataPacketV13Parser;
import com.microwise.msp.hardware.handler.codec.v13.GatewayHeartBeatPacketParser;
import com.microwise.msp.hardware.handler.codec.v13.RelayRoutePacketParser;
import com.microwise.msp.hardware.handler.codec.v13.V13Packet;
import com.microwise.msp.hardware.handler.codec.v30.*;
import com.microwise.msp.hardware.netlink.ChannelAttribute;
import com.microwise.msp.hardware.netlink.ChannelAttributeCache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author bastengao
 * @date 14-4-16 上午10:59
 */
public abstract class AbstractServerHandler extends ChannelInboundHandlerAdapter {

    @Qualifier("facadeAgentWarden")
    @Autowired
    protected AgentWarden agentWarden;


    // 包解析器集合
    protected static final List<PacketParser> PACKET_PARSERS = Collections.unmodifiableList(Lists.<PacketParser>newArrayList(
            // v3.0
            new DataPacketV30Parser(),
            new StatusPacketParser(),
            new RequestPacketV30Parser(),
            new CmdRespPacketParser(),

            // v1.3
            new DataPacketV13Parser(),
            new GatewayHeartBeatPacketParser(),
            new RelayRoutePacketParser()
    ));


    public static ByteBuffer nioBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.writerIndex()];
        byteBuf.getBytes(0, bytes);
        return ByteBuffer.wrap(bytes);
    }

    /**
     * 跟新 channel 对应的属性
     *
     * @param packet
     */
    protected void updateChannelAttribute(Packet packet) {
        ChannelAttributeCache channelAttributeCache = getChannelAttributeCache();
        ChannelAttribute channelAttribute = channelAttributeCache.requireChannelAttribute(packet.getRemote());

        if (channelAttribute == null) {
            return;
        }

        InetSocketAddress localAddress = packet.getChannel().localAddress();
        channelAttribute.setLocalAddress(localAddress);
        channelAttribute.setVersion(packet.getVersion());
        channelAttribute.setLastTimestamp(new Date());

        if (packet.getVersion() == Versions.V_3) {
            if (packet.getPacketType() == Packets.UP_DATA) {
                DataV30Packet dataPacket = (DataV30Packet) packet;
                channelAttribute.setSiteId(dataPacket.getSiteId());
                // 如果是网关或者父节点是自己，则次设备是网络的根节点
                if (dataPacket.getDeviceType() == Devices.GATEWAY) {
                    channelAttribute.setGatewayId(dataPacket.getSelfId());
                } else if (dataPacket.getParentId() == dataPacket.getSelfId()){
                    channelAttribute.setGatewayId(dataPacket.getSelfId());
                }
            }
            // TODO 上行状态包，也可更新属性 @gaohui 2014-01-16
        }

        if (packet.getVersion() == Versions.V_1 && packet.getPacketType() == Packets.V1_GATEWAY) {
            V13Packet dataPacket = (V13Packet) packet;
            channelAttribute.setGatewayId(dataPacket.getSelfId());
        }

        // write back
        channelAttributeCache.put(packet.getRemote(), channelAttribute);
    }

    abstract protected ChannelAttributeCache getChannelAttributeCache();

}
