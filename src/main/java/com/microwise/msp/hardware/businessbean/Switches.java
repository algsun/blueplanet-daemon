package com.microwise.msp.hardware.businessbean;

import com.microwise.msp.hardware.handler.codec.v30.DataPacketV30Parser;
import com.microwise.msp.hardware.handler.codec.v30.Switch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 控制模块开关状态
 *
 * @date 2014-02-20
 * @author gaohui
 */
public class Switches {

    private String id;

    private String deviceId;

    private int enable;

    private int onOff;

    private Date timestamp;

    private List<Switch> values;

    public Switches() {
    }

    public Switches(String deviceId, int enable, int onOff, Date timestamp) {
        this.deviceId = deviceId;
        this.enable = enable;
        this.onOff = onOff;
        this.timestamp = timestamp;

        parseToValues();
    }

    public Switches(String deviceId, int enable, int onOff, Date timestamp, List<Switch> values) {
        this.deviceId = deviceId;
        this.enable = enable;
        this.onOff = onOff;
        this.timestamp = timestamp;
        this.values = values;
    }

    /**
     * 解析状态到 values
     *
     */
    public void parseToValues(){
        List<Switch> switches = new ArrayList<Switch>();
        for(int i = 0; i< DataPacketV30Parser.SWITCH_MAX_SIZE; i++){
            Switch switcH = new Switch();
            switcH.setIndex(i + 1);
            switcH.setEnable(((enable >> i ) & 0x0001) == 1);
            switcH.setOn(((onOff >> i) & 0x0001) == 1);

            switches.add(switcH);
        }

        this.values = switches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Switch> getValues() {
        return values;
    }

    public void setValues(List<Switch> values) {
        this.values = values;
    }
}
