package com.microwise.msp.hardware.businessbean.rabbitmq;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 报警原始数据
 *
 * @author sun.cong
 * @create 2017-11-13 11:19
 **/
@Data
public abstract class Message implements Serializable {

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 系统类型
     * 1-银河
     * 2-终结者
     */
    private int systemType;

    /**
     * 银河的站点/终结者的机构
     */
    private Unit unit;

    /**
     * 银河的区域/终结者的文物
     */
    private Target target;

    /**
     * 位置点
     */
    private Location location;

    /**
     * 时间戳
     */
    private Timestamp timeStamp;

    public String setMessageType(MessageType messageType) {
        this.messageType = messageType.getValue();
        return this.messageType;
    }
}
