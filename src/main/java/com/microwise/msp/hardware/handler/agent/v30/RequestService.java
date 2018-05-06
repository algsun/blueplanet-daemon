package com.microwise.msp.hardware.handler.agent.v30;

import com.google.common.primitives.Bytes;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.handler.codec.Devices;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v30.PacketEncoder;
import com.microwise.msp.hardware.handler.codec.v30.RequestPacket;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;

import java.util.List;

/**
 * 上行请求服务
 *
 * @author gaohui
 * @date 13-8-12 15:30
 */
public class RequestService {

    // 强制授时的时间范围(单位秒)
    public static final int FORCE_SET_TIME_RANGE = 5;

    // TODO 与 DcoOperateService 的包序列号合并 @gaohui 2014-03-13
    // 强制授时包顺序号(1-255)
    private int FORCE_SET_TIME_SEQUENCE = 0;


    // 用于打印数据包
    public static final Logger pLog = Packets.log;

    public void service(Packet packet){
        RequestPacket requestPacket = (RequestPacket) packet;

        processRequestPackage(requestPacket);
    }

    /**
     * <pre>
     * 解析[上行请求包(0x05)]--生成ACK[请求应答包(0x06)]--发送ACK
     * </pre>
     *
     * @param packet
     *            上行请求包0x05
     * @author he.ming
     * @since May 29, 2012
     */
    public void processRequestPackage(RequestPacket packet) {
        // 请求应答包0x06
        List<Byte> ackList = processPacket(packet);
        // 以UDP方式发送，请求应答包
        // UDPClient.send(Bytes.toArray(ackList), packet.getRemoteHost(), packet.getRemotePort());
        packet.getChannel().write(ackList);

        forceSetTimeToGateway(packet);
    }


    /**
     * 处理包，并返回内容
     *
     * @param packet
     * @return
     */
    protected List<Byte> processPacket(RequestPacket packet){
        byte[] orderValue = new byte[]{};
        switch (packet.getOrderId()) {
            case Defines.ORDER_LINK: // 连接请求:网关以固定频率上行此包，以侦测\维持网络连接
                orderValue = new byte[]{};
                break;
            case Defines.ORDER_SETTIME: // 授时请求:授时请求，网关向服务器请求服务器时间
                orderValue = PacketEncoder.timestampNow();
                break;
            case RequestPacket.ORDER_REPORT_TIME:
                orderValue = new byte[]{};
                break;
            default:
                pLog.error("XXX 未知指令类型: {}, {}", packet.getOrderId(), StringUtil.toHex(packet.getPacket()));
                break;
        }
        byte[] data = PacketEncoder.encodeRequestAck(7, packet.getOrderId(), packet.getSequence(), orderValue);
        Packets.logV30DownRequestAck(packet, data);
        return  Bytes.asList(data);
    }

    /**
     * 判断网状时间缀，如果与服务器相关 正负 5 秒，强制授时 @gaohui 2013-09-13
     *
     * @param packet
     */
    private void forceSetTimeToGateway(RequestPacket packet){
        DateTime dateReceived = new DateTime(packet.getAttribute().get(Packet.ATTR_TIMESTAMP));
        DateTime timestamp = new DateTime(packet.getTimestamp());

        Duration duration = new Duration(dateReceived, timestamp);
        if(duration.getStandardSeconds() < -FORCE_SET_TIME_RANGE || duration.getStandardSeconds() > FORCE_SET_TIME_RANGE){
            FORCE_SET_TIME_SEQUENCE = (FORCE_SET_TIME_SEQUENCE % 255) + 1;
            // 强制授时
            byte[] cmd = PacketEncoder.encodeCommand(
                    Devices.GATEWAY,
                    packet.getTerminalId(),
                    FORCE_SET_TIME_SEQUENCE,
                    0,
                    null,
                    0x0013,
                    PacketEncoder.timestampNow()
            );
            packet.getChannel().write(cmd);
            pLog.info("[{}:{}] => 下行命令包[09]-{}-{}: {}",
                    packet.getRemoteHost(),
                    packet.getRemotePort(),
                    packet.getDeviceType(),
                    packet.getTerminalId(),
                    StringUtil.toHex(cmd)
            );
        }
    }

}
