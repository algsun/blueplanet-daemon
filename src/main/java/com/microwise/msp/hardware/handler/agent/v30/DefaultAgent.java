package com.microwise.msp.hardware.handler.agent.v30;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DeviceService;
import com.microwise.msp.hardware.businessservice.NodeService;
import com.microwise.msp.hardware.handler.agent.CmdDeferredManager;
import com.microwise.msp.hardware.handler.agent.DeviceAgent;
import com.microwise.msp.hardware.handler.channel.Channel;
import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v30.*;
import com.microwise.msp.hardware.netlink.DeviceChannelSuggestor;
import com.microwise.msp.hardware.netlink.DeviceChannelSuggestor.SuggestChannel;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Timestamp;
import java.util.Date;

/**
 * V3.0 协议设备 agent 的骨架实现。按包类型分配到不同的方法上，子类覆盖合适的方法即可。
 *
 * @author gaohui
 * @date 13-8-17 14:12
 */
public abstract class DefaultAgent extends DeviceAgent {
    public static final Logger log = LoggerFactory.getLogger(DefaultAgent.class);
    public static final Logger pLog = Packets.log;

    // 公司成立时间, 2007-1-1 00:00:00
    public static final Timestamp DARK_END = new Timestamp(
            DateTime.now()
                    .withDate(2007, 1, 1)
                    .withTime(0, 0, 0, 0)
                    .getMillis()
    );

    @Autowired
    protected NodeService nodeService;

    @Autowired
    @Qualifier("DeviceService")
    protected DeviceService deviceService;

    @Autowired
    private DeviceChannelSuggestor deviceChannelSuggestor;

    /**
     * 站点ID， 不保存 siteId 有值，通过上行数据包更新, 主要给命令响应包
     */
    protected String siteId;

    // 数据包序列号
    protected int dataSequence;
    // 数据包时间戳
    protected Date dataTimestamp;

    // 状态包序列号
    private int statusSequence;

    @Override
    public void onPacketReceived(Packet packet) {

        switch (packet.getPacketType()) {
            case Packets.UP_DATA:
                onDataReceived((DataV30Packet) packet);
                break;
            case Packets.UP_COMMAND_ACK:
                onCommandAck((CmdRespPacket) packet);
                break;
            case Packets.UP_STATUS:
                onDeviceStatus((StatusPacket) packet);
                break;

            default:
                log.error("未知包类型: {}", packet.getPacketType());
        }
    }


    /**
     * 当上行数据
     *
     * @param packet
     */
    protected void onDataReceived(DataV30Packet packet) {
        updateRootChannel(packet);

        if (!filterPacket(packet)) {
            return;
        }

        // 包序列号与时间戳相同
        if(packet.getSequence() == dataSequence && packet.getTimestamp().equals(dataTimestamp)){
            Packets.log.warn("[{}:{}] XXX 重复数据包", packet.getRemoteHost(), packet.getRemotePort());
            return;
        }
        dataSequence = packet.getSequence();
        dataTimestamp = packet.getTimestamp();

        onDataReady(packet);
    }

    /**
     * 上行数据包满足条件，准备好时
     *
     * @param packet
     */
    protected void onDataReady(DataV30Packet packet) {
        DeviceBean deviceBean = DataV30Packets.fromPacket(packet);

        siteId = deviceBean.siteId;

        deviceService.businessProcess(deviceBean);
    }

    /**
     * 当上行请求
     *
     * @param packet
     */
    protected void onRequestReceived(RequestPacket packet) {
    }

    /**
     * 当上行设备状态包
     *
     * @param packet
     */
    protected void onDeviceStatus(StatusPacket packet){
        updateRootChannel(packet);

        if(packet.getSequence() == statusSequence){
            Packets.log.warn("[{}:{}] XXX 重复设备状态包", packet.getRemoteHost(), packet.getRemotePort());
            return;
        }

        statusSequence = packet.getSequence();
        siteId = packet.getSiteId();

        DeviceBean deviceBean = DataV30Packets.fromPacket(packet);
        deviceService.businessProcess(deviceBean);
    }

    /**
     * 当命令响应
     *
     * @param packet
     */
    protected void onCommandAck(CmdRespPacket packet) {

        // TODO 判断是否是重复 ack, 目前只有源包序列号不好判断 @gaohui 2014-04-23

        // 因为命令响应包没有携带站点ID, 所以无法直接推断出设备ID, 但可以根据替他数据包得到 siteId。
        // 如果没有 siteId 推断不出 deviceId
        if (siteId != null) {
            String deviceId = deviceId(packet);

            switch (packet.getFeedback()) {
                case Commands.FEEDBACK_GOT:
                    CmdDeferredManager.getInstance().gotCommand(deviceId, packet.getSourceSequence());
                    break;
                case Commands.FEEDBACK_SUCCESS:
                    CmdDeferredManager.getInstance().executeCommand(deviceId, packet.getSourceSequence(), true);
                    break;
                case Commands.FEEDBACK_FAILED:
                    CmdDeferredManager.getInstance().executeCommand(deviceId, packet.getSourceSequence(), false);
                    break;
            }

            onCommandAckReady(packet);
        }
    }

    /**
     * 当命令响应包, 准备好
     * @param packet
     */
    protected void onCommandAckReady(CmdRespPacket packet){
        log.debug("test");
    }


    /**
     * 包过滤, 如果能处理返回 true, 不能处理返回 false
     *
     * @param packet
     * @return
     */
    protected boolean filterPacket(DataV30Packet packet) {
        if (packet.getPacketType() == Packets.UP_DATA) {
            // 判断包的完整性 @gaohui 2013-10-24
            if (Strings.isNullOrEmpty(packet.getSiteId())) {
                pLog.warn("XXX 无接入点号(siteId), {}", StringUtil.toHex(packet.getPacket()));
                return false;
            }

            if (packet.getTimestamp() == null) {
                pLog.warn("XXX 无时间缀(timestamp), {}", StringUtil.toHex(packet.getPacket()));
                return false;
            }

            if (isTooEarly(packet)) {
                return false;
            }

            if (isFuture(packet)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 更新 channel
     *
     * @param packet
     */
    private void updateRootChannel(DataV30Packet packet) {
        DeviceBean device = DataV30Packets.fromPacket(packet);
        updateRootChannel(device.deviceid, packet);
    }

    /**
     * 更新 channel
     *
     * @param packet
     */
    private void updateRootChannel(StatusPacket packet){
        DeviceBean device = DataV30Packets.fromPacket(packet);
        updateRootChannel(device.deviceid, packet);
    }

    /**
     * 更新 channel
     *
     * @param deviceId
     * @param packet
     */
    private void updateRootChannel(String deviceId, Packet packet){
        // 更新 channel
        String rootDeviceId = deviceChannelSuggestor.queryRootDeviceId(deviceId);
        if (rootDeviceId != null && packet.getChannel() != null) {
            if (packet.getChannel().type() == Channel.TYPE_UDP) {
                deviceChannelSuggestor.putRootChannel(rootDeviceId,
                        new SuggestChannel(SuggestChannel.TYPE_UDP, packet.getRemote()));
            } else if (packet.getChannel().type() == Channel.TYPE_TCP) {
                deviceChannelSuggestor.putRootChannel(rootDeviceId,
                        new SuggestChannel(SuggestChannel.TYPE_TCP, packet.getRemote(), packet.getChannel()));
            }
        }
    }

    /**
     * 是否太早了
     *
     * @return
     */
    private boolean isTooEarly(DataV30Packet packet) {
        DataPacket p = (DataPacket) packet;
        if (p.getTimestamp().before(DARK_END)) {
            // v3.0 协议因为授时可能有一定的延迟，在按授时回复正常前数据的时间可能是错的(通常是2000-1-1 开始)，
            // 所以可以做简单的过滤，因为公司是 2007 年成立的，所以数据凡是小于 2007 年直接掉丢
            pLog.warn("[{}:{}] XXX {}-{} 时间戳早于2007年, {}",
                    packet.getRemoteHost(),
                    packet.getRemotePort(),
                    p.getDeviceType(),
                    p.getSelfId(),
                    StringUtil.toHex(packet.getPacket())
            );
            return true;
        }

        return false;
    }

    /**
     * 是否是未来时间
     *
     * @param packet
     * @return
     */
    private boolean isFuture(DataV30Packet packet) {
        // 判断时间超过服务器当前时间30天，则将数据丢弃 @gaohui 2013-09-13
        // 接收数据的时间
        DataPacket p = (DataPacket) packet;
        DateTime dataReceived = new DateTime(((Date) p.getAttribute().get(Packet.ATTR_TIMESTAMP)));
        // 数据包中的时间缀
        DateTime dateTime = new DateTime(p.getTimestamp());
        // 如果数据包中的时间缀比接收数据的时间缀大
        if (dataReceived.isBefore(dateTime)) {
            Duration duration = new Duration(dataReceived, dateTime);
            // 相差超过 30 天
            if (duration.getStandardDays() > 30) {
                pLog.warn("[{}:{}] XXX {}-{} 时间穿越到未来 {}, {}",
                        packet.getRemoteHost(),
                        packet.getRemotePort(),
                        p.getDeviceType(),
                        p.getSelfId(),
                        DateUtils.getDate(p.getTimestamp()),
                        StringUtil.toHex(p.getPacket())
                );
                return true;
            }
        }

        return false;
    }

    /**
     * 根据命令反馈包获取设备ID, 注意 siteId 可能为 null
     *
     * @param packet
     * @return
     */
    protected String deviceId(CmdRespPacket packet) {
        return siteId + StringUtil.fillZero(packet.getTerminalId(), 5);
    }

}
