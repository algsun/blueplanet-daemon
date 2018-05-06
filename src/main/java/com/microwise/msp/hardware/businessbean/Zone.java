package com.microwise.msp.hardware.businessbean;

/**
 * 区域实体类
 *
 * @author li.jianfei
 * @date 2016-07-14
 */
public class Zone {

    /**
     * column t_zone.zoneId 区域uuid
     */
    protected String zoneId;

    /**
     * column t_zone.siteId 父id为0的表明是：监测区域
     */
    protected String siteId;

    /**
     * column t_zone.parentId 父id为null的表明是顶级区域
     */
    protected String parentId;

    /**
     * column t_zone.zoneName 监测区域平面部署图， 主要用于设备在平面图进行部署
     */
    protected String zoneName;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
