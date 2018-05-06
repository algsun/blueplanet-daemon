package com.microwise.msp.hardware.businessbean.rabbitmq;

/**
 * Created by lijianfei on 2017/12/18.
 *
 * @author li.jianfei
 * @since 2017/12/18
 */
public class SensorDataMessage extends Message {
    public SensorDataMessage() {
        super();
        this.setMessageType(MessageType.SENSOR_DATA);
    }
}
