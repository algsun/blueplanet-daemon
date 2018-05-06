package com.microwise.msp.hardware.dao;

import com.microwise.msp.platform.bean.LogicGroup;
import com.microwise.msp.hardware.businessbean.Site;

import java.util.List;

/**
 * 站点
 *
 * @author gaohui
 * @date 13-11-26 09:16
 */
public interface SiteDao {
    Site findBySiteId(String siteId);

    Integer findLogicGroupBySiteId(String siteId);

    /**
     * 查询所有所有站点组
     */
    public List<LogicGroup> findAllLogicGroup();
}
