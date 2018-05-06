package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.util.StringUtil;

import java.sql.Timestamp;
import java.util.Date;

/**
 * V13Packet 工具类
 *
 * @author gaohui
 * @date 13-8-18 11:06
 */
public class V13Packets {
    /**
     * 根据 v1.3 包创建 DeviceBean 对象
     *
     * @param packet
     * @return
     */
    public static DeviceBean fromPacket(DefaultPacket packet) {
        return fromPacket(fromBasicPacket(packet), packet);
    }

    /**
     * 根据 v1.3 数据包创建 DeviceBean 对象(不包括监测指标)
     *
     * @param packet
     * @return
     */
    public static DeviceBean fromPacket(DataV13Packet packet) {
        return fromPacket(fromBasicPacket(packet), packet);
    }

    /**
     * 复制基本信息
     *
     * @param packet
     * @return
     */
    public static DeviceBean fromBasicPacket(Packet packet) {
        DeviceBean deviceBean = new DeviceBean();
        deviceBean.remoteAddress = packet.getRemoteHost();
        deviceBean.remotePort = packet.getRemotePort();
        deviceBean.siteId = (String) packet.getAttribute().get(Packet.ATTR_SITE_ID);

        return deviceBean;
    }

    /**
     * 复制设备信息
     *
     * @param deviceBean
     * @param packet
     * @return
     */
    public static DeviceBean fromPacket(DeviceBean deviceBean, V13Packet packet) {
        deviceBean.packageType = packet.getPacketType();
        deviceBean.deviceType = packet.getDeviceType();
        deviceBean.version = packet.getVersion();
        deviceBean.size = packet.getBodyLength();

        deviceBean.netid = packet.getNetId();
        deviceBean.jump = packet.getJump();
        deviceBean.parentid = packet.getParentId();
        deviceBean.selfid = packet.getSelfId();
        deviceBean.feedback = packet.getFeedback();
        deviceBean.sequence = packet.getSequence();
        deviceBean.voltage = packet.getVoltage();
        deviceBean.rssi = packet.getRssi();
        deviceBean.lqi = packet.getLqi();


        // v1.3 协议默认值
        deviceBean.isControl = 1;
        deviceBean.interval = 1200; //默认工作周期(20分钟)
        deviceBean.deviceMode = 0;
        deviceBean.sdCardState = 0;
        Date timestamp = (Date) packet.getAttribute().get(Packet.ATTR_TIMESTAMP);
        deviceBean.timeStamp = new Timestamp(timestamp.getTime());
        deviceBean.deviceid = deviceBean.siteId
                + StringUtil.netIdTo2(deviceBean.netid)
                + StringUtil.selfIdTo3(deviceBean.selfid);
        deviceBean.packet = StringUtil.toHex(packet.getPacket());

        return deviceBean;
    }
}
