package com.microwise.msp.hardware.handler.agent;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

/**
 * 代表命令的执行延迟，可通过此类获取命令的执行结果。
 *
 * @author gaohui
 * @date 2014-03-03
 */
public class CmdDeferred {

    // 设备ID
    private String deviceId;
    // 包序列号
    private int sequence;

    // 收到
    private Deferred sendDeferred;
    // 执行
    private Deferred doDeferred;


    public CmdDeferred(String deviceId, int sequence) {
        this.deviceId = deviceId;
        this.sequence = sequence;
        this.sendDeferred = new DeferredObject();
        this.doDeferred = new DeferredObject();
    }

    /**
     * 返回执行的期待
     *
     * @return
     */
    public CmdPromise promise(){
        return new CmdPromise(deviceId, sequence, sendDeferred.promise(), doDeferred.promise());
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getSequence() {
        return sequence;
    }

    public Deferred getSendDeferred() {
        return sendDeferred;
    }

    public Deferred getDoDeferred() {
        return doDeferred;
    }
}
