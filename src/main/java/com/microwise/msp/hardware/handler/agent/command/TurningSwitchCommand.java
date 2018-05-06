package com.microwise.msp.hardware.handler.agent.command;

/**
 * 控制模块开关控制
 *
 * @author bastengao
 * @date 14-4-29 下午4:33
 */
public class TurningSwitchCommand extends Command {

    // 路数
    private int route;

    // 动作
    private int action;

    // 原因
    private String cause;

    public TurningSwitchCommand(String deviceId, int commandId, int sequence, int route, int action, String cause) {
        super(deviceId, commandId, sequence);
        this.route = route;
        this.action = action;
        this.cause = cause;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
