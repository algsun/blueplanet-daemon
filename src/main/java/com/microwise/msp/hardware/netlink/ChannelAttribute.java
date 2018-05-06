package com.microwise.msp.hardware.netlink;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * channel 额外属性
 *
 * @author gaohui
 * @date 14-1-16 16:47
 */
public class ChannelAttribute {

    /**
     * 本地地址
     */
    private InetSocketAddress localAddress;

    /**
     * 远端地址
     */
    private InetSocketAddress remoteAddress;

    /**
     * 协议版本
     */
    private int version;

    /**
     * 最后接收时间
     */
    private Date lastTimestamp;

    /**
     * 网关ID
     */
    private int gatewayId;

    /**
     * 站点ID
     */
    private String siteId;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Date lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public int getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(int gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    @Override
    public String toString() {
        return "ChannelAttribute{" +
                "localAddress=" + localAddress +
                ", remoteAddress=" + remoteAddress +
                ", version=" + version +
                ", lastTimestamp=" + lastTimestamp +
                ", gatewayId=" + gatewayId +
                ", siteId='" + siteId + '\'' +
                '}';
    }
}
