package com.microwise.msp.hardware.businessbean;

/**
 * 站点
 *
 * @author gaohui
 * @date 13-11-26 09:16
 */
public class Site {
    // 站点ID
    private String siteId;
    // 站点名称
    private String name;
    // 区域编码
    private String areaCode;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
