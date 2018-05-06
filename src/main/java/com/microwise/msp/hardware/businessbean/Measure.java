package com.microwise.msp.hardware.businessbean;

/**
 * 措施
 *
 * @author bastengao
 * @date 14-3-20 下午4:30
 */
public class Measure {
    private String id;
    private String zoneId;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
