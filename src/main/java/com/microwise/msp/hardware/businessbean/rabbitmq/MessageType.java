package com.microwise.msp.hardware.businessbean.rabbitmq;

/**
 * Created by lijianfei on 2017/12/18.
 *
 * @author li.jianfei
 * @since 2017/12/18
 */
public enum MessageType {

    // 监测数据
    SENSOR_DATA("sensor-data"),

    // 设备状态
    DEVICE_STATE("device-state");

    private String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
