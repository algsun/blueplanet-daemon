package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;

import java.util.Date;

/**
 * 上行请求包(0x05)
 *
 * @author gaohui
 * @date 13-8-10 10:20
 */
public class RequestPacket extends Packet {
    // 请求连接
    public static final int ORDER_REQUEST_CONNECT = 0x01;
    // 请求授时
    public static final int ORDER_REQUEST_TIME = 0x02;
    // 上报时间
    public static final int ORDER_REPORT_TIME = 0x03;

    public RequestPacket() {
    }

    public RequestPacket(Packet packet) {
        super(packet);
    }

    /** 指令标识 */
    protected int orderId;
    /** 序列号 */
    protected int sequence;


    /**
     * 节点ID(指令标识为上报时间有效)
     */
    protected int terminalId;
    /**
     * 时间缀(指令标识为上报时间有效)
     */
    protected Date timestamp;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
