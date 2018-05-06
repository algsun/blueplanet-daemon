package com.microwise.msp.hardware.handler.codec;

import com.google.common.primitives.Bytes;
import com.microwise.msp.hardware.handler.channel.Channel;
import com.microwise.msp.util.StringUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据包
 *
 * @author gaohui
 * @date 13-7-18 13:21
 */
public class Packet {
    /** 站点ID, 类型 string */
    public static final String ATTR_SITE_ID = "siteId";
    /** 数据接收时的时间缀, 类型 Date */
    public static final String ATTR_TIMESTAMP = "timestamp";

    /** 远端地址 **/
    private InetSocketAddress remote;

    /** 通道类型 */
    private int channelType;

    /** 通道, 可以通过此通道发送数据 */
    private Channel channel;

    /**
     * 远端地址
     * @deprecated 使用 remote @gaohui 2014-1-16
     */
    protected String remoteHost;
    /**
     * 远端端口
     * @deprecated 使用 remote @gaohui 2014-1-16
     */
    protected int remotePort;

    /** 完成数据包 */
    protected byte[] packet;

    /** 包类型(协议类型) */
    protected int packetType;

    /** 终端类型 */
    protected int deviceType;

    /** 版本 */
    protected int version;

    /** 包长 */
    protected int bodyLength;

    /** CRC16(Modbus) */
    protected int crc;

    /**
     * 位置点编号
     */
    protected String locationId;

    /**
     * 是否是上传的数据
     */
    private boolean isUpload;

    /**
     * 是否第一包数据
     */
    private boolean isFirstPacket;

    /**
     * 是否最后一包数据
     */
    private boolean isLastPacket;

    /**
     * 自定义属性, 放一些临时性的变量
     */
    protected Map<String, Object> attribute;

    /**
     * 是否是1.3节点通过V3网关
     */
    protected boolean isDummyV3Version;

    public Packet() {
        this.attribute = new HashMap<String, Object>();
    }

    /**
     * 复制基本信息到新的 packet
     *
     * @param packet
     */
    public Packet(Packet packet){
        Packets.copy(packet, this);
        this.attribute = packet.attribute;
    }

    public Packet(byte[] packet, int version, String remoteHost, int remotePort) {
        this.packet = packet;
        this.version = version;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public Packet(List<Byte> packet, int version, String remoteHost, int remotePort) {
        this.packet = Bytes.toArray(packet);
        this.version = version;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public boolean isDummyV3Version() {
        return isDummyV3Version;
    }

    public void setDummyV3Version(boolean isDummyV3Version) {
        this.isDummyV3Version = isDummyV3Version;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public byte[] getPacket() {
        return packet;
    }

    public void setPacket(byte[] packet) {
        this.packet = packet;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public String getRemoteHost() {
        if(remote == null){
            return null;
        }

        return remote.getAddress().getHostAddress();
    }

    /**
     * @param remoteHost
     * @deprecated 请使用 remote @gaohui 2014-1-16
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        if(remote == null){
            return -1;
        }

        return remote.getPort();
    }

    /**
     * @param remotePort
    * @deprecated 请使用 remote @gaohui 2014-1-16
    */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public InetSocketAddress getRemote() {
        return remote;
    }

    public void setRemote(InetSocketAddress remote) {
        this.remote = remote;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public boolean isFirstPacket() {
        return isFirstPacket;
    }

    public void setFirstPacket(boolean isFirstPacket) {
        this.isFirstPacket = isFirstPacket;
    }

    public boolean isLastPacket() {
        return isLastPacket;
    }

    public void setLastPacket(boolean isLastPacket) {
        this.isLastPacket = isLastPacket;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "packetType=" + packetType +
                ", version=" + version +
                ", remoteHost='" + getRemoteHost() + '\'' +
                ", remotePort=" + getRemotePort() +
                ", packet=" + StringUtil.toHex(packet) +
                "} " + super.toString();
    }
}
