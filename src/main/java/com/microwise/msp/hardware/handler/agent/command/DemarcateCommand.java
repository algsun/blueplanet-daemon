package com.microwise.msp.hardware.handler.agent.command;

/**
 * 标定模式指令
 *
 * @author li.jianfei
 * @date 2015-06-04
 */
public class DemarcateCommand extends Command {

    /**
     * 进入标定模式？退出标定模式？
     */
    private boolean inOrOut;

    public DemarcateCommand(String deviceId, int commandId, int sequence, boolean inOrOut) {
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
