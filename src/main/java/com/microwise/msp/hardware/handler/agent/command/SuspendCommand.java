package com.microwise.msp.hardware.handler.agent.command;

/**
 * 中继待机指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class SuspendCommand extends Command {

    /**
     * 进入待机？退出待机？
     */
    private boolean inOrOut;

    public SuspendCommand(String deviceId, int commandId, int sequence, boolean inOrOut) {
        super(deviceId, commandId, sequence);
        this.inOrOut = inOrOut;
    }

    public boolean isInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        this.inOrOut = inOrOut;
    }
}
