package com.microwise.msp.hardware.handler.agent.command;

/**
 * 设置默认父节点指令
 *
 * @author li.jianfei
 * @date 2014-05-22
 */
public class SetParentCommand extends Command {

    /**
     * 默认父节点ID
     * <p/>
     * parentId最多5位，不能为负数
     */
    private int parentId;


    public SetParentCommand(String deviceId, int commandId, int sequence, int parentId) {
        super(deviceId, commandId, sequence);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
