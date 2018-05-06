package com.microwise.msp.hardware.handler.agent.command;

/**
 * 修改设备工作周期指令
 *
 * @author liuzhu
 * @date 2014-5-23
 */
public class ModifyIntervalCommand extends Command {

    //工作周期
    private int interval;

    public ModifyIntervalCommand(String deviceId, int commandId, int sequence,int interval) {
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
