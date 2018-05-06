package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.Packet;

/**
 * 命令响应包(0x0A)
 *
 * @author gaohui
 * @date 13-8-15 17:51
 */
public class CmdRespPacket extends Packet {

    /**
     * 终端ID
     */
    private int terminalId;

    /**
     * 源包序列号
     */
    private short sourceSequence;

    /**
     * 指令编号
     */
    private int orderId;

    /**
     * 反馈结果
     */
    private short feedback;

    public CmdRespPacket() {
    }

    public CmdRespPacket(Packet packet) {
        super(packet);
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public short getSourceSequence() {
        return sourceSequence;
    }

    public void setSourceSequence(short sourceSequence) {
        this.sourceSequence = sourceSequence;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public short getFeedback() {
        return feedback;
    }

    public void setFeedback(short feedback) {
        this.feedback = feedback;
    }
}
