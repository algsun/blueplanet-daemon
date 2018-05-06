package com.microwise.msp.hardware.handler.agent;

import org.jdeferred.Promise;

/**
 * 代表命令的执行延迟，可通过此类获取命令的执行结果。
 *
 * 下行命令的反馈分两步，第一步：命令送达网关, 第二部：命令执行成功。
 * 由于中间件与网关是通过 udp 协议，是异步的。可通过 sendPromise 与 doPromise 来判断结果。
 *
 *
 * @author gaohui
 * @date 2014-03-03
 */
public class CmdPromise {

    // 设备ID
    private String deviceId;
    // 包序列号
    private int sequence;

    // 收到
    private Promise sendPromise;
    // 执行
    private Promise doPromise;

    public CmdPromise(String deviceId, int sequence, Promise sendPromise, Promise doPromise) {
        this.deviceId = deviceId;
        this.sequence = sequence;
        this.sendPromise = sendPromise;
        this.doPromise = doPromise;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getSequence() {
        return sequence;
    }

    public Promise getSendPromise() {
        return sendPromise;
    }

    public Promise getDoPromise() {
        return doPromise;
    }
}
