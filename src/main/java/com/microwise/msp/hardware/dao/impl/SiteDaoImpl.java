package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.Site;
import com.microwise.msp.hardware.dao.SiteDao;
import com.microwise.msp.platform.bean.LogicGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaohui
 * @date 13-11-26 09:19
 */
@Component
@Scope("prototype")
public class SiteDaoImpl extends BaseDaoImpl implements SiteDao {

    @Override
    public Site findBySiteId(String siteId) {
        return getSqlSession().selectOne("Site.findBySiteId", siteId);
    }

    @Override
    public Integer findLogicGroupBySiteId(String siteId) {
        return getSqlSession().selectOne("Site.findLogicGroupBySiteId", siteId);
    }

    public List<LogicGroup> findAllLogicGroup() {
        return getSqlSession().selectList("Site.findAllLogicGroup");
    }
}
