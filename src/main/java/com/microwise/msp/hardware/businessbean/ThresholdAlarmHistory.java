package com.microwise.msp.hardware.businessbean;

import com.microwise.msp.util.StringUtil;

import java.util.Date;

/**
 * 阈值报警历时
 *
 * @author bastengao
 * @date 14-3-20 下午3:09
 */
public class ThresholdAlarmHistory {

    /**报警记录id*/
    private String id;

    /** 站点Id*/
    private String siteId;

    /**位置点Id*/
    private String locationId;

    /**监测指标Id*/
    private int sensorId;

    /**报警因素*/
    private String factor;

    /**报警时间*/
    private Date alarmTime;

    /**状态*/
    private int state;

    public ThresholdAlarmHistory(String siteId, String locationId, int sensorId, String factor,int state) {
        this.id = StringUtil.uuid();
        this.siteId = siteId;
        this.locationId = locationId;
        this.sensorId = sensorId;
        this.factor = factor;
        this.alarmTime = new Date();
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
