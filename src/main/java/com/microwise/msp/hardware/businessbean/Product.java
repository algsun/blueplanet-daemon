package com.microwise.msp.hardware.businessbean;

/**
 * 产品基本信息
 *
 * @author li.jianfei
 * @date 2015-01-15
 */
public class Product {

    /**
     * 站点id
     */
    private String siteId;

    /**
     * 站点名称
     */
    private String siteName;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
