package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.UserDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * @author bastengao
 * @date 14-5-4 上午10:20
 */
@Repository
@Scope("prototype")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Override
    public User findById(int userId) {
        return getSqlSession().selectOne("User.findById", userId);
    }

    @Override
    public User findSiteManager(int logicGroupId) {
        return getSqlSession().selectOne("User.findSiteManager", logicGroupId);
    }
}
