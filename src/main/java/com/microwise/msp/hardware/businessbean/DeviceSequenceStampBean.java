package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备
 *
 * @author liuzhu
 * @since 2015-8-12
 */
public class DeviceSequenceStampBean implements Serializable {

    /**
     * 包序列号 1Byte,0x01--0xFF循环自增(无论发送成功与否),设备初始化后首个包序列号为0x00
     */
    public int sequence;

    /**
     * 包序列号
     */
    public Date stamp;

}
