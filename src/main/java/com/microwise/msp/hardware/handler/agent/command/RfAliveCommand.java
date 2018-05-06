package com.microwise.msp.hardware.handler.agent.command;

/**
 * 节点 RF 不休眠指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class RfAliveCommand extends Command {

    /**
     * 休眠？不休眠
     */
    private boolean enable;

    public RfAliveCommand(String deviceId, int commandId, int sequence, boolean enable) {
        super(deviceId, commandId, sequence);
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
