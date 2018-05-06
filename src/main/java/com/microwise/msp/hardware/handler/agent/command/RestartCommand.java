package com.microwise.msp.hardware.handler.agent.command;

/**
 * 设备重启指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class RestartCommand extends Command {

    public RestartCommand(String deviceId, int commandId, int sequence) {
        super(deviceId, commandId, sequence);
    }
}
