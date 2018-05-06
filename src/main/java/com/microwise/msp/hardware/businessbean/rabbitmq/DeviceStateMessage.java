package com.microwise.msp.hardware.businessbean.rabbitmq;

/**
 * Created by lijianfei on 2017/12/18.
 *
 * @author li.jianfei
 * @since 2017/12/18
 */
public class DeviceStateMessage extends Message {
    public DeviceStateMessage() {
        super();
        this.setMessageType(MessageType.DEVICE_STATE);
    }
}
