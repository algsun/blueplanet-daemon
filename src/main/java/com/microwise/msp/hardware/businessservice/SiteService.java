package com.microwise.msp.hardware.businessservice;

/**
 * @author gaohui
 * @date 13-11-26 09:25
 */

import com.microwise.msp.platform.bean.LogicGroup;
import com.microwise.msp.hardware.businessbean.Site;
import com.microwise.msp.hardware.dao.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class SiteService {

    @Autowired
    private SiteDao siteDao;

    public Site findBySiteId(String siteId) {
        return siteDao.findBySiteId(siteId);
    }

    public List<LogicGroup> findAllLogicGroup() {
        return siteDao.findAllLogicGroup();
    }
}
