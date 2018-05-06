package com.microwise.msp.hardware.handler.agent.command;

/**
 * 开启全网巡检指令
 * deviceId 表示网关ID
 *
 * @author li.jianfei
 * @date 2014-05-22
 * @see com.microwise.msp.hardware.handler.agent.command.Command
 */
public class PatrolCheckStartCommand extends Command {

    /**
     * 工作周期
     */
    private int interval;

    public PatrolCheckStartCommand(String deviceId, int commandId, int sequence, int interval) {
        super(deviceId, commandId, sequence);
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
