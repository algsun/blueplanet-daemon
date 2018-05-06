package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessbean.Site;
import com.microwise.msp.hardware.businessservice.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gaohui
 * @date 13-11-26 09:35
 */
@Component
@Scope("prototype")
public class SiteCacheLoader extends CacheLoader<String, Site> {

    @Autowired
    private SiteService siteService;

    @Override
    public Site load(String siteId) throws Exception {
        Site site = siteService.findBySiteId(siteId);
        if (site == null) {
            throw new Exception("站点未找到, siteId :" + siteId);
        }
        return site;
    }
}
