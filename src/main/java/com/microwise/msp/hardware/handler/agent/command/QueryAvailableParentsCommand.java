package com.microwise.msp.hardware.handler.agent.command;

/**
 * 查询可选父节点指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class QueryAvailableParentsCommand extends Command {

    public QueryAvailableParentsCommand(String deviceId, int commandId, int sequence) {
        super(deviceId, commandId, sequence);
    }
}
