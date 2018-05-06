package com.microwise.msp.hardware.handler.codec.v13;

import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * v1.3 上行数据包
 *
 * @author gaohui
 * @date 13-8-8 13:00
 */
public class DataV13Packet extends DataPacket implements V13Packet {

    /**
     * 复制基本信息到新的 packet
     *
     * @param packet
     */
    public DataV13Packet(Packet packet) {
        super(packet);
    }

    /** 子网ID */
    protected int netId;
    /** 路由路数 */
    protected int jump;

    /** 反馈地址 */
    protected int feedback;

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
