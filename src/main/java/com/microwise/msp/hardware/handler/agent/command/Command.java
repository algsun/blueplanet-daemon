package com.microwise.msp.hardware.handler.agent.command;

/**
 * 下行命令
 *
 * @author bastengao
 * @date 14-4-29 下午4:14
 */
public class Command {

    // 设备ID
    protected String deviceId;

    // 命令标示
    protected int commandId;

    // 下行命令包序列号
    protected int sequence;


    public Command(String deviceId, int commandId, int sequence) {
        this.deviceId = deviceId;
        this.commandId = commandId;
        this.sequence = sequence;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
