package com.microwise.msp.hardware.handler.agent.command;

/**
 * 退出全网巡检指令
 * deviceId 表示网关ID
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class PatrolCheckExitCommand extends Command {

    public PatrolCheckExitCommand(String deviceId, int commandId, int sequence) {
        super(deviceId, commandId, sequence);
    }
}
