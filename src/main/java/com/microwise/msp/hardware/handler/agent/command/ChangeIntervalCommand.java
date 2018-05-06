package com.microwise.msp.hardware.handler.agent.command;

/**
 * 修改工作周期指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class ChangeIntervalCommand extends Command {
    public ChangeIntervalCommand(String deviceId, int commandId, int sequence) {
        super(deviceId, commandId, sequence);
    }
}
