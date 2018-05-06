package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.User;

/**
 * @author bastengao
 * @date 14-5-4 上午10:19
 */
public interface UserDao {
    User findById(int userId);

    /**
     * 查询站点管理员
     *
     * @param logicGroupId
     * @return
     */
    User findSiteManager(int logicGroupId);

}
