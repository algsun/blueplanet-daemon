package com.microwise.msp.hardware.handler.codec.v30;

/**
 * 控制模块开关
 *
 * @author gaohui
 * @date 14-1-21 14:11
 */
public class Switch {

    /**
     * 从 1 开始
     */
    private int index;
    /**
     * 是否使用
     */
    private boolean enable;
    /**
     * 开关状态
     */
    private boolean on;
    /**
     * 是否开关动作
     */
    private boolean changed;
    /**
     * 是否是条件反射期间的数据
     */
    private boolean condRelf;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isCondRelf() {
        return condRelf;
    }

    public void setCondRelf(boolean condRelf) {
        this.condRelf = condRelf;
    }

    @Override
    public String toString() {
        return "Switch{" +
                "index=" + index +
                ", enable=" + enable +
                ", on=" + on +
                ", changed=" + changed +
                ", condRefl=" + condRelf +
                '}';
    }
}
